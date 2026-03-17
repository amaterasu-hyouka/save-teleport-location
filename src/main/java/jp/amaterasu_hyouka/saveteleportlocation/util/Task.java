package jp.amaterasu_hyouka.saveteleportlocation.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Task {

    private static JavaPlugin plugin;

    private Task(){}

    public static void init(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
    }

    public static void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    public static void runSync(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    public static void runAsync(Runnable task, Runnable callback) {
        runAsync(() -> {
            task.run();
            runSync(callback);
        });
    }

    public static <T> void supplyAsync(Supplier<T> supplier, Consumer<T> callback) {
        runAsync(() -> {
            T result = supplier.get();
            runSync(() -> callback.accept(result));
        });
    }


}
