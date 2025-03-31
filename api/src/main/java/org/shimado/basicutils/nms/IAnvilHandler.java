package org.shimado.basicutils.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IAnvilHandler {

    Inventory openAnvil(Player player, String anvilTitle);
    String getAnvilText(Player player);

}
