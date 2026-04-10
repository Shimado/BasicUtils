package org.shimado.basicutils.v26_1_R1;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.shimado.basicutils.nms.IAnvilHandler;
import org.shimado.basicutils.utils.ColorUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnvilHandler implements IAnvilHandler {

    private final Map<Player, Object> OPENED_ANVILS = new HashMap<>();


    @Override
    public Inventory openAnvil(Player player, String anvilTitle) {
        try {
            Object entityPlayer = NMSUtil.getEntityPlayer(player);

            Method nextContainerCounter = entityPlayer.getClass().getMethod("nextContainerCounter");
            int id = (int) nextContainerCounter.invoke(entityPlayer);

            Method nullToEmpty = NMSUtil.Component.getMethod("nullToEmpty", String.class);
            Object titleComponent = nullToEmpty.invoke(null, ColorUtil.getColor(anvilTitle));
            Object anvilType = NMSUtil.MenuType.getField("ANVIL").get(null);

            Constructor<?> packetConstructor = NMSUtil.ClientboundOpenScreenPacket.getConstructor(int.class, NMSUtil.MenuType, NMSUtil.Component);
            Object packet = packetConstructor.newInstance(id, anvilType, titleComponent);

            NMSUtil.sendPacket(player, packet);

            // ContainerLevelAccess
            Constructor<?> blockPosConstructor = NMSUtil.BlockPos.getConstructor(int.class, int.class, int.class);
            Object blockPos = blockPosConstructor.newInstance(0, 0, 0);

            Object worldHandle = NMSUtil.getWorld(player.getLocation());

            Method create = NMSUtil.ContainerLevelAccess.getMethod("create", NMSUtil.Level, NMSUtil.BlockPos);
            Object access = create.invoke(null, worldHandle, blockPos);

            // AnvilMenu
            Constructor<?> anvilConstructor = NMSUtil.AnvilMenu.getConstructor(
                    int.class,
                    NMSUtil.Inventory,
                    NMSUtil.ContainerLevelAccess
            );

            Method getInventory = entityPlayer.getClass().getMethod("getInventory");
            Object inventory = getInventory.invoke(entityPlayer);

            Object anvil = anvilConstructor.newInstance(id, inventory, access);

            // checkReachable = false
            Field checkReachable = anvil.getClass().getField("checkReachable");
            checkReachable.setBoolean(anvil, false);

            // setTitle
            Method setTitle = anvil.getClass().getMethod("setTitle", NMSUtil.Component);
            setTitle.invoke(anvil, titleComponent);

            // maximumRepairCost = 0
            Field maxCost = anvil.getClass().getField("maximumRepairCost");
            maxCost.setInt(anvil, 0);

            // containerMenu
            Field containerMenu = entityPlayer.getClass().getField("containerMenu");
            containerMenu.set(entityPlayer, anvil);

            Method initMenu = entityPlayer.getClass().getMethod("initMenu", NMSUtil.AbstractContainerMenu);
            initMenu.invoke(entityPlayer, anvil);

            OPENED_ANVILS.put(player, anvil);

            // Bukkit inventory
            Method getBukkitView = anvil.getClass().getMethod("getBukkitView");
            Object view = getBukkitView.invoke(anvil);

            Method getTopInventory = view.getClass().getMethod("getTopInventory");
            return (Inventory) getTopInventory.invoke(view);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getAnvilText(Player player) {
        try {
            if (OPENED_ANVILS.containsKey(player)) {
                Object anvil = OPENED_ANVILS.get(player);
                Field itemName = anvil.getClass().getField("itemName");
                return (String) itemName.get(anvil);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


//public class AnvilHandler implements IAnvilHandler {
//
//    private Map<Player, AnvilMenu> OPENED_ANVILS = new HashMap<>();
//
//
//    @Override
//    public Inventory openAnvil(Player player, String anvilTitle){
//        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
//        int id = entityPlayer.nextContainerCounter();
//        NMSUtil.sendPacket(player, new ClientboundOpenScreenPacket(id, MenuType.ANVIL, Component.nullToEmpty(ColorUtil.getColor(anvilTitle))));
//
//        AnvilMenu anvilContainer = new AnvilMenu(
//                id,
//                entityPlayer.getInventory(),
//                ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(0, 0, 0))
//        );
//        anvilContainer.checkReachable = false;
//        anvilContainer.setTitle(Component.literal(ColorUtil.getColor(anvilTitle)));
//        anvilContainer.maximumRepairCost = 0;
//        entityPlayer.containerMenu = anvilContainer;
//        entityPlayer.initMenu(anvilContainer);
//
//        OPENED_ANVILS.put(player, anvilContainer);
//
//        return anvilContainer.getBukkitView().getTopInventory();
//    }
//
//
//    @Override
//    public String getAnvilText(Player player){
//        if(OPENED_ANVILS.containsKey(player)){
//            return OPENED_ANVILS.get(player).itemName;
//        }
//        return null;
//    }
//
//}
