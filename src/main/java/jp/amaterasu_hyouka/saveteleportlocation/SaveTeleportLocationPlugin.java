package jp.amaterasu_hyouka.saveteleportlocation;

import jp.amaterasu_hyouka.saveteleportlocation.utils.Log;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveTeleportLocationPlugin extends JavaPlugin {

    private static SaveTeleportLocationPlugin instance;
    public static JavaPlugin getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Log.info("SaveTeleportLocationを起動中...");
        Log.info("SaveTeleportLocationを起動しました");
    }

    @Override
    public void onDisable() {
        Log.info("SaveTeleportLocationを停止中...");
        Log.info("SaveTeleportLocationを停止しました");
    }
}
