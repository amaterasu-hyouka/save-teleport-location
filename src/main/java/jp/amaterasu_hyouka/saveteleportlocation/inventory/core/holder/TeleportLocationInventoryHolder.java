package jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.History;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.SettingMode;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.CategoryIdValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerCategoryValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerUuidValue;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public record TeleportLocationInventoryHolder(
        Inventory inventory,
        PlayerCategoryValue playerCategoryValue,
        SettingMode settingMode,
        int page
) implements CustomInventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public static TeleportLocationInventoryHolder init(Player p) {
        return new TeleportLocationInventoryHolder(
                null,
                new PlayerUuidValue(p.getUniqueId().toString(), p.getName()),
                SettingMode.NORMAL,
                1
        );
    }
    public static TeleportLocationInventoryHolder from(CategoryIdValue categoryIdValue) {
        return new TeleportLocationInventoryHolder(
                null,
                categoryIdValue,
                SettingMode.NORMAL,
                1
        );
    }
    public static TeleportLocationInventoryHolder from(History history) {
        return new TeleportLocationInventoryHolder(
                null,
                history.playerCategoryValue(),
                SettingMode.NORMAL,
                history.page()
        );
    }
    public static TeleportLocationInventoryHolder from(History history, SettingMode settingMode) {
        return new TeleportLocationInventoryHolder(
                null,
                history.playerCategoryValue(),
                settingMode,
                history.page()
        );
    }
    public static TeleportLocationInventoryHolder from(String playerUuid, String playerName) {
        return new TeleportLocationInventoryHolder(
                null,
                new PlayerUuidValue(playerUuid, playerName),
                SettingMode.NORMAL,
                1
        );
    }

    public TeleportLocationInventoryHolder addPage(int movePage) {
        return new TeleportLocationInventoryHolder(
                null,
                playerCategoryValue,
                settingMode,
                page + movePage
        );
    }

    public TeleportLocationInventoryHolder setSettingMode(SettingMode settingMode) {
        return new TeleportLocationInventoryHolder(
                null,
                playerCategoryValue,
                settingMode,
                page
        );
    }
}
