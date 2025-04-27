package org.shimado.basicutils.v1_14_R1;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.shimado.basicutils.nms.IInvHandler;
import org.shimado.basicutils.utils.ColorUtil;

import java.util.List;
import java.util.stream.IntStream;

public class InvHandler implements IInvHandler {

    public boolean checkIfClickInventory(InventoryView inventoryView, String title, int slot, int lines){
        return inventoryView != null && title != null && inventoryView.getTitle() != null
                && inventoryView.getTopInventory() != null
                && inventoryView.getTopInventory().getSize() == lines
                && inventoryView.getTitle().equals(ColorUtil.getColor(title))
                && IntStream.range(0, lines).anyMatch(it -> slot == it);
    }


    public boolean checkIfPluginInventory(InventoryView inventoryView, List<String> listOfTitles){
        return inventoryView != null && inventoryView.getTitle() != null
                && inventoryView.getTopInventory() != null
                && listOfTitles.contains(ColorUtil.getColor(inventoryView.getTitle()));
    }


    public Inventory getTopInventory(InventoryInteractEvent e){
        return e.getView().getTopInventory();
    }


    public String getTitle(InventoryInteractEvent e){
        if(e.getView() == null) return "";
        return e.getView().getTitle();
    }

}
