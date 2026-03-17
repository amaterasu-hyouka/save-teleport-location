package jp.amaterasu_hyouka.saveteleportlocation.inventory.core;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.holder.TeleportLocationInventoryHolder;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerCategoryValue;

public record History(
        PlayerCategoryValue playerCategoryValue,
        int page
) {
    public static History from(TeleportLocationInventoryHolder holder){
        return new History(holder.playerCategoryValue(), holder.page());
    }
}
