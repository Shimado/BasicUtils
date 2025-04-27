package org.shimado.basicutils.v1_13_R2;

import net.minecraft.server.v1_13_R2.ContainerAnvil;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;
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
        return null;
    }


    @Override
    public String getAnvilText(Player player){
        if(OPENED_ANVILS.containsKey(player)){
            return ((ContainerAnvil) OPENED_ANVILS.get(player)).renameText;
        }
        return null;
    }

}
