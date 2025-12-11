package org.shimado.basicutils.utils;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.flags.types.RoleFlag;
import me.angeschossen.lands.api.land.LandWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LandsUtil {

    private LandsIntegration api;

    public LandsUtil(Plugin plugin){
        if (PluginsHook.isLands()) {
            api = LandsIntegration.of(plugin);
        }
    }


    public boolean isHasAccess(@NotNull Player player, @NotNull Location loc, boolean breakOfPlace){
        if(api == null) return true;

        LandWorld world = api.getWorld(loc.getWorld());
        if (world != null) {
            try{
                ItemStack item = player.getInventory().getItemInMainHand();
                Material material = item == null ? Material.AIR : item.getType();
                return world.hasFlag(player, loc, material, (RoleFlag) (breakOfPlace ? Flags.BLOCK_BREAK : Flags.BLOCK_PLACE), true);
            }catch (Exception ex){
                return true;
            }
        }
        return true;
    }


}
