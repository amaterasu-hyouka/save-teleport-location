package jp.amaterasu_hyouka.saveteleportlocation.inventory;

import io.papermc.paper.event.player.AsyncChatEvent;
import jp.amaterasu_hyouka.saveteleportlocation.SaveTeleportLocationKey;
import jp.amaterasu_hyouka.saveteleportlocation.core.CustomAsyncChat;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.*;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element.BaseInventoryItem;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.CustomInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.TeleportLocationInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.CategoryIdValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerCategoryValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerUuidValue;
import jp.amaterasu_hyouka.saveteleportlocation.listener.AsyncChatListener;
import jp.amaterasu_hyouka.saveteleportlocation.listener.InventoryClickListener;
import jp.amaterasu_hyouka.saveteleportlocation.model.LocationCategory;
import jp.amaterasu_hyouka.saveteleportlocation.model.SaveTeleportLocation;
import jp.amaterasu_hyouka.saveteleportlocation.repository.LocationCategoryRepository;
import jp.amaterasu_hyouka.saveteleportlocation.repository.SaveTeleportLocationRepository;
import jp.amaterasu_hyouka.saveteleportlocation.util.InventoryUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.Task;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

import static jp.amaterasu_hyouka.saveteleportlocation.listener.BlockPlaceListener.BEACON;
import static jp.amaterasu_hyouka.saveteleportlocation.util.InventoryUtil.SIX_ROWS;

public class TeleportLocationInventory extends AbstractCustomInventory implements CustomInventoryClick, InventoryPage, CustomAsyncChat {
    private final Component INVENTORY_TITLE = Component.text("Teleport Location");
    private final Inventory baseInventory = Bukkit.createInventory(null, SIX_ROWS, INVENTORY_TITLE);
    private static final int SETTING_MODE_SLOT = 2;

    private static final TeleportLocationInventory teleportLocationInventory = new TeleportLocationInventory();
    private TeleportLocationInventory(){
        bindItemAction(baseInventory, BaseInventoryItem.Default.class);
        setAllElement(baseInventory, BaseInventoryItem.SaveLocation.class);
        setAllElement(baseInventory, BaseInventoryItem.SettingMode.class);
        setSettingModeMap();
        setInventoryDividerItem(baseInventory);

        InventoryClickListener.getInstance().register(TeleportLocationInventoryHolder.class, this);
    }
    public static TeleportLocationInventory getInstance(){return teleportLocationInventory;}

    private final SaveTeleportLocationRepository saveTeleportLocationRepository = SaveTeleportLocationRepository.getInstance();
    private final LocationCategoryRepository locationCategoryRepository = LocationCategoryRepository.getInstance();

    private final Map<UUID, History> historyMap = new HashMap<>();
    public History getHistoryMap(UUID uuid){
        return historyMap.get(uuid);
    }

    @Override
    public void setInventory(Player p) {
        History history = historyMap.get(p.getUniqueId());
        if(history == null){setInventoryInit(p);return;}
        setInventory(p, history);
    }
    public void setInventoryInit(Player p) {setInventoryPerPlayer(p, TeleportLocationInventoryHolder.init(p));}
    public void setInventory(Player p, History history) {setInventoryPerPlayer(p, TeleportLocationInventoryHolder.from(history));}

    public void setInventoryPerPlayer(Player p, TeleportLocationInventoryHolder holder){
        historyMap.put(p.getUniqueId(), History.from(holder));
        Inventory inventory = InventoryUtil.cloneInventory(holder, INVENTORY_TITLE, baseInventory);
        holder.settingMode().setItem(inventory, SETTING_MODE_SLOT);

        PlayerCategoryValue value = holder.playerCategoryValue();
        if (value instanceof PlayerUuidValue uuidValue) {
            if(!p.getUniqueId().toString().equals(uuidValue.uuid()))setClearSettingItem(inventory);
            inventory.setItem(BaseInventoryItem.InventoryType.PLAYER_LIST.getSlot(), ItemUtil.createPlayerHead(uuidValue.uuid(), uuidValue.name()));
            Task.supplyAsync(
                    () -> saveTeleportLocationRepository.findByPlayerUuidInRange(uuidValue.uuid(), (holder.page() - 1) * InventoryPage.PAGE_SIZE, InventoryPage.PAGE_SIZE),
                    list -> {
                        setPageItem(inventory, list.stream().map(SaveTeleportLocation::item).toList(), holder.page());
                        p.openInventory(inventory);
                    }
            );
        } else if (value instanceof CategoryIdValue categoryIdValue) {
            Integer categoryId = categoryIdValue.id();
            LocationCategory locationCategory = locationCategoryRepository.getById(categoryId);
            if(locationCategory == null){setInventoryInit(p);return;}
            inventory.setItem(BaseInventoryItem.InventoryType.PLAYER_LIST.getSlot(), locationCategory.item());
            Task.supplyAsync(
                    () -> saveTeleportLocationRepository.getByCategoryIdInRange(categoryId, (holder.page() - 1) * InventoryPage.PAGE_SIZE, InventoryPage.PAGE_SIZE),
                    list -> {
                        setPageItem(inventory, list.stream().map(SaveTeleportLocation::item).toList(), holder.page());
                        p.openInventory(inventory);
                    }
            );
        }
    }
    private void setClearSettingItem(Inventory inventory){
        for(BaseInventoryItem.SettingMode mode : BaseInventoryItem.SettingMode.values())inventory.clear(mode.getSlot());
        inventory.clear(BaseInventoryItem.SaveLocation.SAVE_LOCATION_NOW.getSlot());
        inventory.clear(BaseInventoryItem.SaveLocation.SAVE_LOCATION_BLOCK.getSlot());
    }

