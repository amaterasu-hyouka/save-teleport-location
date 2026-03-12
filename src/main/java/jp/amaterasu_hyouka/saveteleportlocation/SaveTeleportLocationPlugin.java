package jp.amaterasu_hyouka.saveteleportlocation;

import jp.amaterasu_hyouka.saveteleportlocation.db.mybatis.DatabaseRegistry;
import jp.amaterasu_hyouka.saveteleportlocation.exception.FileCreationException;
import jp.amaterasu_hyouka.saveteleportlocation.listener.InitializeListener;
import jp.amaterasu_hyouka.saveteleportlocation.util.Log;

import org.bukkit.plugin.java.JavaPlugin;

import static jp.amaterasu_hyouka.saveteleportlocation.util.Log.setLogger;

public final class SaveTeleportLocationPlugin extends JavaPlugin {

    private static SaveTeleportLocationPlugin instance;
    public static JavaPlugin getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setLogger(this);
        Log.info("SaveTeleportLocationを起動中...");

        try {
            new PluginFile(this);
            DatabaseRegistry.initializeAll();
        } catch (FileCreationException e) {
            Log.info("SaveTeleportLocationの起動に失敗しました");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new InitializeListener(this);

        Log.info("SaveTeleportLocationを起動しました");
    }

    @Override
    public void onDisable() {
        Log.info("SaveTeleportLocationを停止中...");
        Log.info("SaveTeleportLocationを停止しました");
    }
}
