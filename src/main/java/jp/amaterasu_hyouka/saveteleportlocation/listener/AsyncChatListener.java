package jp.amaterasu_hyouka.saveteleportlocation.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import jp.amaterasu_hyouka.saveteleportlocation.core.CustomAsyncChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AsyncChatListener implements Listener {

    private static final AsyncChatListener asyncChatListener = new AsyncChatListener();
    private AsyncChatListener(){}
    public static AsyncChatListener getInstance(){return asyncChatListener;}

    private final Map<UUID, CustomAsyncChat> tempCustomAsyncChatMap = new HashMap<>();

    public void putTempCustomAsyncChatMap(UUID uuid, CustomAsyncChat customAsyncChat){
        tempCustomAsyncChatMap.put(uuid, customAsyncChat);
    }
    public void quit(UUID uuid){
        tempCustomAsyncChatMap.remove(uuid);
    }

    @EventHandler
    public void handle(AsyncChatEvent e) {
        Player p = e.getPlayer();
        CustomAsyncChat tempCustomAsyncChat = tempCustomAsyncChatMap.get(p.getUniqueId());
        if(tempCustomAsyncChat == null)return;
        e.setCancelled(true);
        tempCustomAsyncChat.handle(e);
    }
}
