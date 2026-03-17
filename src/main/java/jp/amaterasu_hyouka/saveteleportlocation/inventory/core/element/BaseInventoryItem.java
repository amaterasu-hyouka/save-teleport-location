package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.CategoryListInventory;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.PlayerListInventory;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.TeleportLocationInventory;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class BaseInventoryItem {

    static Component ARROW_CLICK = Component.text("➡ 左クリックで", NamedTextColor.GOLD).decorate(TextDecoration.UNDERLINED);
    static Component LINE_BREAK = Component.empty();

    public enum Default implements InventoryClickItem {
        TELEPORT_LOCATION(0, ItemUtil.createItem(Material.ENDER_PEARL, "テレポート場所", NamedTextColor.LIGHT_PURPLE),
                p -> TeleportLocationInventory.getInstance().setInventoryInit(p)),
        PLAYER_LIST(47, ItemUtil.createItem(Material.PLAYER_HEAD, "プレイヤー選択"),
                p -> PlayerListInventory.getInstance().setInventory(p)),
        CATEGORY_LIST(48, ItemUtil.createItem(Material.WHITE_SHULKER_BOX, "カテゴリー選択"),
                p -> CategoryListInventory.getInstance().setInventory(p));

        final int slot;                @Override public int getSlot(){return slot;}
        final ItemStack item;          @Override public ItemStack getItem(){return item;}
        final Consumer<Player> action; @Override public Consumer<Player> getAction(){return action;}
        Default(int slot, ItemStack item, Consumer<Player> action){this.slot = slot; this.item = item; this.action = action;}
    }

    public enum InventoryType implements InventoryClickItem {
        PLAYER_LIST(1, Default.PLAYER_LIST.getItem(), Default.PLAYER_LIST.getAction()),
        CATEGORY_LIST(1, Default.CATEGORY_LIST.getItem(), Default.CATEGORY_LIST.getAction());

        final int slot;                @Override public int getSlot(){return slot;}
        final ItemStack item;          @Override public ItemStack getItem(){return item;}
        final Consumer<Player> action; @Override public Consumer<Player> getAction(){return action;}
        InventoryType(int slot, ItemStack item, Consumer<Player> action){this.slot = slot; this.item = item; this.action = action;}
    }

    public enum Category implements InventoryItem {
        CREATE_CATEGORY(50, ItemUtil.createItem(Material.SHULKER_SHELL,"新しいカテゴリーを作成する"));

        final int slot;       @Override public int getSlot(){return slot;}
        final ItemStack item; @Override public ItemStack getItem(){return item;}
        Category(int slot, ItemStack item){this.slot = slot; this.item = item;}
    }

    public enum SaveLocation implements InventoryItem {
        SAVE_LOCATION_NOW(50, ItemUtil.createItem(Material.REDSTONE_TORCH, "現在位置を保存する")),
        SAVE_LOCATION_BLOCK(51, ItemUtil.createItem(Material.BEACON, "位置を保存するブロックを入手"));

        final int slot;       @Override public int getSlot(){return slot;}
        final ItemStack item; @Override public ItemStack getItem(){return item;}
        SaveLocation(int slot, ItemStack item){this.slot = slot; this.item = item;}
    }

    public enum SettingMode implements InventoryItem {
        RENAME(5, ItemUtil.createItem(Material.NAME_TAG,"名前変更モード")),
        CHANGE_MATERIAL(6, ItemUtil.createItem(Material.WHITE_DYE, "アイテム変更モード")),
        MOVE(7, ItemUtil.createItem(Material.MINECART, "入れ替えモード")),
        DELETE(8, ItemUtil.createItem(Material.REDSTONE_BLOCK, "削除モード"));

        final int slot;       @Override public int getSlot(){return slot;}
        final ItemStack item; @Override public ItemStack getItem(){return item;}
        SettingMode(int slot, ItemStack item){this.slot = slot; this.item = item;}
    }

}
