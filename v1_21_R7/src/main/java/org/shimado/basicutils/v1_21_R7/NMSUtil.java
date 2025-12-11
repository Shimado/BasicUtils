package org.shimado.basicutils.v1_21_R7;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil {

    public static EntityPlayer getEntityPlayer(Player player){
        return ((CraftPlayer) player).getHandle();
    }

    public static int getEntityID(Entity entity){
        return entity.aA();
    }

    public static WorldServer getWorld(Location loc){
        return ((CraftWorld) loc.getWorld()).getHandle();
    }

    public static void sendPacket(Player player, Packet packet){
        ((ServerCommonPacketListenerImpl) getEntityPlayer(player).g).b(packet);
    }

    public static DataWatcher getDataWatcher(Entity entity){
        return entity.aD();
    }

    public static void setWatcher(DataWatcher watcher, DataWatcherObject<Byte> field, byte bytes){
        watcher.a(field, bytes);
    }

    public static PacketPlayOutSpawnEntity getPacketPlayOutSpawnEntity(Entity entity, Location loc){
        return new PacketPlayOutSpawnEntity(getEntityID(entity), entity.cY(), loc.getX(), loc.getY(), loc.getZ(), entity.ec(), entity.ee(), entity.ay(), 1, entity.dN(), entity.cS() * 1.0);
    }

    public static PacketPlayOutSpawnEntity getPacketPlayOutSpawnEntity(Entity entity){
        return new PacketPlayOutSpawnEntity(getEntityID(entity), entity.cY(), entity.dP(), entity.dR(), entity.dV(), entity.ec(), entity.ee(), entity.ay(), 1, entity.dN(), entity.cS() * 1.0);
    }

}

