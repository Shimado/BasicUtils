package org.shimado.basicutils.v1_21_R4;


import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil {

    public static EntityPlayer getEntityPlayer(Player player){
        return ((CraftPlayer) player).getHandle();
    }

    public static int getEntityID(Entity entity){
        return entity.ao();
    }

    public static WorldServer getWorld(Location loc){
        return ((CraftWorld) loc.getWorld()).getHandle();
    }

    public static void sendPacket(Player player, Packet packet){
        ((ServerCommonPacketListenerImpl) getEntityPlayer(player).f).b(packet);
    }

    public static DataWatcher getDataWatcher(Entity entity){
        return entity.ar();
    }


    public static void setWatcher(DataWatcher watcher, DataWatcherObject<Byte> field, byte bytes){
        watcher.a(field, bytes);
    }

}
