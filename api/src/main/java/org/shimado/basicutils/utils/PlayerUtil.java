package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerUtil {

    public static boolean isPlayerOnline(@Nonnull UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(@Nonnull String playerName){
        Player player = Bukkit.getPlayer(playerName);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(@Nullable Player player){
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(@Nullable OfflinePlayer player){
        return player != null && player.isOnline();
    }


    public static boolean isPlayerVanished(@Nonnull UUID playerUUID){
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
        if(player == null || !player.isOnline()) return true;
        return ((Player) player).getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }


    public static boolean isPlayerVanished(@Nullable Player player){
        return !isPlayerOnline(player) || player.getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }

}
