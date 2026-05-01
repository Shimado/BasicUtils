package org.shimado.basicutils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.utils.ColorUtil;
import org.shimado.basicutils.utils.CreateItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryUtil {

    public static boolean addItemToInventory(@NotNull Player player, @Nullable ItemStack itemToGive){
        if(itemToGive == null || itemToGive.getAmount() <= 0) return true;
        return addItemsToInventory(player, List.of(itemToGive));
    }


    public static boolean addItemsToInventory(@NotNull Player player, @NotNull List<ItemStack> itemsToGive){
        if(itemsToGive.isEmpty()) return true;

        Inventory oldInv = player.getInventory();
        ItemStack[] oldStorage = oldInv.getStorageContents();

        Inventory newInv = Bukkit.createInventory(null, 36, UUID.randomUUID().toString());
        ItemStack[] newStorage = new ItemStack[oldStorage.length];

        for (int slot = 0; slot < oldStorage.length; slot++) {
            if(oldStorage[slot] != null){
                newStorage[slot] = oldStorage[slot].clone();
            }
        }
        newInv.setStorageContents(newStorage);


        // Клонируем массив только один раз
        ItemStack[] clonedItems = itemsToGive.stream().map(ItemStack::clone).toArray(ItemStack[]::new);

        if (!newInv.addItem(clonedItems).isEmpty()) return false;

        return oldInv.addItem(clonedItems).isEmpty();
    }


    public static void addItemToInventoryOrDrop(@NotNull Player player, @Nullable ItemStack itemToGive){
        if(itemToGive == null || itemToGive.getAmount() <= 0) return;
        addItemsToInventoryOrDrop(player, List.of(itemToGive));
    }


    public static void addItemsToInventoryOrDrop(@NotNull Player player, @NotNull List<ItemStack> itemsToGive){
        if(itemsToGive.isEmpty()) return;
        player.getInventory().addItem(itemsToGive.toArray(ItemStack[]::new)).values()
                .forEach(item -> player.getWorld().dropItemNaturally(player.getLocation().clone().add(0, 1, 0), item));
    }


    public static boolean removeSameItemsFromInv(@Nullable ItemStack item, @NotNull Inventory inv){
        if(item == null || item.getAmount() <= 0) return false;

        int total = item.getAmount();
        Material material = item.getType();
        ItemStack chipClone = CreateItemUtil.getCloneAmount1(item);

        List<ItemStack> markedForRemoval = new ArrayList<>();

        for(ItemStack itemStack : inv.getContents()) {
            if(itemStack == null || !material.equals(itemStack.getType()) || !chipClone.equals(CreateItemUtil.getCloneAmount1(itemStack))) continue;

            if(itemStack.getAmount() >= total){
                itemStack.setAmount(itemStack.getAmount() - total);
                markedForRemoval.forEach(toDel -> toDel.setAmount(0));
                return true;
            }

            total -= itemStack.getAmount();
            markedForRemoval.add(itemStack);

            if(total <= 0) {
                markedForRemoval.forEach(toDel -> toDel.setAmount(0));
                return true;
            }
        }

        return false;
    }


    public static void setItemToGUI(@NotNull Inventory inv, int slot, @NotNull Object material, @NotNull String name, @NotNull List<String> lore, boolean enchant, int customModelData, boolean hideName){
        if(slot >= 0 && slot < inv.getSize()){
            inv.setItem(slot, CreateItemUtil.create(material, name, lore, enchant, customModelData, hideName));
        }
    }

    public static void setItemToGUI(@NotNull Inventory inv, @NotNull List<Integer> slots, @NotNull Object material, @NotNull String name, @NotNull List<String> lore, boolean enchant, int customModelData, boolean hideName){
        slots.forEach(s -> setItemToGUI(inv, s, material, name, lore, enchant, customModelData, hideName));
    }


    public static void setItemToGUI(@NotNull Inventory inv, int slot, @Nullable ItemStack item){
        if(slot >= 0 && slot < inv.getSize()){
            inv.setItem(slot, item);
        }
    }

    public static void setItemToGUI(@NotNull Inventory inv, @NotNull List<Integer> slots, @Nullable ItemStack item){
        slots.forEach(s -> setItemToGUI(inv, s, item));
    }


    public static boolean isHaveFreeSlotsForItem(@NotNull Player player, @NotNull ItemStack item){
        if (item == null || item.getType().equals(Material.AIR)) return false;

        int left = item.getAmount();
        Material material = item.getType();
        ItemStack chipClone = CreateItemUtil.getCloneAmount1(item);
        int maxStack = item.getMaxStackSize();

        for (ItemStack it : player.getInventory().getStorageContents()) {
            if (it == null || it.getType().equals(Material.AIR)) {
                left -= maxStack;
            } else if (material.equals(it.getType()) && chipClone.equals(CreateItemUtil.getCloneAmount1(it))) {
                left -= (maxStack - it.getAmount());
            }
            if(left <= 0) return true;
        }

        return false;
    }


    @FunctionalInterface
    public interface InventoryLogic{
        void run(@NotNull Inventory inv);
    }


    public static void createGUI(@NotNull Player player, @NotNull InvSessionInstance session, int linesGUISize, @NotNull String titleGUI, @NotNull Object emptySlotsMap, @NotNull InventoryLogic logic){
        session.setChangingPage(true);
        Inventory inv = Bukkit.createInventory(null, 9 * linesGUISize, ColorUtil.getColor(titleGUI));
        session.setInv(inv);
        ((Map<Integer, Object>) emptySlotsMap).forEach((slot, materials) -> {
            if(materials instanceof ItemStack){
                setItemToGUI(inv, slot, (ItemStack) materials);
            }
            else{
                List<Object> list = (List<Object>) materials;
                setItemToGUI(inv, slot, list.get(0), " ", new ArrayList<>(), false, (int) list.get(1), true);
            }
        });
        logic.run(inv);
        player.openInventory(inv);
        session.setChangingPage(false);
    }

}
