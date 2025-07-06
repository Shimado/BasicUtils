package org.shimado.basicutils.v1_16_R2;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.shimado.basicutils.BasicUtils;
import org.shimado.basicutils.instances.Pair;
import org.shimado.basicutils.nms.IVersionControl;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class VersionInstance implements IVersionControl {

    private final Color transparentColor = new Color(255, 255, 255, 0);


    @Override
    public ItemStack createItemWithTag(ItemStack item, String tag, String value) {
        net.minecraft.server.v1_16_R2.ItemStack itemNMS = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = itemNMS.getOrCreateTag();
        tagCompound.setString(tag, value);
        itemNMS.setTag(tagCompound);
        return CraftItemStack.asCraftMirror(itemNMS);
    }


    @Override
    public ItemStack createItemWithTags(ItemStack item, Map<String, String> map) {
        net.minecraft.server.v1_16_R2.ItemStack itemNMS = CraftItemStack.asNMSCopy(item);
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
        net.minecraft.server.v1_16_R2.ItemStack itemNMS = CraftItemStack.asNMSCopy(item);
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


    @Override
    public BlockFace getFacing(Player player){
        return player.getFacing();
    }


    @Override
    public void spawnParticleNote(org.bukkit.World world, double x, double y, double z, float color) {
        Packet packet = new PacketPlayOutWorldParticles(Particles.NOTE, false, x, y, z, color, 0f, 0f, 1, 0);
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(world.getUID().equals(p.getWorld().getUID())){
                NMSUtil.sendPacket(p, packet);
            }
        });
    }


    @Override
    public void spawnParticleDust(org.bukkit.World world, double x, double y, double z, float r, float g, float b) {
        Packet packet = new PacketPlayOutWorldParticles(new ParticleParamRedstone(r, g, b, 1f), false, x, y, z, 0f, 0f, 0f, 1, 1);
        Bukkit.getOnlinePlayers().forEach(p -> {
            if(world.getUID().equals(p.getWorld().getUID())){
                NMSUtil.sendPacket(p, packet);
            }
        });
    }


    @Override
    public Pair spawnArmorStandByLocation(Location loc, org.bukkit.inventory.ItemStack itemHead, boolean isSmall, float angleX, float angleY, float angleZ){

        //СОЗДАНИЕ САМОГО АРМОР СТЕНДА
        Entity stand =  new EntityArmorStand(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ());

        //ОТПРАВКА ПАКЕТА НА СОЗДАНИЕ АРМОР СТЕНДА
        Packet packet = new PacketPlayOutSpawnEntity(stand);
        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));

        //ОТПРАВКА ПАКЕТА НА НАДЕВАНИЕ ГОЛОВЫ НА АРМОРСТЕНД;
        Packet packetHead = new PacketPlayOutEntityEquipment(NMSUtil.getEntityID(stand), Arrays.asList(new com.mojang.datafixers.util.Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemHead))));
        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packetHead));

        //ИНВИЗ И УДАЛЕНИЕ ПЛОЩАДКИ
        try {

            DataWatcher watcher = NMSUtil.getDataWatcher(stand);

            //УДАЛЕНИЕ ПЛОЩАКИ У АРМОРСТЕНДА
            Field plate = EntityArmorStand.class.getDeclaredField("b");
            plate.setAccessible(true);
            NMSUtil.setWatcher(watcher, (DataWatcherObject<Byte>) plate.get(stand), (byte) 0x08);

            //МАЛЕНЬКИЙ
            if(isSmall){
                Field small = EntityArmorStand.class.getDeclaredField("b");
                small.setAccessible(true);
                NMSUtil.setWatcher(watcher, (DataWatcherObject<Byte>) plate.get(stand), (byte) 0x01);
            }

            //УГОЛ ГОЛОВЫ
            Field angle = EntityArmorStand.class.getDeclaredField("c");
            angle.setAccessible(true);
            watcher.set((DataWatcherObject<Vector3f>) angle.get(stand), new Vector3f(angleX, angleY, angleZ));

            //ИНВИЗ ДЛЯ АРМОРСТЕНДА
            Field invis = Entity.class.getDeclaredField("S");
            invis.setAccessible(true);
            NMSUtil.setWatcher(watcher, (DataWatcherObject<Byte>) invis.get(stand), (byte) 0x20);

            //ОТПРАВКА ПАКЕТА НА ИНВИЗ И УДАЛЕНИЕ ПЛОЩАДКИ
            PacketPlayOutEntityMetadata packetMeta = new PacketPlayOutEntityMetadata(NMSUtil.getEntityID(stand), watcher, true);
            Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packetMeta));

            return new Pair(stand, packetMeta);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return new Pair(stand, null);

    }


    @Override
    public void moveStand(Object entity, Location newLoc, Location oldLoc){
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                NMSUtil.getEntityID((Entity) entity),
                (short) ((newLoc.getX() * 32 - oldLoc.getX() * 32) * 128),
                (short) (((newLoc.getY() + 1.7) * 32 - (oldLoc.getY() + 1.7) * 32) * 128),
                (short) ((newLoc.getZ() * 32 - oldLoc.getZ() * 32) * 128),
                false
        );
        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
    }


    @Override
    public void removeEntity(Object stand){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(NMSUtil.getEntityID((Entity) stand));
        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
    }


    @Override
    public void rotateStand(Object stand, int angle){
        Entity entity = (Entity) stand;

        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                NMSUtil.getEntityID(entity),
                (short) 0,
                (short) 0,
                (short) 0,
                (byte) (angle * 256 / 360F),
                (byte) (angle * 256 / 360F),
                true);
        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
    }


    @Override
    public void spawnArmorStandToPlayer(Player player, Object standRaw, org.bukkit.inventory.ItemStack itemHead, Object packetRaw){
        Entity stand = (Entity) standRaw;
        Packet packetSpawn = new PacketPlayOutSpawnEntity(stand);
        NMSUtil.sendPacket(player, packetSpawn);

        Packet packetHead = new PacketPlayOutEntityEquipment(NMSUtil.getEntityID(stand), Arrays.asList(new com.mojang.datafixers.util.Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemHead))));
        NMSUtil.sendPacket(player, packetHead);

        //ОТПРАВКА ПАКЕТА НА ИНВИЗ И УДАЛЕНИЕ ПЛОЩАДКИ
        NMSUtil.sendPacket(player, (PacketPlayOutEntityMetadata) packetRaw);
    }

}
