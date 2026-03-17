package jp.amaterasu_hyouka.saveteleportlocation.listener;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.TeleportLocationInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;


public class SwapItemListener implements Listener {

    private final TeleportLocationInventory teleportLocationInventory = TeleportLocationInventory.getInstance();

    @EventHandler
    public void handle(PlayerSwapHandItemsEvent e){
        Player p = e.getPlayer();
        if(!p.isOp() || p.isSneaking())return;

        e.setCancelled(true);
        teleportLocationInventory.setInventory(p);
    }
}
