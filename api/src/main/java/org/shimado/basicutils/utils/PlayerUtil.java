package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerUtil {

    private static final boolean isFolia = PluginsHook.isFolia();
    private static Method teleportAsyncMethod = null;

    static {
        if(isFolia){
            try {
                teleportAsyncMethod = Class.forName("org.bukkit.entity.Entity").getDeclaredMethod("teleportAsync", Location.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to cache teleportAsync method", e);
            }
        }
    }


    public static boolean isPlayerOnline(@NotNull UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(@NotNull String playerName){
        Player player = Bukkit.getPlayer(playerName);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(@Nullable Player player){
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(@Nullable OfflinePlayer player){
        return player != null && player.isOnline();
    }


    public static boolean isPlayerVanished(@NotNull UUID playerUUID){
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
        if(player == null || !player.isOnline()) return true;
        return ((Player) player).getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }


    public static boolean isPlayerVanished(@Nullable Player player){
        return !isPlayerOnline(player) || player.getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }


    public static void playerTeleport(@NotNull Player player, @NotNull Location loc){
        if(isFolia){
            try {
                teleportAsyncMethod.invoke(player, loc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            player.teleport(loc);
        }
    }

}
