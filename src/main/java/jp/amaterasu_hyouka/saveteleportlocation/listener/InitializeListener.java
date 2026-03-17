package jp.amaterasu_hyouka.saveteleportlocation.listener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class  InitializeListener {

    public InitializeListener(JavaPlugin plugin){
        Bukkit.getServer().getPluginManager().registerEvents(AsyncChatListener.getInstance(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinServerListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(InventoryClickListener.getInstance(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(BlockPlaceListener.getInstance(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new SwapItemListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitServerListener(), plugin);
    }

}
