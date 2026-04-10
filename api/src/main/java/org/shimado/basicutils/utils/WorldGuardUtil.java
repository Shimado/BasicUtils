package org.shimado.basicutils.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WorldGuardUtil {

    private WorldGuard worldGuard = null;

    public WorldGuardUtil(){
        if(PluginsHook.isWorldGuard()){
            worldGuard = WorldGuard.getInstance();
        }
    }


    public boolean canInteractWith(@NotNull UUID playerUUID, @NotNull Location loc){
        if(worldGuard == null) return true;
        ApplicableRegionSet set = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));

        if(set != null && !set.getRegions().isEmpty()){
            return set.getRegions().stream().anyMatch(it -> it.getMembers().getUniqueIds().contains(playerUUID) || it.getOwners().getUniqueIds().contains(playerUUID));
        }

        return true;
    }

}
