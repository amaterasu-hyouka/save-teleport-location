package jp.amaterasu_hyouka.saveteleportlocation.listener;

import jp.amaterasu_hyouka.saveteleportlocation.inventory.TeleportLocationInventory;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.History;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.CategoryIdValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerCategoryValue;
import jp.amaterasu_hyouka.saveteleportlocation.inventory.core.type.PlayerUuidValue;
import jp.amaterasu_hyouka.saveteleportlocation.model.SaveTeleportLocation;
import jp.amaterasu_hyouka.saveteleportlocation.repository.LocationCategoryRepository;
import jp.amaterasu_hyouka.saveteleportlocation.repository.SaveTeleportLocationRepository;
import jp.amaterasu_hyouka.saveteleportlocation.util.ItemUtil;
import jp.amaterasu_hyouka.saveteleportlocation.util.Task;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    private static final BlockPlaceListener blockPlaceListener = new BlockPlaceListener();
    private BlockPlaceListener(){}
    public static BlockPlaceListener getInstance(){return blockPlaceListener;}

    private final SaveTeleportLocationRepository saveTeleportLocationRepository = SaveTeleportLocationRepository.getInstance();
    private final LocationCategoryRepository locationCategoryRepository = LocationCategoryRepository.getInstance();

    public static final ItemStack BEACON = ItemUtil.createItem(Material.BEACON, "置いてテレポート場所を保存");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if(!p.isOp()) return;

        ItemStack item = e.getItemInHand();
        if(item.getType().isAir()) return;
        if(!ItemUtil.getCustomName(BEACON).equals(ItemUtil.getCustomName(item))) return;
        e.setCancelled(true);

        int slot = p.getInventory().getHeldItemSlot();
        p.getInventory().clear(slot);

        History history = TeleportLocationInventory.getInstance().getHistoryMap(p.getUniqueId());
        if(history == null) return;

        PlayerCategoryValue value = history.playerCategoryValue();
        if(value == null) return;

        Location saveLocation = toCardinalLocation(e.getBlockPlaced().getLocation(), p.getLocation().getYaw());

        Material belowMaterial = e.getBlockPlaced().getLocation().clone().add(0, -1, 0).getBlock().getType();
        if (!belowMaterial.isItem() || belowMaterial.isAir())belowMaterial = Material.STONE;

        SaveTeleportLocation st = SaveTeleportLocation.from(value, p, belowMaterial.name(), saveLocation);

        if(st == null){
            p.sendMessage("エラーが発生しました");
            return;
        }

        Task.runAsync(() -> saveTeleportLocationRepository.insert(st));

        if (value instanceof PlayerUuidValue) {
            p.sendMessage("現在位置を保存しました");
            p.playSound(p.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 1.0f, 1.0f);
        } else if (value instanceof CategoryIdValue categoryIdValue) {
            Task.supplyAsync(
                    () -> locationCategoryRepository.getById(categoryIdValue.id()),
                    locationCategory -> {
                        p.sendMessage("現在位置を保存しました(" + locationCategory.name() + ")");
                        p.playSound(p.getLocation(), Sound.UI_STONECUTTER_SELECT_RECIPE, 1.0f, 1.0f);
                    }
            );
        }
    }

    public Location toCardinalLocation(Location base, float playerYaw) {
        Location loc = base.clone().add(0.5, 0, 0.5);
        loc.setYaw(toCardinalYaw(playerYaw));
        loc.setPitch(0);
        return loc;
    }

    public float toCardinalYaw(float yaw) {
        float normalizedYaw = normalizeYaw(yaw);

        if (normalizedYaw >= 315 || normalizedYaw < 45) {
            return 0f;     // 南
        } else if (normalizedYaw < 135) {
            return 90f;    // 西
        } else if (normalizedYaw < 225) {
            return 180f;   // 北
        } else {
            return 270f;   // 東
        }
    }

    public static float normalizeYaw(float yaw) {
        yaw %= 360;
        if (yaw < 0) yaw += 360;
        return yaw;
    }
}
