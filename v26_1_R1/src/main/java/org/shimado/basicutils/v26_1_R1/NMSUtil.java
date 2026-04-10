package org.shimado.basicutils.v26_1_R1;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class NMSUtil {

    public static final Class<?> ServerPlayer;
    public static final Class<?> CraftPlayer;
    public static final Class<?> CraftWorld;
    public static final Class<?> Packet;
    public static final Class<?> EntityDataAccessor;
    public static final Class<?> ClientboundAddEntityPacket;
    public static final Class<?> ClientboundOpenScreenPacket;
    public static final Class<?> MenuType;
    public static final Class<?> Component;
    public static final Class<?> BlockPos;
    public static final Class<?> ContainerLevelAccess;
    public static final Class<?> Level;
    public static final Class<?> AnvilMenu;
    public static final Class<?> Inventory;
    public static final Class<?> AbstractContainerMenu;
    public static final Class<?> FireworkRocketEntity;
    public static final Class<?> ItemStack;
    public static final Class<?> CraftItemStack;
    public static final Class<?> ClientboundSetEntityDataPacket;
    public static final Class<?> ClientboundEntityEventPacket;
    public static final Class<?> ClientboundRemoveEntitiesPacket;
    public static final Class<?> ClientboundLevelParticlesPacket;
    public static final Class<?> ParticleTypes;
    public static final Class<?> ParticleOptions;
    public static final Class<?> DustParticleOptions;
    public static final Class<?> ArmorStand;
    public static final Class<?> Entity;
    public static final Class<?> Rotations;
    public static final Class<?> EquipmentSlot;
    public static final Class<?> ClientboundSetEquipmentPacket;
    public static final Class<?> Pair;
    public static final Class<?> ClientboundMoveEntityPacket$Pos;
    public static final Class<?> ClientboundMoveEntityPacket$PosRot;
    public static final Class<?> ItemEntity;
    public static final Class<?> ServerCommonPacketListenerImpl;
    public static final Class<?> SynchedEntityData;

    public static final Method getPlayerHandler;
    public static final Method getWorldHandler;
    public static final Method asNMSCopy;
    public static final Method getId;
    public static final Method sendPacket;
    public static final Method getEntityData;
    public static final Method setEntityData;
    public static final Method getEntityUUID;
    public static final Method getEntityX;
    public static final Method getEntityY;
    public static final Method getEntityZ;
    public static final Method getEntityYRot;
    public static final Method getEntityXRot;
    public static final Method getEntityType;
    public static final Method getEntityDeltaMovement;
    public static final Method getEntityYHeadRot;
    public static final Method getProfile;

    public static final Field connection;

    public static final Constructor ClientboundAddEntityPacketConstructor;

    static {
        try {
            ServerPlayer = getClass("net.minecraft.server.level.ServerPlayer");
            CraftPlayer = getClass("org.bukkit.craftbukkit.entity.CraftPlayer");
            CraftWorld = getClass("org.bukkit.craftbukkit.CraftWorld");
            Packet = getClass("net.minecraft.network.protocol.Packet");
            EntityDataAccessor = getClass("net.minecraft.network.syncher.EntityDataAccessor");
            ClientboundAddEntityPacket = getClass("net.minecraft.network.protocol.game.ClientboundAddEntityPacket");
            ClientboundOpenScreenPacket = getClass("net.minecraft.network.protocol.game.ClientboundOpenScreenPacket");
            MenuType = getClass("net.minecraft.world.inventory.MenuType");
            Component = getClass("net.minecraft.network.chat.Component");
            BlockPos = getClass("net.minecraft.core.BlockPos");
            ContainerLevelAccess = getClass("net.minecraft.world.inventory.ContainerLevelAccess");
            Level = getClass("net.minecraft.world.level.Level");
            AnvilMenu = getClass("net.minecraft.world.inventory.AnvilMenu");
            Inventory = getClass("net.minecraft.world.entity.player.Inventory");
            AbstractContainerMenu = getClass("net.minecraft.world.inventory.AbstractContainerMenu");
            FireworkRocketEntity = getClass("net.minecraft.world.entity.projectile.FireworkRocketEntity");
            ItemStack = getClass("net.minecraft.world.item.ItemStack");
            CraftItemStack = getClass("org.bukkit.craftbukkit.inventory.CraftItemStack");
            ClientboundSetEntityDataPacket = getClass("net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket");
            ClientboundEntityEventPacket = getClass("net.minecraft.network.protocol.game.ClientboundEntityEventPacket");
            ClientboundRemoveEntitiesPacket = getClass("net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket");
            ClientboundLevelParticlesPacket = getClass("net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket");
            ParticleTypes = getClass("net.minecraft.core.particles.ParticleTypes");
            ParticleOptions = getClass("net.minecraft.core.particles.ParticleOptions");
            DustParticleOptions = getClass("net.minecraft.core.particles.DustParticleOptions");
            ArmorStand = getClass("net.minecraft.world.entity.decoration.ArmorStand");
            Entity = getClass("net.minecraft.world.entity.Entity");
            Rotations = getClass("net.minecraft.core.Rotations");
            EquipmentSlot = getClass("net.minecraft.world.entity.EquipmentSlot");
            ClientboundSetEquipmentPacket = getClass("net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket");
            Pair = getClass("com.mojang.datafixers.util.Pair");
            ClientboundMoveEntityPacket$Pos = getClass("net.minecraft.network.protocol.game.ClientboundMoveEntityPacket$Pos");
            ClientboundMoveEntityPacket$PosRot = getClass("net.minecraft.network.protocol.game.ClientboundMoveEntityPacket$PosRot");
            ItemEntity = getClass("net.minecraft.world.entity.item.ItemEntity");
            ServerCommonPacketListenerImpl = getClass("net.minecraft.server.network.ServerCommonPacketListenerImpl");

            SynchedEntityData = getClass("net.minecraft.network.syncher.SynchedEntityData");

            getPlayerHandler = CraftPlayer.getMethod("getHandle");
            getWorldHandler = CraftWorld.getMethod("getHandle");
            asNMSCopy = CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            getId = Entity.getMethod("getId");

            Method sendPacketMethod;
            try {
                sendPacketMethod = ServerCommonPacketListenerImpl.getMethod("sendPacket", Packet);
            }catch (Exception ex){
                sendPacketMethod = ServerCommonPacketListenerImpl.getMethod("send", Packet);
            }
            sendPacket = sendPacketMethod;

            getEntityData = Entity.getMethod("getEntityData");
            setEntityData = SynchedEntityData.getMethod("set", EntityDataAccessor, Object.class);
            getEntityUUID = Entity.getMethod("getUUID");
            getEntityX = Entity.getMethod("getX");
            getEntityY = Entity.getMethod("getY");
            getEntityZ = Entity.getMethod("getZ");
            getEntityYRot = Entity.getMethod("getYRot");
            getEntityXRot = Entity.getMethod("getXRot");
            getEntityType = Entity.getMethod("getType");
            getEntityDeltaMovement = Entity.getMethod("getDeltaMovement");
            getEntityYHeadRot = Entity.getMethod("getYHeadRot");
            getProfile = CraftPlayer.getMethod("getProfile");

            connection = ServerPlayer.getField("connection");

            ClientboundAddEntityPacketConstructor = ClientboundAddEntityPacket.getConstructor(int.class, UUID.class, double.class, double.class, double.class, float.class, float.class, getClass("net.minecraft.world.entity.EntityType"), int.class, getClass("net.minecraft.world.phys.Vec3"), double.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Class<?> getClass(String name) throws Exception {
        return Class.forName(name);
    }


    //ServerPlayer
    public static Object getEntityPlayer(Player player) throws Exception {
        return getPlayerHandler.invoke(CraftPlayer.cast(player));
    }

    //ItemStack
    public static Object getItem(org.bukkit.inventory.ItemStack item) throws Exception {
        return asNMSCopy.invoke(null, item);
    }


    public static int getEntityID(Object entity) throws Exception {
        return (int) getId.invoke(entity);
    }

    //ServerLevel
    public static Object getWorld(Location loc) throws Exception{
        return getWorldHandler.invoke(CraftWorld.cast(loc.getWorld()));
    }


    public static void sendPacket(Player player, Object packet) throws Exception{
        sendPacket.invoke(connection.get(getEntityPlayer(player)), packet);
    }

    //SynchedEntityData
    public static Object getDataWatcher(Object entity) throws Exception {
        return getEntityData.invoke(entity);
    }

    public static void setWatcher(Object watcher, Object dataAccessor, byte value) throws Exception{
        setEntityData.invoke(watcher, dataAccessor, value);
    }


    //ClientboundAddEntityPacket
    public static Object getPacketPlayOutSpawnEntity(Object entity, Location loc) throws Exception{
        return ClientboundAddEntityPacketConstructor.newInstance(
                getEntityID(entity),
                getEntityUUID.invoke(entity),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                getEntityYRot.invoke(entity),
                getEntityXRot.invoke(entity),
                getEntityType.invoke(entity),
                1,
                getEntityDeltaMovement.invoke(entity),
                getEntityYHeadRot.invoke(entity)
        );
    }

    //ClientboundAddEntityPacket
    public static Object getPacketPlayOutSpawnEntity(Object entity) throws Exception{
        return ClientboundAddEntityPacketConstructor.newInstance(
                getEntityID(entity),
                getEntityUUID.invoke(entity),
                getEntityX.invoke(entity),
                getEntityY.invoke(entity),
                getEntityZ.invoke(entity),
                getEntityYRot.invoke(entity),
                getEntityXRot.invoke(entity),
                getEntityType.invoke(entity),
                1,
                getEntityDeltaMovement.invoke(entity),
                getEntityYHeadRot.invoke(entity)
        );
    }
}




//public class NMSUtil {
//
//    public static ServerPlayer getEntityPlayer(Player player){
//        return ((CraftPlayer) player).getHandle();
//    }
//
//    public static int getEntityID(Entity entity){
//        return entity.getId();
//    }
//
//    public static ServerLevel getWorld(Location loc){
//        return ((CraftWorld) loc.getWorld()).getHandle();
//    }
//
//    public static void sendPacket(Player player, Packet packet){
//        getEntityPlayer(player).connection.sendPacket(packet);
//    }
//
//    public static SynchedEntityData getDataWatcher(Entity entity){
//        return entity.getEntityData();
//    }
//
//    public static void setWatcher(SynchedEntityData watcher, EntityDataAccessor<Byte> field, byte bytes){
//        watcher.set(field, bytes);
//    }
//
//    public static ClientboundAddEntityPacket getPacketPlayOutSpawnEntity(Entity entity, Location loc){
//        return new ClientboundAddEntityPacket(getEntityID(entity), entity.getUUID(), loc.getX(), loc.getY(), loc.getZ(), entity.getYRot(), entity.getXRot(), entity.getType(), 1, entity.getDeltaMovement(), entity.getYHeadRot() * 1.0);
//    }
//
//    public static ClientboundAddEntityPacket getPacketPlayOutSpawnEntity(Entity entity){
//        return new ClientboundAddEntityPacket(getEntityID(entity), entity.getUUID(), entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot(), entity.getType(), 1, entity.getDeltaMovement(), entity.getYHeadRot() * 1.0);
//    }
//
//}

