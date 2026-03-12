package jp.amaterasu_hyouka.saveteleportlocation.listener;

import jp.amaterasu_hyouka.saveteleportlocation.model.LoginPlayer;
import jp.amaterasu_hyouka.saveteleportlocation.repository.PlayerRepository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class JoinServerListener implements Listener {

    private final PlayerRepository playerRepository = PlayerRepository.getInstance();

    @EventHandler
    public void handle(AsyncPlayerPreLoginEvent e){
        String uuid = e.getUniqueId().toString();
        String savedName = playerRepository.getNameByUuid(uuid);
        if (savedName == null || !savedName.equals(e.getName())) {
            LoginPlayer player = new LoginPlayer(uuid, e.getName());
            playerRepository.insertOrReplaceData(player);
        }
    }
}
