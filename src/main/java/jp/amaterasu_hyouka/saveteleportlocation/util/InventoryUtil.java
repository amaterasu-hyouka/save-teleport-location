package jp.amaterasu_hyouka.saveteleportlocation.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil {

    public static final int ONE_ROW = 9;
    public static final int TWO_ROWS = 18;
    public static final int THREE_ROWS = 27;
    public static final int FOUR_ROWS = 36;
    public static final int FIVE_ROWS = 45;
    public static final int SIX_ROWS = 54;

    public static Inventory cloneInventory(InventoryHolder holder, Component title, Inventory originalInventory){
        Inventory copyInventory = Bukkit.createInventory(holder, originalInventory.getSize(), title);
        copyInventory.setContents(originalInventory.getContents());
        return copyInventory;
    }

    public static void setEnchantSlotItem(Inventory inventory, int slot){
        ItemStack item = inventory.getItem(slot);
        if(item == null)return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setEnchantmentGlintOverride(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    public static void clear(Inventory inventory, Material material, String customName) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material && ItemUtil.getCustomName(item).equals(customName)) {
                inventory.remove(item);
            }
        }
    }
}
