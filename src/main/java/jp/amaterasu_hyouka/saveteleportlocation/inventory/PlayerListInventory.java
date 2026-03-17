package jp.amaterasu_hyouka.saveteleportlocation.inventory;

import jp.amaterasu_hyouka.saveteleportlocation.SaveTeleportLocationKey;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.*;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element.BaseInventoryItem;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.CustomInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.PlayerListInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.TeleportLocationInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.listener.InventoryClickListener;
import jp.amaterasu_hyouka.saveteleportlocation.model.LoginPlayer;
import jp.amaterasu_hyouka.saveteleportlocation.repository.PlayerRepository;
import jp.amaterasu_hyouka.saveteleportlocation.util.InventoryUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.Task;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static jp.amaterasu_hyouka.saveteleportlocation.util.InventoryUtil.SIX_ROWS;
import static jp.amaterasu_hyouka.saveteleportlocation.util.Util.isOnline;

public class PlayerListInventory extends AbstractCustomInventory implements CustomInventoryClick, InventoryPage {
    private final Component INVENTORY_TITLE = Component.text("Player Select");
    private final Inventory baseInventory = Bukkit.createInventory(null, SIX_ROWS, INVENTORY_TITLE);

    private static final PlayerListInventory playerListInventory = new PlayerListInventory();
    private PlayerListInventory(){
        bindItemAction(baseInventory, BaseInventoryItem.Default.class);
        setInventoryDividerItem(baseInventory);
        bindItemAction(baseInventory, BaseInventoryItem.InventoryType.PLAYER_LIST);

        InventoryClickListener.getInstance().register(PlayerListInventoryHolder.class, this);
    }
    public static PlayerListInventory getInstance(){return playerListInventory;}

    private final PlayerRepository playerRepository = PlayerRepository.getInstance();

    @Override
    public void setInventory(Player p) {
        setInventoryPerPlayer(p, new PlayerListInventoryHolder(null, 1));
    }

    public void setInventoryPerPlayer(Player p, PlayerListInventoryHolder holder){
        Inventory inventory = InventoryUtil.cloneInventory(holder, INVENTORY_TITLE, baseInventory);

        Task.supplyAsync(
                playerRepository::getAll,
                list -> {
                    List<ItemStack> items = list.stream()
                            .sorted(Comparator.comparing((LoginPlayer player) -> !player.uuid().equals(p.getUniqueId().toString())).thenComparing(player -> !isOnline(player.uuid())))
                            .map(LoginPlayer::item)
                            .toList();

                    setPageItem(inventory, items, holder.page());
                    p.openInventory(inventory);
                }
        );
    }

    @Override
    public void handle(InventoryClickEvent e, CustomInventoryHolder customHolder) {
        if (!(customHolder instanceof PlayerListInventoryHolder holder)) return;
        Player p = (Player) e.getWhoClicked();

        int slot = e.getRawSlot();
        ItemStack clickedItem = e.getCurrentItem();
        Material material = clickedItem.getType();

        Consumer<Player> action = getAction(slot, material);
        if(action != null){action.accept(p);return;}

        if(slot >= 18 && slot < 45) {
            String playerUuid = SaveTeleportLocationKey.getPlayerUuid(clickedItem);
            String playerName = ItemUtil.getCustomName(clickedItem);
            if(playerUuid == null){p.sendMessage(Component.text("UUIDの取得に失敗しました"));return;}
            TeleportLocationInventory.getInstance().setInventoryPerPlayer(p, TeleportLocationInventoryHolder.from(playerUuid, playerName));
        }else{
            Integer movePage = InventoryPage.getMovePage(slot, material);
            if(movePage != null)setInventoryPerPlayer(p, holder.addPage(movePage));
        }
    }
}
