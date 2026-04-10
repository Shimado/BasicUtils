package org.shimado.basicutils.v26_1_R1;

import com.mojang.authlib.GameProfile;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
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

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VersionInstance implements IVersionControl{

    private final Color transparentColor = new Color(255, 255, 255, 0);

    @Override
    public ItemStack createItemWithTag(ItemStack item, String tag, String value) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(BasicUtils.getPlugin(), tag), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }


    @Override
    public ItemStack createItemWithTags(ItemStack item, Map<String, String> map) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
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
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return "";
        NamespacedKey key = new NamespacedKey(BasicUtils.getPlugin(), tag.toLowerCase().replace(" ", ""));
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
    public void createFirework(List<Player> players, Location loc, ItemStack fireworkItem) {
        try {
            Constructor<?> constructor = NMSUtil.FireworkRocketEntity.getConstructor(NMSUtil.Level, double.class, double.class, double.class, NMSUtil.ItemStack);
            Object firework = constructor.newInstance(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), NMSUtil.getItem(fireworkItem));

            Object spawnPacket = NMSUtil.getPacketPlayOutSpawnEntity(firework, loc);
            for(Player p : players){
                NMSUtil.sendPacket(p, spawnPacket);
            }

            // lifetime = 0
            NMSUtil.FireworkRocketEntity.getField("lifetime").setInt(firework, 0);

            Object watcher = NMSUtil.getDataWatcher(firework);
            Method packDirty = watcher.getClass().getMethod("packDirty");

            Object metaPacket = NMSUtil.ClientboundSetEntityDataPacket.getConstructor(int.class, List.class).newInstance(NMSUtil.getEntityID(firework), packDirty.invoke(watcher));
            Object statusPacket = NMSUtil.ClientboundEntityEventPacket.getConstructor(NMSUtil.Entity, byte.class).newInstance(firework, (byte) 17);
            Object destroyPacket = NMSUtil.ClientboundRemoveEntitiesPacket.getConstructor(int[].class).newInstance((Object) new int[]{NMSUtil.getEntityID(firework)});

            for(Player p : players){
                NMSUtil.sendPacket(p, metaPacket);
                NMSUtil.sendPacket(p, statusPacket);
                NMSUtil.sendPacket(p, destroyPacket);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public GameProfile getGameProfile(Player player){
        try {
            return (GameProfile) NMSUtil.getProfile.invoke(NMSUtil.CraftPlayer.cast(player));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public BlockFace getFacing(Player player){
        return player.getFacing();
    }


    @Override
    public void spawnParticleNote(World world, double x, double y, double z, float color) {
        try {
            Object note = NMSUtil.ParticleTypes.getField("NOTE").get(null);

            Object packet = NMSUtil.ClientboundLevelParticlesPacket.getConstructor(
                    NMSUtil.ParticleOptions,
                    boolean.class, boolean.class,
                    double.class, double.class, double.class,
                    float.class, float.class, float.class,
                    float.class, int.class
            ).newInstance(note, false, true, x, y, z, color, 0f, 0f, 1f, 0);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().equals(world)) {
                    NMSUtil.sendPacket(p, packet);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void spawnParticleDust(World world, double x, double y, double z, float r, float g, float b) {
        try {
            int color = ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);

            Object dust = NMSUtil.DustParticleOptions.getConstructor(int.class, float.class).newInstance(color, 1f);

            Object packet = NMSUtil.ClientboundLevelParticlesPacket.getConstructor(
                    NMSUtil.ParticleOptions,
                    boolean.class, boolean.class,
                    double.class, double.class, double.class,
                    float.class, float.class, float.class,
                    float.class, int.class
            ).newInstance(dust, false, true, x, y, z, 0f, 0f, 0f, 1f, 1);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().equals(world)) {
                    NMSUtil.sendPacket(p, packet);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Pair spawnArmorStandByLocation(Location loc, ItemStack itemHead, boolean isSmall, float angleX, float angleY, float angleZ) {
        try {
            // === СОЗДАНИЕ ARMOR STAND ===
            Constructor<?> armorStandConstructor = NMSUtil.ArmorStand.getConstructor(NMSUtil.Level, double.class, double.class, double.class);

            Object stand = armorStandConstructor.newInstance(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ());

            // === SPAWN PACKET ===
            Object spawnPacket = NMSUtil.getPacketPlayOutSpawnEntity(stand);
            for(Player p : Bukkit.getOnlinePlayers()){
                NMSUtil.sendPacket(p, spawnPacket);
            }

            // === ITEM В ГОЛОВУ ===
            Constructor<?> pairConstructor = NMSUtil.Pair.getConstructor(Object.class, Object.class);
            Object pair = pairConstructor.newInstance(NMSUtil.EquipmentSlot.getField("HEAD").get(null), NMSUtil.getItem(itemHead));

            Object packetHead = NMSUtil.ClientboundSetEquipmentPacket
                    .getConstructor(int.class, List.class)
                    .newInstance(NMSUtil.getEntityID(stand), Arrays.asList(pair));

            for(Player p : Bukkit.getOnlinePlayers()){
                NMSUtil.sendPacket(p, packetHead);
            }

            // === WATCHER ===
            Object watcher = NMSUtil.getDataWatcher(stand);

            Method setMethod = watcher.getClass().getMethod("set", NMSUtil.EntityDataAccessor, Object.class);

            // === ПОЛУЧЕНИЕ DATA ACCESSORS ===
            Object DATA_CLIENT_FLAGS = getStaticField(NMSUtil.ArmorStand, "DATA_CLIENT_FLAGS");
            Object DATA_HEAD_POSE = getStaticField(NMSUtil.ArmorStand, "DATA_HEAD_POSE");
            Object DATA_SHARED_FLAGS = getStaticField(NMSUtil.Entity, "DATA_SHARED_FLAGS_ID");

            // === УДАЛЕНИЕ ПЛИТЫ + SMALL ===
            byte flags = 0x08;
            if (isSmall) flags |= 0x01;

            setMethod.invoke(watcher, DATA_CLIENT_FLAGS, flags);

            // === УГОЛ ГОЛОВЫ ===
            Constructor<?> rotationsCtor = NMSUtil.Rotations.getConstructor(float.class, float.class, float.class);
            Object rotations = rotationsCtor.newInstance(angleX, angleY, angleZ);

            setMethod.invoke(watcher, DATA_HEAD_POSE, rotations);

            // === INVISIBLE ===
            setMethod.invoke(watcher, DATA_SHARED_FLAGS, (byte) 0x20);

            // === META PACKET ===
            Method packDirty = watcher.getClass().getMethod("packDirty");

            Object metaPacket = NMSUtil.ClientboundSetEntityDataPacket
                    .getConstructor(int.class, List.class)
                    .newInstance(NMSUtil.getEntityID(stand), packDirty.invoke(watcher));

            for(Player p : Bukkit.getOnlinePlayers()){
                NMSUtil.sendPacket(p, metaPacket);
            }

            return new Pair(stand, metaPacket);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Object getStaticField(Class<?> clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(null);
    }


    @Override
    public void moveStand(Object entity, Location newLoc, Location oldLoc) {
        try {
            Constructor<?> ctor = NMSUtil.ClientboundMoveEntityPacket$Pos.getConstructor(int.class, short.class, short.class, short.class, boolean.class);

            short dx = (short) ((newLoc.getX() * 32 - oldLoc.getX() * 32) * 128);
            short dy = (short) (((newLoc.getY() + 1.7) * 32 - (oldLoc.getY() + 1.7) * 32) * 128);
            short dz = (short) ((newLoc.getZ() * 32 - oldLoc.getZ() * 32) * 128);

            Object packet = ctor.newInstance(
                    NMSUtil.getEntityID(entity),
                    dx, dy, dz,
                    false
            );

            for(Player p : Bukkit.getOnlinePlayers()){
                NMSUtil.sendPacket(p, packet);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void removeEntity(Object stand) {
        try {
            Constructor<?> ctor = NMSUtil.ClientboundRemoveEntitiesPacket.getConstructor(int[].class);
            Object packet = ctor.newInstance((Object) new int[]{NMSUtil.getEntityID(stand)});
            for(Player p : Bukkit.getOnlinePlayers()){
                NMSUtil.sendPacket(p, packet);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void rotateStand(Object stand, int angle) {
        try {
            Constructor<?> ctor = NMSUtil.ClientboundMoveEntityPacket$PosRot.getConstructor(
                    int.class, short.class, short.class, short.class,
                    byte.class, byte.class, boolean.class
            );

            byte rot = (byte) (angle * 256 / 360F);

            Object packet = ctor.newInstance(
                    NMSUtil.getEntityID(stand),
                    (short) 0, (short) 0, (short) 0,
                    rot, rot,
                    true
            );

            for(Player p : Bukkit.getOnlinePlayers()){
                NMSUtil.sendPacket(p, packet);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void spawnArmorStandToPlayer(Player player, Object stand, ItemStack itemHead, Object packetRaw) {
        try {
            Object spawnPacket = NMSUtil.getPacketPlayOutSpawnEntity(stand);
            NMSUtil.sendPacket(player, spawnPacket);

            Method asNMSCopy = NMSUtil.CraftItemStack.getMethod("asNMSCopy", ItemStack.class);
            Object nmsItem = asNMSCopy.invoke(null, itemHead);

            Object headSlot = NMSUtil.EquipmentSlot.getField("HEAD").get(null);

            Constructor<?> pairCtor = NMSUtil.Pair.getConstructor(Object.class, Object.class);
            Object pair = pairCtor.newInstance(headSlot, nmsItem);

            Object packetHead = NMSUtil.ClientboundSetEquipmentPacket
                    .getConstructor(int.class, List.class)
                    .newInstance(NMSUtil.getEntityID(stand), Arrays.asList(pair));

            NMSUtil.sendPacket(player, packetHead);

            // meta packet
            NMSUtil.sendPacket(player, packetRaw);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object createItem(List<Player> players, Location loc, ItemStack itemToDrop,
                             double vectorX, double vectorY, double vectorZ) {
        try {
            Constructor<?> ctor = NMSUtil.ItemEntity.getConstructor(NMSUtil.Level, double.class, double.class, double.class, NMSUtil.ItemStack, double.class, double.class, double.class);

            Object item = ctor.newInstance(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), NMSUtil.getItem(itemToDrop), vectorX, vectorY, vectorZ);

            Object spawnPacket = NMSUtil.getPacketPlayOutSpawnEntity(item, loc);

            Object watcher = NMSUtil.getDataWatcher(item);
            Method packDirty = watcher.getClass().getMethod("packDirty");

            Object metaPacket = NMSUtil.ClientboundSetEntityDataPacket
                    .getConstructor(int.class, List.class)
                    .newInstance(NMSUtil.getEntityID(item), packDirty.invoke(watcher));

            for (Player p : players) {
                NMSUtil.sendPacket(p, spawnPacket);
                NMSUtil.sendPacket(p, metaPacket);
            }

            return item;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


//public class VersionInstance implements IVersionControl {
//
//    private final Color transparentColor = new Color(255, 255, 255, 0);
//
//
//    @Override
//    public ItemStack createItemWithTag(ItemStack item, String tag, String value) {
//        ItemMeta meta = item.getItemMeta();
//        if(meta == null) return item;
//        PersistentDataContainer container = meta.getPersistentDataContainer();
//        container.set(new NamespacedKey(BasicUtils.getPlugin(), tag), PersistentDataType.STRING, value);
//        item.setItemMeta(meta);
//        return item;
//    }
//
//
//    @Override
//    public ItemStack createItemWithTags(ItemStack item, Map<String, String> map) {
//        ItemMeta meta = item.getItemMeta();
//        if(meta == null) return item;
//        PersistentDataContainer container = meta.getPersistentDataContainer();
//        for(Map.Entry<String, String> a : map.entrySet()){
//            container.set(new NamespacedKey(BasicUtils.getPlugin(), a.getKey()), PersistentDataType.STRING, a.getValue());
//        }
//        item.setItemMeta(meta);
//        return item;
//    }
//
//
//    @Override
//    public String getTag(ItemStack item, String tag){
//        if(item == null || item.getType().equals(Material.AIR)) return "";
//        ItemMeta meta = item.getItemMeta();
//        if(meta == null) return "";
//        NamespacedKey key = new NamespacedKey(BasicUtils.getPlugin(), tag.toLowerCase().replace(" ", ""));
//        return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
//    }
//
//
//    @Override
//    public void moveHeadToBottom(Player player){
//        Location loc = player.getLocation().clone();
//        loc.setPitch(90);
//        player.teleport(loc);
//    }
//
//
//    @Override
//    public void setPixelColor(MapCanvas mapCanvas, int x, int y, Color color){
//        mapCanvas.setPixelColor(x, y, color);
//    }
//
//
//    @Override
//    public Color getBasePixelColor(MapCanvas mapCanvas, int x, int y){
//        try {
//            byte color = mapCanvas.getBasePixel(x, y);
//            return MapPalette.getColor(color);
//        }catch (Exception e){
//            return transparentColor;
//        }
//    }
//
//    @Override
//    public void createFirework(Player player, Location loc, ItemStack fireworkItem) {
//        FireworkRocketEntity firework = new FireworkRocketEntity(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(fireworkItem));
//        NMSUtil.sendPacket(player, NMSUtil.getPacketPlayOutSpawnEntity(firework, loc));
//        firework.lifetime = 0;
//        NMSUtil.sendPacket(player, new ClientboundSetEntityDataPacket(NMSUtil.getEntityID(firework), NMSUtil.getDataWatcher(firework).packDirty()));
//        NMSUtil.sendPacket(player, new ClientboundEntityEventPacket(firework, (byte) 17));
//        NMSUtil.sendPacket(player, new ClientboundRemoveEntitiesPacket(NMSUtil.getEntityID(firework)));
//    }
//
//
//    @Override
//    public GameProfile getGameProfile(Player player){
//        return ((CraftPlayer) player).getProfile();
//    }
//
//
//    @Override
//    public BlockFace getFacing(Player player){
//        return player.getFacing();
//    }
//
//
//    @Override
//    public void spawnParticleNote(World world, double x, double y, double z, float color) {
//        Packet packet = new ClientboundLevelParticlesPacket(ParticleTypes.NOTE, false, true, x, y, z, color, 0f, 0f, 1, 0);
//        Bukkit.getOnlinePlayers().forEach(p -> {
//            if(world.getUID().equals(p.getWorld().getUID())){
//                NMSUtil.sendPacket(p, packet);
//            }
//        });
//    }
//
//
//    @Override
//    public void spawnParticleDust(World world, double x, double y, double z, float r, float g, float b) {
//        int R = (int) (r * 255);
//        int G = (int) (g * 255);
//        int B = (int) (b * 255);
//        Packet packet = new ClientboundLevelParticlesPacket(new DustParticleOptions((R << 16) | (G << 8) | B, 1f), false, true, x, y, z, 0f, 0f, 0f, 1, 1);
//        Bukkit.getOnlinePlayers().forEach(p -> {
//            if(world.getUID().equals(p.getWorld().getUID())){
//                NMSUtil.sendPacket(p, packet);
//            }
//        });
//    }
//
//
//    @Override
//    public Pair spawnArmorStandByLocation(Location loc, ItemStack itemHead, boolean isSmall, float angleX, float angleY, float angleZ){
//
//        //СОЗДАНИЕ САМОГО АРМОР СТЕНДА
//        ArmorStand stand = new ArmorStand(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ());
//
//        //ОТПРАВКА ПАКЕТА НА СОЗДАНИЕ АРМОР СТЕНДА
//        Packet packet = NMSUtil.getPacketPlayOutSpawnEntity(stand);
//        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
//
//        //ОТПРАВКА ПАКЕТА НА НАДЕВАНИЕ ГОЛОВЫ НА АРМОРСТЕНД;
//        Packet packetHead = new ClientboundSetEquipmentPacket(NMSUtil.getEntityID(stand), Arrays.asList(new com.mojang.datafixers.util.Pair(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(itemHead))));
//        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packetHead));
//
//        //ИНВИЗ И УДАЛЕНИЕ ПЛОЩАДКИ
//        try {
//            SynchedEntityData watcher = NMSUtil.getDataWatcher(stand);
//
//            //УДАЛЕНИЕ ПЛОЩАКИ У АРМОРСТЕНДА
//            Field plate = ArmorStand.class.getDeclaredField("DATA_CLIENT_FLAGS");
//            plate.setAccessible(true);
//            NMSUtil.setWatcher(watcher, (EntityDataAccessor<Byte>) plate.get(stand), (byte) 0x08);
//
//            //МАЛЕНЬКИЙ
//            if(isSmall){
//                Field small = ArmorStand.class.getDeclaredField("DATA_CLIENT_FLAGS");
//                small.setAccessible(true);
//                NMSUtil.setWatcher(watcher, (EntityDataAccessor<Byte>) plate.get(stand), (byte) 0x01);
//            }
//
//            //УГОЛ ГОЛОВЫ
//            Field angle = ArmorStand.class.getDeclaredField("DATA_HEAD_POSE");
//            angle.setAccessible(true);
//            watcher.set((EntityDataAccessor<Rotations>) angle.get(stand), new Rotations(angleX, angleY, angleZ));
//
//            //ИНВИЗ ДЛЯ АРМОРСТЕНДА
//            Field invis = Entity.class.getDeclaredField("DATA_SHARED_FLAGS_ID");
//            invis.setAccessible(true);
//            NMSUtil.setWatcher(watcher, (EntityDataAccessor<Byte>) invis.get(stand), (byte) 0x20);
//
//            //ОТПРАВКА ПАКЕТА НА ИНВИЗ И УДАЛЕНИЕ ПЛОЩАДКИ
//            ClientboundSetEntityDataPacket packetMeta = new ClientboundSetEntityDataPacket(NMSUtil.getEntityID(stand), watcher.packDirty());
//            Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packetMeta));
//
//            return new Pair(stand, packetMeta);
//
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        return new Pair(stand, null);
//
//    }
//
//
//    @Override
//    public void moveStand(Object entity, Location newLoc, Location oldLoc){
//        ClientboundMoveEntityPacket.Pos packet = new ClientboundMoveEntityPacket.Pos(
//                NMSUtil.getEntityID((Entity) entity),
//                (short) ((newLoc.getX() * 32 - oldLoc.getX() * 32) * 128),
//                (short) (((newLoc.getY() + 1.7) * 32 - (oldLoc.getY() + 1.7) * 32) * 128),
//                (short) ((newLoc.getZ() * 32 - oldLoc.getZ() * 32) * 128),
//                false
//        );
//        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
//    }
//
//
//    @Override
//    public void removeEntity(Object stand){
//        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(NMSUtil.getEntityID((Entity) stand));
//        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
//    }
//
//
//    @Override
//    public void rotateStand(Object stand, int angle){
//        Entity entity = (Entity) stand;
//
//        ClientboundMoveEntityPacket.PosRot packet = new ClientboundMoveEntityPacket.PosRot(
//                NMSUtil.getEntityID(entity),
//                (short) 0,
//                (short) 0,
//                (short) 0,
//                (byte) (angle * 256 / 360F),
//                (byte) (angle * 256 / 360F),
//                true);
//        Bukkit.getOnlinePlayers().forEach(p -> NMSUtil.sendPacket(p, packet));
//    }
//
//
//    @Override
//    public void spawnArmorStandToPlayer(Player player, Object standRaw, ItemStack itemHead, Object packetRaw){
//        Entity stand = (Entity) standRaw;
//        Packet packetSpawn = NMSUtil.getPacketPlayOutSpawnEntity(stand);
//        NMSUtil.sendPacket(player, packetSpawn);
//
//        Packet packetHead = new ClientboundSetEquipmentPacket(NMSUtil.getEntityID(stand), Arrays.asList(new com.mojang.datafixers.util.Pair(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(itemHead))));
//        NMSUtil.sendPacket(player, packetHead);
//
//        //ОТПРАВКА ПАКЕТА НА ИНВИЗ И УДАЛЕНИЕ ПЛОЩАДКИ
//        NMSUtil.sendPacket(player, (ClientboundSetEntityDataPacket) packetRaw);
//    }
//
//
//    @Override
//    public Object createItem(List<Player> players, Location loc, ItemStack itemToDrop, double vectorX, double vectorY, double vectorZ) {
//        ItemEntity item = new ItemEntity(NMSUtil.getWorld(loc), loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(itemToDrop), vectorX, vectorY, vectorZ);
//        Packet spawnPacket = NMSUtil.getPacketPlayOutSpawnEntity(item, loc);
//        Packet metaPacket = new ClientboundSetEntityDataPacket(NMSUtil.getEntityID(item), NMSUtil.getDataWatcher(item).packDirty());
//        for(Player p : players){
//            NMSUtil.sendPacket(p, spawnPacket);
//            NMSUtil.sendPacket(p, metaPacket);
//        }
//        return item;
//    }
//
//}
