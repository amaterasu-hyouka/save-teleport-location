package jp.amaterasu_hyouka.saveteleportlocation.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jp.amaterasu_hyouka.saveteleportlocation.util.Util.hasText;

public class ItemUtil {

    public static ItemStack createItem(final Material material) {
        return createItem(material, 1, Component.empty());
    }
    public static ItemStack createItem(final Material material, final int amount) {
        return createItem(material, amount, Component.empty());
    }
    public static ItemStack createItem(final Material material, final int amount, final String name) {
        return createItem(material, amount, TextUtil.plain(name));
    }
    public static ItemStack createItem(final Material material, final int amount, final String name, final TextColor color) {
        return createItem(material, amount, Component.text(name, color));
    }

    public static ItemStack createItem(final Material material, final String name) {
        return createItem(material, 1, TextUtil.plain(name));
    }
    public static ItemStack createItem(final Material material, final String name, final TextColor color) {
        return createItem(material, 1, Component.text(name, color));
    }
    public static ItemStack createItem(final Material material, final Component component){
        return createItem(material, 1, component);
    }
    public static ItemStack createItem(final Material material, final Component component, final List<Component> lore){
        return createItem(material, 1, component, lore);
    }
    public static ItemStack createItem(final Material material, final Component component, final Component... lore){
        return createItem(material, 1, component, lore);
    }

    public static ItemStack createItem(final Material material, final int amount, final Component name) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        if(meta == null)return item;
        meta.displayName(TextUtil.clearItalic(name));
        meta.setMaxStackSize(amount);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(final Material material, final String name, final Component... lore) {
        return createItem(material, 1, TextUtil.plain(name), lore);
    }
    public static ItemStack createItem(final Material material, final int amount, final Component name, final Component... lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        if(meta == null)return item;
        meta.displayName(TextUtil.clearItalic(name));
        meta.lore(TextUtil.clearItalic(lore));
        meta.setMaxStackSize(amount);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack createItem(final Material material, final int amount, Component name, final List<Component> lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        if(meta == null)return item;
        meta.displayName(TextUtil.clearItalic(name));
        meta.setMaxStackSize(amount);
        meta.lore(TextUtil.clearItalic(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(final Material material, final int amount, Component name, final boolean enchant, final List<Component> lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        if(meta == null)return item;

        if(name == null)name = Component.empty();
        meta.displayName(name);

        if(enchant){
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setMaxStackSize(amount);
        meta.lore(TextUtil.clearItalic(lore));

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPlayerHead(String playerUuid, String itemName) {
        if(!hasText(playerUuid))return createItem(Material.PLAYER_HEAD, itemName);

        UUID uuid = UUID.fromString(playerUuid);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.displayName(Component.text(itemName).decoration(TextDecoration.ITALIC, false));
        if (!(itemMeta instanceof SkullMeta meta)) {
            item.setItemMeta(itemMeta);
            return item;
        }

        if(Bukkit.getPlayer(uuid) == null){
            item.setItemMeta(meta);
            return item;
        }
        meta.setPlayerProfile(Bukkit.createProfile(uuid));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean hasCustomName(ItemStack item){
        return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomName();
    }
    public static String getCustomName(ItemStack item){
        if(hasCustomName(item))return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(item.getItemMeta().customName()));
        return "";
    }

    public static Material fromMaterialOrDefault(String materialName) {
        if (materialName == null || materialName.isBlank()) return Material.STONE;

        Material material = Material.matchMaterial(materialName);
        if (material == null) return Material.STONE;

        return material;
    }
    public static String getMaterialNameFrom(ItemStack item) {
        if (item == null || item.getType().isAir()) return Material.STONE.name();
        return item.getType().name();
    }
}
