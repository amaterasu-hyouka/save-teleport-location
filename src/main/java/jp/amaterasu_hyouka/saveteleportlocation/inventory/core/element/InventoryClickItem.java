package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface InventoryClickItem {
    int getSlot();
    ItemStack getItem();
    Consumer<Player> getAction();
}