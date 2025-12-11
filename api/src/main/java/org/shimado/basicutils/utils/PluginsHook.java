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

    private static boolean isPlugin(String pluginName){
        return Bukkit.getServer().getPluginManager().getPlugin(pluginName) != null;
    }


    public static boolean isBoombox(){
        return isPlugin("Boombox");
    }


    public static boolean isPlaceholderAPI(){
        return isPlugin("PlaceholderAPI");
    }


    public static boolean isItemsAdder(){
        return isPlugin("ItemsAdder");
    }


    public static boolean isOraxen(){
        return isPlugin("Oraxen");
    }


    public static boolean isDiscordSRV(){
        return isPlugin("DiscordSRV");
    }


    public static boolean isVault(){
        return isPlugin("Vault");
    }


    public static boolean isPlayerPoints(){
        return isPlugin("PlayerPoints");
    }


    public static boolean isVotingPlugin(){
        return isPlugin("VotingPlugin");
    }


    public static boolean isLuckPerms(){
        return isPlugin("LuckPerms");
    }


    public static boolean isVegas(){
        return isPlugin("Vegas");
    }


    public static boolean isLands(){
        return isPlugin("Lands");
    }

}