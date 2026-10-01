package org.shimado.basicutils.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WorldGuardUtil {

    private WorldGuard worldGuard = null;

    public WorldGuardUtil(){
        if(PluginsHook.isWorldGuard()){
            worldGuard = WorldGuard.getInstance();
        }
    }


    public boolean canBuild(@NotNull UUID playerUUID, @NotNull Location loc) {
        if (worldGuard == null) return true;

        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;

        com.sk89q.worldguard.LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        com.sk89q.worldedit.util.Location wgLoc = BukkitAdapter.adapt(loc);

        return query.testBuild(wgLoc, localPlayer, Flags.BUILD);
    }

}
