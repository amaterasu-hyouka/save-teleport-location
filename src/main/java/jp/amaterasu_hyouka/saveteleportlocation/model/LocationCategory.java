package jp.amaterasu_hyouka.saveteleportlocation.model;

import jp.amaterasu_hyouka.saveteleportlocation.SaveTeleportLocationKey;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public record LocationCategory(
        Integer id,
        String name,
        String materialName,
        Integer priority
) {
    public ItemStack item(){
        ItemStack item = ItemUtil.createItem(ItemUtil.fromMaterialOrDefault(materialName), name);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)return item;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(SaveTeleportLocationKey.CATEGORY_ID, PersistentDataType.INTEGER, id);
        item.setItemMeta(meta);
        return item;
    }
}
