package jp.amaterasu_hyouka.saveteleportlocation.inventory;

import io.papermc.paper.event.player.AsyncChatEvent;
import jp.amaterasu_hyouka.saveteleportlocation.SaveTeleportLocationKey;
import jp.amaterasu_hyouka.saveteleportlocation.core.CustomAsyncChat;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.*;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element.BaseInventoryItem;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.CategoryListInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.CustomInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.TeleportLocationInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.CategoryIdValue;
import jp.amaterasu_hyouka.saveteleportlocation.listener.AsyncChatListener;
import jp.amaterasu_hyouka.saveteleportlocation.listener.InventoryClickListener;
import jp.amaterasu_hyouka.saveteleportlocation.model.LocationCategory;
import jp.amaterasu_hyouka.saveteleportlocation.repository.LocationCategoryRepository;
import jp.amaterasu_hyouka.saveteleportlocation.util.InventoryUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.Task;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

import static jp.amaterasu_hyouka.saveteleportlocation.util.InventoryUtil.SIX_ROWS;

public class CategoryListInventory extends AbstractCustomInventory implements CustomInventoryClick, InventoryPage, CustomAsyncChat {
    private final Component INVENTORY_TITLE = Component.text("Category Select");
    private final Inventory baseInventory = Bukkit.createInventory(null, SIX_ROWS, INVENTORY_TITLE);
    private static final int SETTING_MODE_SLOT = 2;

    private static final CategoryListInventory locationCategoryInventory = new CategoryListInventory();
    private CategoryListInventory(){
        bindItemAction(baseInventory, BaseInventoryItem.Default.class);
        setAllElement(baseInventory, BaseInventoryItem.SettingMode.class);
        setSettingModeMap();
        bindItemAction(baseInventory, BaseInventoryItem.InventoryType.CATEGORY_LIST);
        setInventoryDividerItem(baseInventory);
        setElement(baseInventory, BaseInventoryItem.Category.CREATE_CATEGORY);

        InventoryClickListener.getInstance().register(CategoryListInventoryHolder.class, this);
    }
    public static CategoryListInventory getInstance(){return locationCategoryInventory;}

    private final LocationCategoryRepository locationCategoryRepository = LocationCategoryRepository.getInstance();
    private final TeleportLocationInventory teleportLocationInventory = TeleportLocationInventory.getInstance();

    @Override
    public void setInventory(Player p) {
        setInventoryPerPlayer(p, CategoryListInventoryHolder.init(p));
    }

    public void setInventoryPerPlayer(Player p, CategoryListInventoryHolder holder){
        Inventory inventory = InventoryUtil.cloneInventory(holder, INVENTORY_TITLE, baseInventory);
        holder.settingMode().setItem(inventory, SETTING_MODE_SLOT);

        Task.supplyAsync(
                () -> locationCategoryRepository.getAllInRange((holder.page() - 1) * InventoryPage.PAGE_SIZE, InventoryPage.PAGE_SIZE),
                list -> {
                    setPageItem(inventory, list.stream().map(LocationCategory::item).toList(), holder.page());
                    p.openInventory(inventory);
                }
        );
    }

    @Override
    public void handle(InventoryClickEvent e, CustomInventoryHolder customHolder) {
        if (!(customHolder instanceof CategoryListInventoryHolder holder)) return;
        Player p = (Player) e.getWhoClicked();

        int slot = e.getRawSlot();
        ItemStack clickedItem = e.getCurrentItem();
        Material material = clickedItem.getType();

        Consumer<Player> action = getAction(slot, material);
        if(action != null){action.accept(p);return;}

        if(shouldAction(slot, material, BaseInventoryItem.Category.CREATE_CATEGORY)){
            String materialName = ItemUtil.getMaterialNameFrom(p.getInventory().getItemInMainHand());
            locationCategoryRepository.insert(new LocationCategory(null, "", materialName, null));
            p.sendMessage("新しいカテゴリーを作成しました");
            setInventoryPerPlayer(p, holder);
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
        void execute(Player player, ItemStack clickedItem, CategoryListInventoryHolder holder);
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
    public void handleNormal(Player p, ItemStack item, CategoryListInventoryHolder holder){
        Integer categoryId = SaveTeleportLocationKey.getCategoryId(item);
        if(categoryId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        teleportLocationInventory.setInventoryPerPlayer(p, TeleportLocationInventoryHolder.from(new CategoryIdValue(categoryId)));
    }

    private final Map<UUID, Integer> renameTargetMap = new HashMap<>();
    public void handleRename(Player p, ItemStack item, CategoryListInventoryHolder holder){
        Integer categoryId = SaveTeleportLocationKey.getCategoryId(item);
        if(categoryId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        renameTargetMap.put(p.getUniqueId(), categoryId);
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
                () -> locationCategoryRepository.updateNameById(targetId, finalNewName),
                () -> {
                    player.sendMessage(Component.text("名前を変更しました: " + finalNewName));
                    setInventory(player);
                }
        );
    }

    public void handleChangeMaterial(Player p, ItemStack item, CategoryListInventoryHolder holder) {
        Integer categoryId = SaveTeleportLocationKey.getCategoryId(item);
        if(categoryId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}

        ItemStack mainHandItem = p.getInventory().getItemInMainHand();
        Material material = mainHandItem.getType();
        if(material.isAir()){p.sendMessage(Component.text("保存するアイテムを手に持ってください"));return;}

        Task.runAsync(
                () -> locationCategoryRepository.updateMaterialNameById(categoryId, material.name()),
                () -> {
                    p.sendMessage(Component.text("アイテムを " + material.name() + " に変更しました"));
                    setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.NORMAL));
                }
        );
    }

    private final Map<UUID, Integer> moveMap = new HashMap<>();
    public void handleMove(Player p, ItemStack item, CategoryListInventoryHolder holder){
        Integer categoryId = SaveTeleportLocationKey.getCategoryId(item);
        if(categoryId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        moveMap.put(p.getUniqueId(), categoryId);
        setInventoryPerPlayer(p, holder.setSettingMode(SettingMode.MOVE_TARGET));
    }
    public void handleMoveTarget(Player p, ItemStack item, CategoryListInventoryHolder holder){
        Integer targetId = SaveTeleportLocationKey.getCategoryId(item);
        if(targetId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        Integer categoryId = moveMap.remove(p.getUniqueId());
        if(categoryId == null){p.sendMessage("移動中にエラーが起きました");setInventory(p);return;}
        if(targetId.equals(categoryId)){p.sendMessage("同じアイテムは移動できません");setInventory(p);return;}

        Task.supplyAsync(() -> {
            Integer priorityId = locationCategoryRepository.getPriorityById(categoryId);
            Integer priorityTargetId = locationCategoryRepository.getPriorityById(targetId);

            if (priorityId == null || priorityTargetId == null) return false;

            locationCategoryRepository.updatePriorityById(categoryId, priorityTargetId);
            locationCategoryRepository.updatePriorityById(targetId, priorityId);
            return true;
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

    public void handleDelete(Player p, ItemStack item, CategoryListInventoryHolder holder){
        Integer categoryId = SaveTeleportLocationKey.getCategoryId(item);
        if(categoryId == null){p.sendMessage(Component.text("IDの取得に失敗しました"));return;}
        Task.runAsync(
                () -> locationCategoryRepository.deleteById(categoryId),
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
