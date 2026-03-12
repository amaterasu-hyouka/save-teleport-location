package jp.amaterasu_hyouka.saveteleportlocation.model;

import org.bukkit.Location;

public record SaveTeleportLocation(
        Integer id,
        String name,
        String groupKey,
        String materialName,
        String world,
        double x,
        double y,
        double z,
        float yaw,
        float pitch,
        Integer priority
) {
    public SaveTeleportLocation(String name, String groupKey, String materialName, Location location) {
        this(
                null,
                name,
                groupKey,
                materialName,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                null
        );
    }
}
