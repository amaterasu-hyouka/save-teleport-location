package jp.amaterasu_hyouka.saveteleportlocation.inventory.core;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.CustomInventoryHolder;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface CustomInventoryClick {
    void handle(InventoryClickEvent e, CustomInventoryHolder customHolder);
}
