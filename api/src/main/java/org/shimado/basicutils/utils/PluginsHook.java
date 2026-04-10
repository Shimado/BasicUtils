package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PluginsHook {

    public static boolean isFolia(){
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.EntityScheduler");
            return true;
        }catch (ClassNotFoundException ex){
            return false;
        }
    }

    private static boolean isPlugin(@NotNull String pluginName){
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


    public static boolean isNexo(){
        return isPlugin("Nexo");
    }


    public static boolean isDiscordSRV(){
        return isPlugin("DiscordSRV");
    }


    public static boolean isDiscordSynthesis(){
        return isPlugin("DiscordSynthesis");
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


    public static boolean isWorldGuard(){
        return isPlugin("WorldGuard");
    }


    public static boolean isCoinsEngine(){
        return isPlugin("CoinsEngine") || isPlugin("ExcellentEconomy");
    }


    public static boolean isExcellentEconomy(){
        return isCoinsEngine();
    }


    public static boolean isArcLight(){
        return new File(Bukkit.getServer().getWorldContainer().getParentFile() + File.separator + "mohist-config", "mohist.yml").exists();
    }

}