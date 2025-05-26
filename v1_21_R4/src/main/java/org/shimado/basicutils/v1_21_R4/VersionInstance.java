package org.shimado.basicutils.v1_21_R4;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.projectile.EntityFireworks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
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
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(BasicUtils.getPlugin(), tag), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }


    @Override
    public ItemStack createItemWithTags(ItemStack item, Map<String, String> map) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for(Map.Entry<String, String> a : map.entrySet()){
            container.set(new NamespacedKey(BasicUtils.getPlugin(), a.getKey()), PersistentDataType.STRING, a.getValue());
        }
        item.setItemMeta(meta);
        return item;
    }


    @Override
    public String getTag(ItemStack item, String tag){
        if(item == null || item.getType().equals(Material.AIR)) return "";
        NamespacedKey key = new NamespacedKey(BasicUtils.getPlugin(), tag.toLowerCase().replace(" ", ""));
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }


    @Override
    public void moveHeadToBottom(Player player){
        Location loc = player.getLocation().clone();
        loc.setPitch(90);
        player.teleport(loc);
    }


    @Override
    public void setPixelColor(MapCanvas mapCanvas, int x, int y, Color color){
        mapCanvas.setPixelColor(x, y, color);
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
        NMSUtil.sendPacket(player, new PacketPlayOutSpawnEntity(NMSUtil.getEntityID(firework), firework.cG(), loc.getX(), loc.getY(), loc.getZ(), firework.dL(), firework.dN(), firework.an(), 1, firework.dy(), firework.cA() * 1.0));
        firework.h = 0;
        NMSUtil.sendPacket(player, new PacketPlayOutEntityMetadata(NMSUtil.getEntityID(firework), firework.ar().b()));
        NMSUtil.sendPacket(player, new PacketPlayOutEntityStatus(firework, (byte) 17));
        NMSUtil.sendPacket(player, new PacketPlayOutEntityDestroy(NMSUtil.getEntityID(firework)));
    }


    @Override
    public GameProfile getGameProfile(Player player){
        return ((CraftPlayer) player).getProfile();
    }

}
