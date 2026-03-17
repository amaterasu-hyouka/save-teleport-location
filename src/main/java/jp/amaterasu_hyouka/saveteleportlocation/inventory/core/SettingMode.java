package jp.amaterasu_hyouka.saveteleportlocation.inventory.core;

import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public enum SettingMode {
    NORMAL(null),
    RENAME(ItemUtil.createItem(Material.NAME_TAG,"名前変更モード")),
    CHANGE_MATERIAL(ItemUtil.createItem(Material.WHITE_DYE, "アイテム変更モード")),
    MOVE(ItemUtil.createItem(Material.MINECART, "入れ替えモード")),
    MOVE_TARGET(ItemUtil.createItem(Material.MINECART, 2, "入れ替えモード")),
    DELETE(ItemUtil.createItem(Material.REDSTONE_BLOCK, "削除モード"));

    private final ItemStack item;
    SettingMode(ItemStack item) {
        this.item = item;
    }

    public void setItem(Inventory inventory, int slot) {
        if(item == null)return;
        inventory.setItem(slot, item);
    }
}
