package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;

public class PluginsHook {

    public static boolean isFolia(){
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.EntityScheduler");
            return true;
        }catch (ClassNotFoundException ex){
            return false;
        }
    }


    public static boolean isBoombox(){
        return Bukkit.getServer().getPluginManager().getPlugin("Boombox") != null;
    }


    public static boolean isPlaceholderAPI(){
        return Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }


    public static boolean isItemsAdder(){
        return Bukkit.getServer().getPluginManager().getPlugin("ItemsAdder") != null;
    }


    public static boolean isOraxen(){
        return Bukkit.getServer().getPluginManager().getPlugin("Oraxen") != null;
    }


    public static boolean isDiscordSRV(){
        return Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV") != null;
    }


    public static boolean isVault(){
        return Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;
    }


    public static boolean isPlayerPoints(){
        return Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints") != null;
    }


    public static boolean isVotingPlugin(){
        return Bukkit.getServer().getPluginManager().getPlugin("VotingPlugin") != null;
    }


    public static boolean isLuckPerms(){
        return Bukkit.getServer().getPluginManager().getPlugin("LuckPerms") != null;
    }

}