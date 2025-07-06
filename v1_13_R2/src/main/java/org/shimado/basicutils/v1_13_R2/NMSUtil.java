package org.shimado.basicutils.v1_13_R2;


import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil {

    public static EntityPlayer getEntityPlayer(Player player){
        return ((CraftPlayer) player).getHandle();
    }

    public static int getEntityID(Entity entity){
        return entity.getId();
    }

    public static WorldServer getWorld(Location loc){
        return ((CraftWorld) loc.getWorld()).getHandle();
    }

    public static void sendPacket(Player player, Packet packet){
        getEntityPlayer(player).playerConnection.sendPacket(packet);
    }

    public static DataWatcher getDataWatcher(Entity entity){
        return entity.getDataWatcher();
    }

    public static void setWatcher(DataWatcher watcher, DataWatcherObject<Byte> field, byte bytes){
        watcher.set(field, bytes);
    }

}
