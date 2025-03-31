package org.shimado.basicutils.nms;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.List;

public interface IInvHandler {

    boolean checkIfClickInventory(InventoryView invView, String title, int slot, int lines);
    boolean checkIfPluginInventory(InventoryView invView, List<String> listOfTitles);
    Inventory getTopInventory(InventoryInteractEvent e);
    String getTitle(InventoryInteractEvent e);

}
