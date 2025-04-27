package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.shimado.basicutils.BasicUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomModelBlockUtil {

    public static void createTexture(Player player, Location locOrig, ItemStack headItem, int customModelData, boolean toCenterModel){
        if(customModelData <= 0) return;
        Location loc = locOrig.clone();
        if(toCenterModel){
            loc.setYaw(Math.round((player.getLocation().getYaw() + 180) / 90) * 90);
        } else{
            loc.setYaw(player.getLocation().getYaw() - 180);
        }
        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0.5, 0.0, 0.5), EntityType.ARMOR_STAND);
        stand.setCustomName("texture_cmd");
        stand.setCustomNameVisible(false);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setInvulnerable(true);
        stand.setVisible(false);
        stand.setHelmet(CreateItemUtil.create(headItem, " ", new ArrayList<>(), false, customModelData, true));
    }


    public static void removeTexture(Location loc){
        if(loc.getWorld() == null) return;
        loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 0.2, 0.5, 0.2).forEach(it -> {
            if(it instanceof ArmorStand){
                EntityEquipment equipment = ((ArmorStand) it).getEquipment();
                if(equipment != null && equipment.getHelmet() != null && it.getCustomName().equals("texture_cmd")){
                    ((ArmorStand) it).damage(((ArmorStand) it).getHealth());
                    it.remove();
                }
            }
        });
    }


    public static void setTexture(List<Location> locs, ItemStack headItem, int customModelData, boolean toCenterModel){
        Bukkit.getScheduler().scheduleSyncDelayedTask(BasicUtils.getPlugin(), () -> {

            for(Location l : locs){
                removeTexture(l);

                Location loc = l.clone();
                if(toCenterModel){
                    loc.setYaw(Math.round(loc.getYaw() / 90) * 90);
                }
                ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0.5, 0.0, 0.5), EntityType.ARMOR_STAND);
                stand.setCustomName("texture_cmd");
                stand.setCustomNameVisible(false);
                stand.setGravity(false);
                stand.setBasePlate(false);
                stand.setInvulnerable(true);
                stand.setVisible(false);
                stand.setHelmet(CreateItemUtil.create(headItem, " ", new ArrayList<>(), false, customModelData, true));
            }

        }, 20);
    }

}
