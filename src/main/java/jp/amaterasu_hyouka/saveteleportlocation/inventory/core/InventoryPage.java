package jp.amaterasu_hyouka.saveteleportlocation.inventory.core;

import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface InventoryPage {
    int[] CONTENT_SLOTS = {18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};
    int PAGE_SIZE = 27;
    int PAGE_PREV_SLOT = 45;
    int PAGE_NEXT_SLOT = 53;

    default void setPageItem(Inventory inventory, List<ItemStack> items, int page){
        int size = Math.min(items.size(), PAGE_SIZE);
        for(int i = 0; i < size; i++) inventory.setItem(CONTENT_SLOTS[i], items.get(i));
        if(page <= 0) return;

        boolean hasPrev = page > 1;
        boolean hasNext = size == PAGE_SIZE;
        inventory.setItem(PAGE_PREV_SLOT,
                hasPrev ? ItemUtil.createItem(Material.ARROW, (page - 1), (page - 1) + "ページ目へ")
                        : ItemUtil.createItem(Material.STRUCTURE_VOID)
        );
        inventory.setItem(
                PAGE_NEXT_SLOT,
                hasNext ? ItemUtil.createItem(Material.ARROW, (page + 1), (page + 1) + "ページ目へ")
                        : ItemUtil.createItem(Material.STRUCTURE_VOID)
        );
    }

    static Integer getMovePage(int slot, Material material){
        if(material == Material.ARROW){
            if(slot == PAGE_PREV_SLOT)return -1;
            if(slot == PAGE_NEXT_SLOT)return 1;
        }
        return null;
    }
}
