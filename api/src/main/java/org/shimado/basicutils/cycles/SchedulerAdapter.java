package org.shimado.basicutils.cycles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shimado.basicutils.utils.PluginsHook;

public class SchedulerAdapter {

    private static Plugin plugin;
    private static boolean isFolia;

    public SchedulerAdapter(Plugin plugin){
        this.plugin = plugin;
        isFolia = PluginsHook.isFolia();
    }


    public static CycleTask scheduleSyncDelayedTask(@NotNull Runnable task, int delay){
        if(delay < 1) return null;
        if(isFolia){
            return new CycleTask(plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, t -> task.run(), delay));
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
        }
    }


    public static CycleTask scheduleSyncDelayedTask(@NotNull Location loc, @NotNull Runnable task, int delay){
        if(delay < 1) return null;
        if(loc == null){
            return scheduleSyncDelayedTask(task, delay);
        }
        if(isFolia){
            return new CycleTask(plugin.getServer().getRegionScheduler().runDelayed(plugin, loc, t -> task.run(), delay));
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
        }
    }


    public static CycleTask scheduleSyncDelayedTask(@NotNull Player player, @NotNull Runnable task, int delay){
        if(delay < 1) return null;
        if(player == null){
            return scheduleSyncDelayedTask(task, delay);
        }
        if(isFolia){
            return new CycleTask(player.getScheduler().runDelayed(plugin, t -> task.run(), null, delay));
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
        }
    }


    public static CycleTask scheduleSyncRepeatingTask(@NotNull Runnable task, int delayBeforeStart, int delay){
        if(delayBeforeStart < 0 || delay < 1) return null;
        if(isFolia){
            if(delayBeforeStart == 0) delayBeforeStart++;
            return new CycleTask(plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, t -> task.run(), delayBeforeStart, delay));
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
        }
    }


    public static CycleTask scheduleSyncRepeatingTask(@NotNull Location loc, @NotNull Runnable task, int delayBeforeStart, int delay){
        if(delayBeforeStart < 0 || delay < 1) return null;
        if(loc == null){
            return scheduleSyncRepeatingTask(task, delayBeforeStart, delay);
        }
        if(isFolia){
            if(delayBeforeStart == 0) delayBeforeStart++;
            return new CycleTask(plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, loc, t -> task.run(), delayBeforeStart, delay));
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
        }
    }


    public static CycleTask scheduleSyncRepeatingTask(@NotNull Player player, @NotNull Runnable task, int delayBeforeStart, int delay){
        if(delayBeforeStart < 0 || delay < 1) return null;
        if(player == null){
            return scheduleSyncRepeatingTask(task, delayBeforeStart, delay);
        }
        if(isFolia){
            if(delayBeforeStart == 0) delayBeforeStart++;
            return new CycleTask(player.getScheduler().runAtFixedRate(plugin, t -> task.run(), null, delayBeforeStart, delay));
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
        }
    }


    public static void runTaskAsynchronously(@NotNull Runnable task){
        if(isFolia){
            plugin.getServer().getAsyncScheduler().runNow(plugin, t -> task.run());
        }else{
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

}
