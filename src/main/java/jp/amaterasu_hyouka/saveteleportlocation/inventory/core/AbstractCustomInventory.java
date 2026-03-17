package jp.amaterasu_hyouka.saveteleportlocation.inventory.core;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element.InventoryClickItem;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.element.InventoryItem;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractCustomInventory {

    protected final Map<Integer, Map<Material, Consumer<Player>>> actionMap = new HashMap<>();

    protected <E extends Enum<E> & InventoryItem> void setElement(Inventory inventory, E element){
        inventory.setItem(element.getSlot(), element.getItem());
    }
    protected <E extends Enum<E> & InventoryItem> void setElement(Inventory inventory, int slot, ItemStack item){
        inventory.setItem(slot, item);
    }
    protected <E extends Enum<E> & InventoryItem> void setAllElement(Inventory inventory, Class<E> elementClass){
        for(E element: elementClass.getEnumConstants())inventory.setItem(element.getSlot(), element.getItem());
    }
    protected <E extends Enum<E> & InventoryItem> boolean shouldAction(int slot, Material material, E element){
        return element.getSlot() == slot && element.getItem().getType() == material;
    }

    protected <E extends Enum<E> & InventoryItem> void registerAction(E element, Consumer<Player> action){
        actionMap.computeIfAbsent(element.getSlot(), k -> new HashMap<>()).put(element.getItem().getType(), action);
    }

    protected <E extends Enum<E> & InventoryClickItem> void bindItemAction(Inventory inventory, Class<E> elementClass){
        for(E element: elementClass.getEnumConstants()){
            inventory.setItem(element.getSlot(), element.getItem());
            actionMap.computeIfAbsent(element.getSlot(), k -> new HashMap<>()).put(element.getItem().getType(), element.getAction());
        }
    }
    protected <E extends Enum<E> & InventoryClickItem> void bindItemAction(Inventory inventory, E element){
        inventory.setItem(element.getSlot(), element.getItem());
        actionMap.computeIfAbsent(element.getSlot(), k -> new HashMap<>()).put(element.getItem().getType(), element.getAction());
    }
    protected <E extends Enum<E> & InventoryItem> void bindItemAction(Inventory inventory, E element, Consumer<Player> action){
        inventory.setItem(element.getSlot(), element.getItem());
        actionMap.computeIfAbsent(element.getSlot(), k -> new HashMap<>()).put(element.getItem().getType(), action);
    }

    protected void setAction(int slot, ItemStack item, Consumer<Player> action){
        actionMap.computeIfAbsent(slot, k -> new HashMap<>()).put(item.getType(), action);
    }
    protected void setAction(int slot, Material material, Consumer<Player> action){
        actionMap.computeIfAbsent(slot, k -> new HashMap<>()).put(material, action);
    }

    protected Consumer<Player> getAction(int slot, Material material){
        Map<Material, Consumer<Player>> slotActions = actionMap.get(slot);
        return slotActions != null ? slotActions.get(material) : null;
    }

    private final static ItemStack dividerItem = ItemUtil.createItem(Material.IRON_BARS);
    public void setInventoryDividerItem(Inventory inventory){
        for(int i = 9; i < 18; i++){
            inventory.setItem(i, dividerItem);
        }
    }

    public abstract void setInventory(Player p);
}
