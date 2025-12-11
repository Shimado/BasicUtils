package org.shimado.basicutils.cycles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shimado.basicutils.utils.PluginsHook;

public class TaskUtil {

    private static Plugin plugin;
    private static boolean isFolia;

    public TaskUtil(Plugin plugin){
        this.plugin = plugin;
        isFolia = PluginsHook.isFolia();
    }

    public static void runTask(@NotNull Location loc, @NotNull Runnable task){
        if(isFolia){
            Bukkit.getRegionScheduler().run(plugin, loc, t -> task.run());
        }else{
            task.run();
        }
    }

}
