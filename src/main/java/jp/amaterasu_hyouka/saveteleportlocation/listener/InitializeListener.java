package jp.amaterasu_hyouka.saveteleportlocation.listener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InitializeListener {

    public InitializeListener(JavaPlugin plugin){
        Bukkit.getServer().getPluginManager().registerEvents(new JoinServerListener(), plugin);
    }

}
