package jp.amaterasu_hyouka.saveteleportlocation.model;

import jp.amaterasu_hyouka.saveteleportlocation.SaveTeleportLocationKey;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public record LoginPlayer(
        String uuid,
        String name
) {
    public ItemStack item(){
        ItemStack item = ItemUtil.createPlayerHead(uuid, name);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)return item;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(SaveTeleportLocationKey.PLAYER_UUID, PersistentDataType.STRING, uuid);

        item.setItemMeta(meta);
        return item;
    }
}
