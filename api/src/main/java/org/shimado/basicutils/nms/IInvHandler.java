package org.shimado.basicutils.nms;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IInvHandler {

    boolean checkIfClickInventory(@NotNull InventoryView invView, @Nullable String title, int slot, int lines);

    boolean checkIfPluginInventory(@NotNull InventoryView invView, @NotNull List<String> listOfTitles);

    @NotNull
    Inventory getTopInventory(@NotNull InventoryInteractEvent e);

    @NotNull
    Inventory getBottomInventory(@NotNull InventoryInteractEvent e);

    @Nullable
    ItemStack getItem(@NotNull InventoryInteractEvent e, int slot);

    void setItem(@NotNull InventoryInteractEvent e, int slot, @Nullable ItemStack item);

    @NotNull
    String getTitle(@NotNull InventoryInteractEvent e);

}
