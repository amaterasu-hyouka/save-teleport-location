package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element;

import org.bukkit.inventory.ItemStack;

public interface InventoryItem {
    int getSlot();
    ItemStack getItem();
}
