package org.shimado.basicutils.v1_15_R1;

import net.minecraft.server.v1_15_R1.ContainerAnvil;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.shimado.basicutils.nms.IAnvilHandler;
import org.shimado.basicutils.utils.ColorUtil;
import org.shimado.basicutils.utils.NumberUtil;

import java.util.HashMap;
import java.util.Map;

public class AnvilHandler implements IAnvilHandler {

    private Map<Player, ContainerAnvil> OPENED_ANVILS = new HashMap<>();


    @Override
    public Inventory openAnvil(Player player, String anvilTitle){
        EntityPlayer entityPlayer = NMSUtil.getEntityPlayer(player);
        int id = NumberUtil.randomInt(0, 10);
        PacketPlayOutOpenWindow packetPlayOutOpenWindow = new PacketPlayOutOpenWindow(id, Containers.ANVIL, CraftChatMessage.fromStringOrNull(ColorUtil.getColor(anvilTitle)));
        entityPlayer.playerConnection.sendPacket(packetPlayOutOpenWindow);

        ContainerAnvil anvilContainer = new ContainerAnvil(id, entityPlayer.inventory);
        anvilContainer.setTitle(CraftChatMessage.fromStringOrNull(ColorUtil.getColor(anvilTitle)));
        anvilContainer.maximumRepairCost = 0;
        entityPlayer.activeContainer = anvilContainer;
        entityPlayer.syncInventory();

        OPENED_ANVILS.put(player, anvilContainer);

        Inventory inv = anvilContainer.getBukkitView().getTopInventory();

        return inv;
    }


    @Override
    public String getAnvilText(Player player){
        if(OPENED_ANVILS.containsKey(player)){
            return ((ContainerAnvil) OPENED_ANVILS.get(player)).renameText;
        }
        return null;
    }

}
