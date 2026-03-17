package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.SettingMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public record CategoryListInventoryHolder(
        Inventory inventory,
        SettingMode settingMode,
        int page
) implements CustomInventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public static CategoryListInventoryHolder init(Player p) {
        return new CategoryListInventoryHolder(
                null,
                SettingMode.NORMAL,
                1
        );
    }

    public CategoryListInventoryHolder addPage(int movePage) {
        return new CategoryListInventoryHolder(
                null,
                settingMode,
                page + movePage
        );
    }

    public CategoryListInventoryHolder setSettingMode(SettingMode settingMode) {
        return new CategoryListInventoryHolder(
                null,
                settingMode,
                page
        );
    }
}
