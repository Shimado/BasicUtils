package org.shimado.basicutils.v1_19_R3;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.shimado.basicutils.nms.IAnvilHandler;
import org.shimado.basicutils.utils.ColorUtil;
import org.shimado.basicutils.utils.NumberUtil;

import java.util.HashMap;
import java.util.Map;

public class AnvilHandler implements IAnvilHandler {

    private Map<Player, Container> OPENED_ANVILS = new HashMap<>();


    @Override
    public Inventory openAnvil(Player player, String anvilTitle){
        EntityPlayer entityPlayer = NMSUtil.getEntityPlayer(player);
        int id = NumberUtil.randomInt(0, 10);
        PacketPlayOutOpenWindow packetPlayOutOpenWindow = new PacketPlayOutOpenWindow(id, Containers.h, IChatBaseComponent.a(ColorUtil.getColor(anvilTitle)));
        entityPlayer.b.a(packetPlayOutOpenWindow);

        ContainerAnvil anvilContainer = new ContainerAnvil(id, entityPlayer.fJ());
        anvilContainer.setTitle(IChatBaseComponent.b(ColorUtil.getColor(anvilTitle)));
        anvilContainer.maximumRepairCost = 0;
        entityPlayer.bP = anvilContainer;
        entityPlayer.a(anvilContainer);

        OPENED_ANVILS.put(player, anvilContainer);

        Inventory inv = anvilContainer.getBukkitView().getTopInventory();

        return inv;
    }


    @Override
    public String getAnvilText(Player player){
        if(OPENED_ANVILS.containsKey(player)){
            return ((ContainerAnvil) OPENED_ANVILS.get(player)).v;
        }
        return null;
    }

}
