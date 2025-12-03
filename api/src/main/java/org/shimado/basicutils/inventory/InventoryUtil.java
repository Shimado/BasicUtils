package org.shimado.basicutils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.shimado.basicutils.utils.ColorUtil;
import org.shimado.basicutils.utils.CreateItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryUtil {

    public static boolean addItemToInventory(@Nonnull Player player, @Nullable ItemStack itemToGive){
        if(itemToGive == null || itemToGive.getAmount() <= 0) return true;
        return addItemsToInventory(player, List.of(itemToGive));
    }


    public static boolean addItemsToInventory(@Nonnull Player player, @Nonnull List<ItemStack> itemsToGive){
        if(itemsToGive.isEmpty()) return true;

        Inventory oldInv = player.getInventory();
        ItemStack[] oldStorage = oldInv.getStorageContents();

        Inventory newInv = Bukkit.createInventory(null, oldInv.getSize(), UUID.randomUUID().toString());
        ItemStack[] newStorage = new ItemStack[oldStorage.length];

        for (int slot = 0; slot < oldStorage.length; slot++) {
            newStorage[slot] = oldStorage[slot].clone();
        }
        newInv.setStorageContents(newStorage);


        if(newInv.addItem(itemsToGive.stream().map(ItemStack::clone).toArray(ItemStack[]::new)).isEmpty()){
            return oldInv.addItem(itemsToGive.stream().map(ItemStack::clone).toArray(ItemStack[]::new)).isEmpty();
        }

        return false;
    }


    public static void addItemToInventoryOrDrop(@Nonnull Player player, @Nullable ItemStack itemToGive){
        if(itemToGive == null || itemToGive.getAmount() <= 0) return;
        addItemsToInventoryOrDrop(player, List.of(itemToGive));
    }


    public static void addItemsToInventoryOrDrop(@Nonnull Player player, @Nonnull List<ItemStack> itemsToGive){
        if(itemsToGive.isEmpty()) return;
        player.getInventory().addItem(itemsToGive.toArray(ItemStack[]::new)).values()
                .forEach(item -> player.getWorld().dropItemNaturally(player.getLocation().clone().add(0, 1, 0), item));
    }


    public static boolean removeSameItemsFromInv(@Nullable ItemStack item, @Nonnull Inventory inv){
        if(item == null || item.getAmount() <= 0) return false;

        int total = item.getAmount();
        Material material = item.getType();
        ItemStack chipClone = CreateItemUtil.getCloneAmount1(item);

        List<ItemStack> itemsToRemove = new ArrayList<>();

        for(ItemStack itemStack : inv.getContents()) {
            if(!material.equals(itemStack.getType()) || !chipClone.equals(CreateItemUtil.getCloneAmount1(itemStack))) continue;

            if(itemStack.getAmount() >= total){
                itemStack.setAmount(itemStack.getAmount() - total);
                itemsToRemove.forEach(toDel -> toDel.setAmount(0));
                return true;
            }
            else if(itemStack.getAmount() < total){
                total -= itemStack.getAmount();
                itemsToRemove.add(itemStack);
                if(total <= 0) {
                    itemsToRemove.forEach(toDel -> toDel.setAmount(0));
                    return true;
                }
            }
        }

        return false;
    }


    public static void setItemToGUI(@Nonnull Inventory inv, int slot, @Nonnull Object material, @Nonnull String name, @Nonnull List<String> lore, boolean enchant, int customModelData, boolean hideName){
        if(slot >= 0 && slot < inv.getSize()){
            inv.setItem(slot, CreateItemUtil.create(material, name, lore, enchant, customModelData, hideName));
        }
    }

    public static void setItemToGUI(@Nonnull Inventory inv, @Nonnull List<Integer> slots, @Nonnull Object material, @Nonnull String name, @Nonnull List<String> lore, boolean enchant, int customModelData, boolean hideName){
        slots.forEach(s -> setItemToGUI(inv, s, material, name, lore, enchant, customModelData, hideName));
    }


    public static void setItemToGUI(@Nonnull Inventory inv, int slot, @Nullable ItemStack item){
        if(slot >= 0 && slot < inv.getSize()){
            inv.setItem(slot, item);
        }
    }

    public static void setItemToGUI(@Nonnull Inventory inv, @Nonnull List<Integer> slots, @Nullable ItemStack item){
        slots.forEach(s -> setItemToGUI(inv, s, item));
    }


    public static boolean isHaveFreeSlotsForItem(@Nonnull Player player, @Nonnull ItemStack item){
        if (item == null || item.getType() == Material.AIR) return false;

        int left = item.getAmount();
        Material material = item.getType();
        ItemStack chipClone = CreateItemUtil.getCloneAmount1(item);

        for (ItemStack it : player.getInventory().getStorageContents()) {

            if (it == null || it.getType() == Material.AIR) {
                left -= item.getMaxStackSize();
            } else if (material.equals(it.getType()) && chipClone.equals(CreateItemUtil.getCloneAmount1(it))) {
                left -= (it.getMaxStackSize() - it.getAmount());
            }

            if (left <= 0) return true;
        }

        return false;
    }


    @FunctionalInterface
    public interface InventoryLogic{
        void run(@Nonnull Inventory inv);
    }

    public static void createGUI(@Nonnull Player player, @Nonnull InvSessionInstance session, int linesGUISize, @Nonnull String titleGUI, @Nonnull Object emptySlotsMap, @Nonnull InventoryLogic logic){
        session.setChangingPage(true);
        Inventory inv = Bukkit.createInventory(null, 9 * linesGUISize, ColorUtil.getColor(titleGUI));
        session.setInv(inv);
        ((Map<Integer, Object>) emptySlotsMap).forEach((slot, materials) -> {
            if(materials instanceof ItemStack){
                setItemToGUI(inv, slot, (ItemStack) materials);
            }
            else{
                setItemToGUI(inv, slot, ((List<Object>) materials).get(0), " ", new ArrayList<>(), false, (int) ((List<Object>) materials).get(1), true);
            }
        });
        logic.run(inv);
        player.openInventory(inv);
        session.setChangingPage(false);
    }

}
