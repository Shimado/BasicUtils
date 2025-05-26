package org.shimado.basicutils.v1_15_R1;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.shimado.basicutils.BasicUtils;
import org.shimado.basicutils.nms.IVersionControl;

import java.awt.*;
import java.util.Map;

public class VersionInstance implements IVersionControl {

    private final Color transparentColor = new Color(255, 255, 255, 0);


    @Override
    public ItemStack createItemWithTag(ItemStack item, String tag, String value) {
        net.minecraft.server.v1_15_R1.ItemStack itemNMS = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = itemNMS.getOrCreateTag();
        tagCompound.setString(tag, value);
        itemNMS.setTag(tagCompound);
        return CraftItemStack.asCraftMirror(itemNMS);
    }


    @Override
    public ItemStack createItemWithTags(ItemStack item, Map<String, String> map) {
        net.minecraft.server.v1_15_R1.ItemStack itemNMS = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = itemNMS.getOrCreateTag();
        for(Map.Entry<String, String> a : map.entrySet()){
            tagCompound.setString(a.getKey(), a.getValue());
        }
        itemNMS.setTag(tagCompound);
        return CraftItemStack.asCraftMirror(itemNMS);
    }


    @Override
    public String getTag(ItemStack item, String tag){
        if(item == null || item.getType().equals(Material.AIR)) return "";
        net.minecraft.server.v1_15_R1.ItemStack itemNMS = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = itemNMS.getTag();
        return tagCompound != null ? tagCompound.getString(tag) : "";
    }


    @Override
    public void moveHeadToBottom(Player player){
        Location loc = player.getLocation().clone();
        loc.setPitch(90);
        player.teleport(loc);
    }


    @Override
    public void setPixelColor(MapCanvas mapCanvas, int x, int y, Color color){
        mapCanvas.setPixel(x, y, MapPalette.matchColor(color));
    }


    @Override
    public Color getBasePixelColor(MapCanvas mapCanvas, int x, int y){
        try {
            byte color = mapCanvas.getBasePixel(x, y);
            return MapPalette.getColor(color);
        }catch (Exception e){
            return transparentColor;
        }
    }


    @Override
    public void createFirework(Player player, Location loc, ItemStack fireworkItem) {
        EntityFireworks firework = new EntityFireworks(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(fireworkItem));
        NMSUtil.sendPacket(player, new PacketPlayOutSpawnEntity(firework));
        firework.expectedLifespan = 0;
        NMSUtil.sendPacket(player, new PacketPlayOutEntityMetadata(NMSUtil.getEntityID(firework), firework.getDataWatcher(), false));
        NMSUtil.sendPacket(player, new PacketPlayOutEntityStatus(firework, (byte) 17));
        NMSUtil.sendPacket(player, new PacketPlayOutEntityDestroy(NMSUtil.getEntityID(firework)));
    }

    @Override
    public GameProfile getGameProfile(Player player){
        return ((CraftPlayer) player).getProfile();
    }

}
