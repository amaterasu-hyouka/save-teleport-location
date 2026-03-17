package jp.amaterasu_hyouka.saveteleportlocation.util;

import org.bukkit.Bukkit;

import java.util.UUID;

public class Util {
    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
    public static boolean isOnline(String uuidText) {
        if (uuidText == null || uuidText.isBlank()) return false;

        try {
            UUID uuid = UUID.fromString(uuidText);
            return Bukkit.getPlayer(uuid) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