    @Override
    public void handle(InventoryClickEvent e, CustomInventoryHolder customHolder) {
        if (!(customHolder instanceof TeleportLocationInventoryHolder holder)) return;
        Player p = (Player) e.getWhoClicked();

        int slot = e.getRawSlot();
        ItemStack clickedItem = e.getCurrentItem();
        Material material = clickedItem.getType();

        Consumer<Player> action = getAction(slot, material);
        if(action != null){action.accept(p);return;}

        if(shouldAction(slot, material, BaseInventoryItem.SaveLocation.SAVE_LOCATION_NOW)){
            String materialName = ItemUtil.getMaterialNameFrom(p.getInventory().getItemInMainHand());
            SaveTeleportLocation st = SaveTeleportLocation.from(holder.playerCategoryValue(), p, materialName, p.getLocation());
            if(st == null){p.sendMessage("エラーが発生しました");return;}
            Task.runAsync(
                    () -> saveTeleportLocationRepository.insert(st),
                    () -> setInventoryPerPlayer(p, holder)
            );
            if (holder.playerCategoryValue() instanceof PlayerUuidValue){
                p.sendMessage("現在位置を保存しました");
                p.playSound(p.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 1.0f, 1.0f);
            }
            else if(holder.playerCategoryValue() instanceof CategoryIdValue value){
                Task.supplyAsync(
                        () -> locationCategoryRepository.getById(value.id()),
                        locationCategory -> {
                            p.sendMessage("現在位置を保存しました(" + locationCategory.name() + ")");
                            p.playSound(p.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 1.0f, 1.0f);
                        }
                );
            }
        }
        else if(shouldAction(slot, material, BaseInventoryItem.SaveLocation.SAVE_LOCATION_BLOCK)){
            p.getInventory().addItem(BEACON);
        }
        else if(slot >= 5 && slot < 9) {
            if     (shouldAction(slot, material, BaseInventoryItem.SettingMode.RENAME))         {setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.RENAME));}
            else if(shouldAction(slot, material, BaseInventoryItem.SettingMode.CHANGE_MATERIAL)){setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.CHANGE_MATERIAL));}
            else if(shouldAction(slot, material, BaseInventoryItem.SettingMode.MOVE))           {setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.MOVE));}
            else if(shouldAction(slot, material, BaseInventoryItem.SettingMode.DELETE))         {setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.DELETE));}
        }
        else if(slot >= 18 && slot < 45) {
            ClickAction settingAction = actions.get(holder.settingMode());
            if(settingAction == null)return;
            settingAction.execute(p, clickedItem, holder);
        }
        else{
            Integer movePage = InventoryPage.getMovePage(slot, material);
            if(movePage != null)setInventoryPerPlayer(p, holder.addPage(movePage));
        }
    }

    @FunctionalInterface
    public interface ClickAction {
        void execute(Player player, ItemStack clickedItem, TeleportLocationInventoryHolder holder);
    }
    private final EnumMap<SettingMode, ClickAction> actions = new EnumMap<>(SettingMode.class);
    private void setSettingModeMap() {
        actions.put(SettingMode.NORMAL, this::handleNormal);
        actions.put(SettingMode.RENAME, this::handleRename);
        actions.put(SettingMode.CHANGE_MATERIAL, this::handleChangeMaterial);
        actions.put(SettingMode.MOVE, this::handleMove);
        actions.put(SettingMode.MOVE_TARGET, this::handleMoveTarget);
        actions.put(SettingMode.DELETE, this::handleDelete);
    }
    public void handleNormal(Player p, ItemStack item, TeleportLocationInventoryHolder holder){
        Location location = SaveTeleportLocationKey.getLocation(item);
        if(location == null){p.sendMessage("テレポートエラー");return;}
        p.closeInventory();
        p.teleport(location);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    private final Map<UUID, Integer> renameTargetMap = new HashMap<>();
    public void handleRename(Player p, ItemStack item, TeleportLocationInventoryHolder holder){
        Integer id = SaveTeleportLocationKey.getId(item);
        if(id == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        renameTargetMap.put(p.getUniqueId(), id);
        p.closeInventory();
        p.sendMessage(Component.text("変更後の名前をチャットで入力してください"));
        p.sendMessage(Component.text("キャンセルする場合は「cancel」と入力してください"));
        AsyncChatListener.getInstance().putTempCustomAsyncChatMap(p.getUniqueId(), this);
    }
    @Override
    public void handle(AsyncChatEvent e){
        Player player = e.getPlayer();
        Integer targetId = renameTargetMap.remove(player.getUniqueId());
        if(targetId == null)return;
        String newName = PlainTextComponentSerializer.plainText().serialize(e.originalMessage()).trim();
        if (newName.equalsIgnoreCase("cancel")){
            player.sendMessage(Component.text("名前変更をキャンセルしました"));
            return;
        }
        if(newName.length() > 50){
            player.sendMessage(Component.text("名前は50文字以下で設定してください"));
            return;
        }

        final String finalNewName = ChatColor.translateAlternateColorCodes('&', newName);

        Task.runAsync(
                () -> saveTeleportLocationRepository.updateNameById(targetId, finalNewName),
                () -> {
                    player.sendMessage(Component.text("名前を変更しました: " + finalNewName));
                    setInventory(player);
                }
        );
    }

    public void handleChangeMaterial(Player p, ItemStack item, TeleportLocationInventoryHolder holder) {
        Integer id = SaveTeleportLocationKey.getId(item);
        if(id == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}

        ItemStack mainHandItem = p.getInventory().getItemInMainHand();
        Material material = mainHandItem.getType();
        if(material.isAir()){p.sendMessage(Component.text("保存するアイテムを手に持ってください"));return;}

        Task.runAsync(
                () -> saveTeleportLocationRepository.updateMaterialNameById(id, material.name()),
                () -> {
                    p.sendMessage(Component.text("アイテムを " + material.name() + " に変更しました"));
                    setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.NORMAL));
                }
        );
    }

    private final Map<UUID, Integer> moveMap = new HashMap<>();
    public void handleMove(Player p, ItemStack item, TeleportLocationInventoryHolder holder){
        Integer id = SaveTeleportLocationKey.getId(item);
        if(id == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        moveMap.put(p.getUniqueId(), id);
        setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.MOVE_TARGET));
    }
    public void handleMoveTarget(Player p, ItemStack item, TeleportLocationInventoryHolder holder){
        Integer targetId = SaveTeleportLocationKey.getId(item);
        if(targetId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        Integer id = moveMap.remove(p.getUniqueId());
        if(id == null){p.sendMessage("移動中にエラーが起きました");setInventory(p);return;}
        if(targetId.equals(id)){p.sendMessage("同じアイテムは移動できません");setInventory(p);return;}

        PlayerCategoryValue value = holder.playerCategoryValue();
        Task.supplyAsync(() -> {
            if (value instanceof PlayerUuidValue) {
                Integer priorityId = saveTeleportLocationRepository.getPlayerPriorityById(id);
                Integer priorityTargetId = saveTeleportLocationRepository.getPlayerPriorityById(targetId);
                if(priorityId == null || priorityTargetId == null)return false;
                saveTeleportLocationRepository.updatePlayerPriorityById(id, priorityTargetId);
                saveTeleportLocationRepository.updatePlayerPriorityById(targetId, priorityId);
                return true;
            }
            if (value instanceof CategoryIdValue) {
                Integer priorityId = saveTeleportLocationRepository.getCategoryPriorityById(id);
                Integer priorityTargetId = saveTeleportLocationRepository.getCategoryPriorityById(targetId);
                if(priorityId == null || priorityTargetId == null)return false;
                saveTeleportLocationRepository.updateCategoryPriorityById(id, priorityTargetId);
                saveTeleportLocationRepository.updateCategoryPriorityById(targetId, priorityId);
                return true;
            }
            return false;
        }, success -> {
            if (!success) {
                p.sendMessage(Component.text("移動中にエラーが起きました"));
                setInventory(p);
                return;
            }
            p.sendMessage(Component.text("アイテムを並び替えました"));
            setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.NORMAL));
        });
    }

    public void handleDelete(Player p, ItemStack item, TeleportLocationInventoryHolder holder){
        Integer id = SaveTeleportLocationKey.getId(item);
        if(id == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        Task.runAsync(
                () -> saveTeleportLocationRepository.deleteById(id),
                () -> {
                    p.sendMessage((ItemUtil.getCustomName(item) + " 削除しました").trim());
                    setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.NORMAL));
                }
        );
    }

    public void quit(UUID uuid){
        renameTargetMap.remove(uuid);
        moveMap.remove(uuid);
    }
}
