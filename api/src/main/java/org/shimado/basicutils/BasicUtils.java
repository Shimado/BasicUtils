package org.shimado.basicutils;

import org.bukkit.plugin.Plugin;
import org.shimado.basicutils.nms.VersionControl;
import org.shimado.basicutils.utils.EconomyUtil;
import org.shimado.basicutils.utils.GroupUtil;

public class BasicUtils {

    private static Plugin plugin;
    private static VersionControl versionControl;

    public BasicUtils(Plugin plugin){
        this.plugin = plugin;
        this.versionControl = new VersionControl();
    }

    public static Plugin getPlugin(){
        return plugin;
    }

    public static VersionControl getVersionControl(){
        return versionControl;
    }

}
