package jp.amaterasu_hyouka.saveteleportlocation.listener;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.CustomInventoryClick;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.CustomInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {

    private static final InventoryClickListener inventoryClickListener = new InventoryClickListener();
    private InventoryClickListener(){}
    public static InventoryClickListener getInstance(){return inventoryClickListener;}

    private final Map<Class<? extends CustomInventoryHolder>, CustomInventoryClick> holderToClickMap = new HashMap<>();

    public void register(Class<? extends CustomInventoryHolder> holderClass, CustomInventoryClick click){
        holderToClickMap.put(holderClass, click);
    }

    @EventHandler
    public void handle(InventoryClickEvent e) {
        if (!e.getWhoClicked().isOp()) return;

        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) return;

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;

        Inventory topInventory = e.getView().getTopInventory();
        if (!(topInventory.getHolder() instanceof CustomInventoryHolder customHolder)) return;

        e.setCancelled(true);

        if (clickedInventory != topInventory) return;

        CustomInventoryClick clickHandler = holderToClickMap.get(customHolder.getClass());
        if (clickHandler == null) return;

        clickHandler.handle(e, customHolder);
    }
    @EventHandler
    public void handleDrag(InventoryDragEvent e) {
        Inventory topInventory = e.getView().getTopInventory();
        if (!(topInventory.getHolder() instanceof CustomInventoryHolder)) return;
        e.setCancelled(true);
    }

}
