package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerUtil {

    public static boolean isPlayerOnline(UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(String playerName){
        Player player = Bukkit.getPlayer(playerName);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(Player player){
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(OfflinePlayer player){
        return player != null && player.isOnline();
    }


    public static boolean isPlayerVanished(UUID playerUUID){
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
        if(player == null || !player.isOnline()) return false;
        return ((Player) player).getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }


    public static boolean isPlayerVanished(Player player){
        return player.getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }

}
