package org.shimado.basicutils.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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


    public static void setItemToGUI(Inventory inv, int slot, Object material, String name, List<String> lore, boolean enchant, int customModelData, boolean hideName){
        if(slot != -1){
            inv.setItem(slot, CreateItemUtil.create(material, name, lore, enchant, customModelData, hideName));
        }
    }

    public static void setItemToGUI(Inventory inv, List<Integer> slots, Object material, String name, List<String> lore, boolean enchant, int customModelData, boolean hideName){
        slots.forEach(s -> setItemToGUI(inv, s, material, name, lore, enchant, customModelData, hideName));
    }


    public static void setItemToGUI(Inventory inv, int slot, ItemStack item){
        if(slot != -1){
            inv.setItem(slot, item);
        }
    }

    public static void setItemToGUI(Inventory inv, List<Integer> slots, ItemStack item){
        slots.forEach(s -> {
            if(s != -1){
                inv.setItem(s, item);
            }
        });
    }

}
