package org.shimado.basicutils.nms;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.shimado.basicutils.instances.Pair;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface IVersionControl {

    ItemStack createItemWithTag(ItemStack item, String tag, String value);
    ItemStack createItemWithTags(ItemStack item, Map<String, String> map);
    String getTag(ItemStack item, String tag);
    void moveHeadToBottom(Player player);
    void setPixelColor(MapCanvas mapCanvas, int x, int y, Color color);
    Color getBasePixelColor(MapCanvas mapCanvas, int x, int y);
    void createFirework(Player player, Location loc, ItemStack firework);
    GameProfile getGameProfile(Player player);
    BlockFace getFacing(Player player);
    void spawnParticleNote(World world, double x, double y, double z, float color);
    void spawnParticleDust(World world, double x, double y, double z, float r, float g, float b);
    Pair spawnArmorStandByLocation(Location loc, org.bukkit.inventory.ItemStack itemHead, boolean isSmall, float angleX, float angleY, float angleZ);
    void moveStand(Object entity, Location newLoc, Location oldLoc);
    void removeEntity(Object stand);
    void rotateStand(Object stand, int angle);
    void spawnArmorStandToPlayer(Player player, Object stand, ItemStack item, Object packet);
    Object createItem(List<Player> players, Location loc, ItemStack itemToDrop, double vectorX, double vectorY, double vectorZ);

}
