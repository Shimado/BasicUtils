package org.shimado.basicutils;

import org.bukkit.plugin.Plugin;
import org.shimado.basicutils.cycles.SchedulerAdapter;
import org.shimado.basicutils.cycles.TaskUtil;
import org.shimado.basicutils.nms.VersionControl;

public class BasicUtils {

    private static Plugin plugin;
    private static VersionControl versionControl;

    public BasicUtils(Plugin plugin){
        this.plugin = plugin;
        versionControl = new VersionControl();
        new SchedulerAdapter(plugin);
        new TaskUtil(plugin);
    }


    public static Plugin getPlugin(){
        return plugin;
    }

    public static VersionControl getVersionControl(){
        return versionControl;
    }

}
