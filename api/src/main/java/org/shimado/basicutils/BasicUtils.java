package org.shimado.basicutils;

import org.bukkit.plugin.Plugin;
import org.shimado.basicutils.nms.VersionControl;
import org.shimado.basicutils.utils.EconomyUtil;

public class BasicUtils {

    private static Plugin plugin;
    private static VersionControl versionControl;
    private static EconomyUtil economyUtil;


    public BasicUtils(Plugin plugin){
        this.plugin = plugin;
        this.versionControl = new VersionControl();
        this.economyUtil = new EconomyUtil();
    }

    public static Plugin getPlugin(){
        return plugin;
    }

    public static VersionControl getVersionControl(){
        return versionControl;
    }

    public static EconomyUtil getEconomyUtil(){
        return economyUtil;
    }

}
