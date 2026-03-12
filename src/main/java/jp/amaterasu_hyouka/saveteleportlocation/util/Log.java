package jp.amaterasu_hyouka.saveteleportlocation.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private static final String PREFIX = "> ";
    private static Logger LOGGER;

    public static void setLogger(JavaPlugin plugin) {
        LOGGER = plugin.getLogger();
    }

    public static void info(String msg) {
        LOGGER.log(Level.INFO, PREFIX + msg);
    }

    public static void warn(String msg) {
        LOGGER.log(Level.WARNING, PREFIX + msg);
    }

    public static void error(String msg) {
        LOGGER.log(Level.SEVERE, PREFIX + msg);
    }

    public static void error(Throwable t) {
        LOGGER.log(Level.SEVERE, PREFIX + "Unexpected error", t);
    }

    public static void error(String msg, Throwable t) {
        LOGGER.log(Level.SEVERE, PREFIX + msg, t);
    }

    /** デバッグログ（必要な時だけ使う） */
    public static void debug(String msg) {
        LOGGER.log(Level.INFO, PREFIX + "[DEBUG] " + msg);
    }
}
