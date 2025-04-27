package org.shimado.basicutils.v1_12_R1;

import net.minecraft.server.v1_12_R1.ContainerAnvil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.shimado.basicutils.nms.IAnvilHandler;

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
