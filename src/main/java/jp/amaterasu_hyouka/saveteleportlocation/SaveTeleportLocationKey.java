package jp.amaterasu_hyouka.saveteleportlocation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaveTeleportLocationKey {
    private SaveTeleportLocationKey() {}

    public static NamespacedKey ID, WORLD, X, Y, Z, YAW, PITCH;
    public static NamespacedKey PLAYER_UUID, PLAYER_NAME;
    public static NamespacedKey CATEGORY_ID;

    public static void init(JavaPlugin plugin) {
        ID = new NamespacedKey(plugin, "save_teleport_location_id");
        WORLD = new NamespacedKey(plugin, "save_teleport_location_world");
        X = new NamespacedKey(plugin, "save_teleport_location_x");
        Y = new NamespacedKey(plugin, "save_teleport_location_y");
        Z = new NamespacedKey(plugin, "save_teleport_location_z");
        YAW = new NamespacedKey(plugin, "save_teleport_location_yaw");
        PITCH = new NamespacedKey(plugin, "save_teleport_location_pitch");

        PLAYER_UUID = new NamespacedKey(plugin, "save_teleport_location_player_uuid");
        PLAYER_NAME = new NamespacedKey(plugin, "save_teleport_location_player_name");

        CATEGORY_ID = new NamespacedKey(plugin, "save_teleport_location_category_id");
    }

    public static Integer getId(ItemStack item){
        if(item == null || item.getType().isAir())return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(ID, PersistentDataType.INTEGER);
    }
    public static Integer getCategoryId(ItemStack item){
        if(item == null || item.getType().isAir())return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(CATEGORY_ID, PersistentDataType.INTEGER);
    }


    public static Location getLocation(ItemStack item){
        if(item == null || item.getType().isAir())return null;

        ItemMeta meta = item.getItemMeta();
        if(meta == null)return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String worldName = pdc.get(WORLD, PersistentDataType.STRING);
        Double x = pdc.get(X, PersistentDataType.DOUBLE);
        Double y = pdc.get(Y, PersistentDataType.DOUBLE);
        Double z = pdc.get(Z, PersistentDataType.DOUBLE);
        Float yaw = pdc.get(YAW, PersistentDataType.FLOAT);
        Float pitch = pdc.get(PITCH, PersistentDataType.FLOAT);

        if(worldName == null || x == null || y == null || z == null || yaw == null || pitch == null)return null;

        World world = Bukkit.getWorld(worldName);
        if (world == null)return null;

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getPlayerUuid(ItemStack item){
        if(item == null || item.getType().isAir())return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(PLAYER_UUID, PersistentDataType.STRING);
    }
    public static String getPlayerName(ItemStack item){
        if(item == null || item.getType().isAir())return null;
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(PLAYER_NAME, PersistentDataType.STRING);
    }
}
