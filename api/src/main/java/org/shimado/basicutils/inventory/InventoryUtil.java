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

public class InventoryUtil {

    public static boolean addItemToInventory(Player player, ItemStack item){
        for(ItemStack itemStack : player.getInventory().getStorageContents()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)){
                player.getInventory().addItem(item);
                return true;
            }
        }
        return false;
    }


    public static boolean addItemsToInventory(Player player, List<ItemStack> items){
        int howMany = 0;
        for(ItemStack itemStack : player.getInventory().getStorageContents()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)){
                howMany++;
                if(howMany >= items.size()){
                    items.forEach(it -> player.getInventory().addItem(it));
                    return true;
                }
            }
        }
        return false;
    }


    public static void addItemToInventoryOrDrop(Player player, ItemStack item){
        if(item == null || item.getType().equals(Material.AIR)) return;
        for(ItemStack itemStack : player.getInventory().getStorageContents()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)){
                player.getInventory().addItem(item);
                return;
            }
        }
        player.getWorld().dropItemNaturally(player.getLocation().clone().add(0, 1, 0), item);
    }


    public static void addItemsToInventoryOrDrop(Player player, List<ItemStack> items){
        if(items.isEmpty()) return;
        List<ItemStack> itemsToGive = new ArrayList<>(items);
        for(ItemStack itemStack : player.getInventory().getStorageContents()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)){
                player.getInventory().addItem(itemsToGive.get(0));
                itemsToGive.remove(0);
                if(itemsToGive.isEmpty()) return;
            }
        }
        itemsToGive.forEach(it -> player.getWorld().dropItemNaturally(player.getLocation().clone().add(0, 1, 0), it));
    }


    public static boolean removeSameItemsFromInv(@Nullable ItemStack item, @Nonnull Inventory inv){
        if(item == null || item.getAmount() <= 0) return false;

        int total = item.getAmount();

        List<ItemStack> itemsToRemove = new ArrayList<>();

        for(ItemStack it : inv.getContents()) {
            if(!item.getType().equals(it.getType())) continue;
            if(!CreateItemUtil.isSameItems(item, it, null)) continue;

            if(it.getAmount() >= total){
                it.setAmount(it.getAmount() - total);
                itemsToRemove.forEach(toDel -> toDel.setAmount(0));
                return true;
            }
            else if(it.getAmount() < total){
                total -= it.getAmount();
                itemsToRemove.add(it);
                if(total <= 0) {
                    itemsToRemove.forEach(toDel -> toDel.setAmount(0));
                    return true;
                }
            }
        }

        return false;
    }


    public static void setItemToGUI(Inventory inv, int slot, Object material, String name, List<String> lore, boolean enchant, int customModelData, boolean hideName){
        if(slot >= 0 && slot < inv.getSize()){
            inv.setItem(slot, CreateItemUtil.create(material, name, lore, enchant, customModelData, hideName));
        }
    }

    public static void setItemToGUI(Inventory inv, List<Integer> slots, Object material, String name, List<String> lore, boolean enchant, int customModelData, boolean hideName){
        slots.forEach(s -> setItemToGUI(inv, s, material, name, lore, enchant, customModelData, hideName));
    }


    public static void setItemToGUI(Inventory inv, int slot, ItemStack item){
        if(slot >= 0 && slot < inv.getSize()){
            inv.setItem(slot, item);
        }
    }

    public static void setItemToGUI(Inventory inv, List<Integer> slots, ItemStack item){
        slots.forEach(s -> setItemToGUI(inv, s, item));
    }


    public static boolean isHaveFreeSlotsForItem(Player player, ItemStack item){
        ItemStack chipClone = CreateItemUtil.getCloneAmount1(item);
        int amountLeft = item.getAmount();

        for(ItemStack it : player.getInventory().getStorageContents()){
            if(it == null){
                return true;
            }

            ItemStack itClone = CreateItemUtil.getCloneAmount1(it);

            if(chipClone.equals(itClone)){
                amountLeft -= it.getAmount();
                if(amountLeft <= 0) return true;
            }
        }

        return false;
    }


    @FunctionalInterface
    public interface InventoryLogic{
        void run(Inventory inv);
    }

    public static void createGUI(Player player, InvSessionInstance session, int sizeGUI, String titleGUI, Object emptySlotsMap, InventoryLogic logic){
        session.setChangingPage(true);
        Inventory inv = Bukkit.createInventory(null, 9 * sizeGUI, ColorUtil.getColor(titleGUI));
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
