package jp.amaterasu_hyouka.saveteleportlocation.listener;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.CategoryListInventory;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.TeleportLocationInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class QuitServerListener implements Listener {

    private final CategoryListInventory categoryListInventory = CategoryListInventory.getInstance();
    private final TeleportLocationInventory teleportLocationInventory = TeleportLocationInventory.getInstance();
    private final AsyncChatListener asyncChatListener = AsyncChatListener.getInstance();


    @EventHandler
    public void handle(PlayerQuitEvent e){
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        categoryListInventory.quit(uuid);
        teleportLocationInventory.quit(uuid);
        asyncChatListener.quit(uuid);
    }
}
