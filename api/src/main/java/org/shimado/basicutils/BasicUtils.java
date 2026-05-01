package org.shimado.basicutils;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shimado.basicutils.cycles.SchedulerAdapter;
import org.shimado.basicutils.cycles.TaskUtil;
import org.shimado.basicutils.nms.VersionControl;
import org.shimado.basicutils.utils.MiniMessagesUtil;
import org.shimado.basicutils.utils.PlayerUtil;

public class BasicUtils {

    private static Plugin plugin;
    private static VersionControl versionControl;

    public BasicUtils(Plugin plugin){
        this.plugin = plugin;
        versionControl = new VersionControl();
        new MiniMessagesUtil(plugin);
        new SchedulerAdapter(plugin);
        new TaskUtil(plugin);
    }


    @NotNull
    public static Plugin getPlugin(){
        return plugin;
    }

    @NotNull
    public static VersionControl getVersionControl(){
        return versionControl;
    }

}
