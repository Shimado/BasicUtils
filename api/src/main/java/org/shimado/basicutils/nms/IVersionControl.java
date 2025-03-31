package org.shimado.basicutils.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;

import java.awt.*;

public interface IVersionControl {

    ItemStack createItemWithTag(ItemStack item, String tag, String value);
    String getTag(ItemStack item, String tag);
    void moveHeadToBottom(Player player);
    void setPixelColor(MapCanvas mapCanvas, int x, int y, Color color);
    Color getBasePixelColor(MapCanvas mapCanvas, int x, int y);

}
