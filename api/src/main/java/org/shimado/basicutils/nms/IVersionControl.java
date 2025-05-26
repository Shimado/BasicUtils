package org.shimado.basicutils.nms;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;

import java.awt.*;
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

}
