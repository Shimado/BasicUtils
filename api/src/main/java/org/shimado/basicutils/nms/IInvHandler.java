package org.shimado.basicutils.nms;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IInvHandler {

    boolean checkIfClickInventory(@Nonnull InventoryView invView, @Nullable String title, int slot, int lines);

    boolean checkIfPluginInventory(@Nonnull InventoryView invView, @Nonnull List<String> listOfTitles);

    @Nullable
    Inventory getTopInventory(@Nonnull InventoryInteractEvent e);

    @Nonnull
    String getTitle(@Nonnull InventoryInteractEvent e);

}
