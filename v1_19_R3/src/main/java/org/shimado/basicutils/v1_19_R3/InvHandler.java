package org.shimado.basicutils.v1_19_R3;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
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
                && listOfTitles.stream().anyMatch(it -> inventoryView.getTitle().equals(ColorUtil.getColor(it)));
    }


    public Inventory getTopInventory(InventoryInteractEvent e){
        return e.getView().getTopInventory();
    }


    public Inventory getBottomInventory(InventoryInteractEvent e){
        return e.getView().getBottomInventory();
    }


    public ItemStack getItem(InventoryInteractEvent e, int slot){
        return e.getView().getItem(slot);
    }


    public void setItem(InventoryInteractEvent e, int slot, ItemStack item){
        e.getView().setItem(slot, item);
    }


    public String getTitle(InventoryInteractEvent e){
        if(e.getView() == null) return "";
        return e.getView().getTitle();
    }

}
