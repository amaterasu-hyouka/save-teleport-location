package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public record PlayerListInventoryHolder(
        Inventory inventory,
        int page
) implements CustomInventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public PlayerListInventoryHolder addPage(int movePage) {
        return new PlayerListInventoryHolder(
                null,
                page + movePage
        );
    }
}
