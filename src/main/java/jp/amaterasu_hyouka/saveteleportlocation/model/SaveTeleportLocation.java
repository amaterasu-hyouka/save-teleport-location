package jp.amaterasu_hyouka.saveteleportlocation.model;

import jp.amaterasu_hyouka.saveteleportlocation.SaveTeleportLocationKey;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.CategoryIdValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerCategoryValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerUuidValue;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public record SaveTeleportLocation(
        Integer id,
        String name,
        String playerUuid,
        String playerName,
        Integer categoryId,
        String materialName,
        String world,
        double x,
        double y,
        double z,
        float yaw,
        float pitch,
        Integer player_priority,
        Integer category_priority
) {

    public static SaveTeleportLocation from(PlayerCategoryValue value, Player p, Location location){
        if (value instanceof PlayerUuidValue) {
            return new SaveTeleportLocation(null, "", p.getUniqueId().toString(), p.getName(), null, Material.STONE.name(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), null, null);
        }else if (value instanceof CategoryIdValue categoryIdValue) {
            if(categoryIdValue.id() == null)return null;
            return new SaveTeleportLocation(null, "", p.getUniqueId().toString(), p.getName(), categoryIdValue.id(), Material.STONE.name(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), null, null);
        }
        return null;
    }
    public static SaveTeleportLocation from(PlayerCategoryValue value, Player p, String materialName, Location location){
        if(materialName == null)materialName = Material.STONE.name();
        if (value instanceof PlayerUuidValue) {
            return new SaveTeleportLocation(null, "", p.getUniqueId().toString(), p.getName(), null, materialName, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), null, null);
        }else if (value instanceof CategoryIdValue categoryIdValue) {
            if(categoryIdValue.id() == null)return null;
            return new SaveTeleportLocation(null, "", p.getUniqueId().toString(), p.getName(), categoryIdValue.id(), materialName, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), null, null);
        }
        return null;
    }


    public SaveTeleportLocation(String name, Player p, ItemStack item, Location location) {
        this(
                null,
                name,
                p.getUniqueId().toString(),
                p.getName(),
                null,
                item == null ? Material.STONE.name() : item.getType().name(),
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                null,
                null
        );
    }
    public SaveTeleportLocation(String name, Player p, Integer categoryId, ItemStack item, Location location) {
        this(
                null,
                name,
                p.getUniqueId().toString(),
                p.getName(),
                categoryId,
                item == null ? Material.STONE.name() : item.getType().name(),
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                null,
                null
        );
    }

    public ItemStack item(){
        Material material = ItemUtil.fromMaterialOrDefault(materialName);
        if(!material.isItem() || material.isAir())material = Material.STONE;
        ItemStack item = ItemUtil.createItem(material, name);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)return item;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(SaveTeleportLocationKey.ID, PersistentDataType.INTEGER, id);
        pdc.set(SaveTeleportLocationKey.WORLD, PersistentDataType.STRING, world);
        pdc.set(SaveTeleportLocationKey.X, PersistentDataType.DOUBLE, x);
        pdc.set(SaveTeleportLocationKey.Y, PersistentDataType.DOUBLE, y);
        pdc.set(SaveTeleportLocationKey.Z, PersistentDataType.DOUBLE, z);
        pdc.set(SaveTeleportLocationKey.YAW, PersistentDataType.FLOAT, yaw);
        pdc.set(SaveTeleportLocationKey.PITCH, PersistentDataType.FLOAT, pitch);
        item.setItemMeta(meta);
        return item;
    }
}
