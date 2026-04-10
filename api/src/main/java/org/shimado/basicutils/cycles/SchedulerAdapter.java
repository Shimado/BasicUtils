package org.shimado.basicutils.cycles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.utils.PluginsHook;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class SchedulerAdapter {

    private static Plugin plugin;
    private static boolean isFolia;

    private static Class regionSchedulerClass = null;
    private static Class globalSchedulerClass = null;
    private static Class asyncSchedulerClass = null;
    private static Class playerSchedulerClass = null;

    private static Object regionScheduler = null;
    private static Object globalScheduler = null;
    private static Object asyncScheduler = null;
    private static Method playerScheduler = null;

    public SchedulerAdapter(Plugin plugin){
        this.plugin = plugin;
        isFolia = PluginsHook.isFolia();
        if(isFolia){
            try {
                regionSchedulerClass = Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
                globalSchedulerClass = Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
                asyncSchedulerClass = Class.forName("io.papermc.paper.threadedregions.scheduler.AsyncScheduler");
                playerSchedulerClass = Class.forName("io.papermc.paper.threadedregions.scheduler.EntityScheduler");

                regionScheduler = plugin.getServer().getClass().getDeclaredMethod("getRegionScheduler").invoke(plugin.getServer());
                globalScheduler = plugin.getServer().getClass().getDeclaredMethod("getGlobalRegionScheduler").invoke(plugin.getServer());
                asyncScheduler = plugin.getServer().getClass().getDeclaredMethod("getAsyncScheduler").invoke(plugin.getServer());
                playerScheduler = Entity.class.getDeclaredMethod("getScheduler");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Nullable
    public static CycleTask scheduleSyncDelayedTask(@NotNull Runnable task, int delay){
        if(delay < 1) return null;
        if(isFolia){
            try {
                Consumer consumer  = t -> task.run();
                return new CycleTask(globalSchedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Consumer.class, long.class).invoke(globalScheduler, plugin, consumer, (long) delay));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
        }
    }


    @Nullable
    public static CycleTask scheduleSyncDelayedTask(@NotNull Location loc, @NotNull Runnable task, int delay){
        if(delay < 1) return null;
        if(loc == null){
            return scheduleSyncDelayedTask(task, delay);
        }
        if(isFolia){
            try {
                Consumer consumer  = t -> task.run();
                return new CycleTask(regionSchedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Location.class, Consumer.class, long.class).invoke(regionScheduler, plugin, loc, consumer, (long) delay));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
        }
    }


    @Nullable
    public static CycleTask scheduleSyncDelayedTask(@NotNull Player player, @NotNull Runnable task, int delay){
        if(delay < 1) return null;
        if(player == null){
            return scheduleSyncDelayedTask(task, delay);
        }
        if(isFolia){
            try {
                Object scheduler = playerScheduler.invoke(player);
                Consumer consumer  = t -> task.run();
                return new CycleTask(playerSchedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Consumer.class, Runnable.class, long.class).invoke(scheduler, plugin, consumer, null, delay));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
        }
    }


    @Nullable
    public static CycleTask scheduleSyncRepeatingTask(@NotNull Runnable task, int delayBeforeStart, int delay){
        if(delayBeforeStart < 0 || delay < 1) return null;
        if(isFolia){
            if(delayBeforeStart == 0) delayBeforeStart++;
            try {
                Consumer consumer  = t -> task.run();
                return new CycleTask(globalSchedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class).invoke(globalScheduler, plugin, consumer, (long) delayBeforeStart, (long) delay));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
        }
    }


    @Nullable
    public static CycleTask scheduleSyncRepeatingTask(@NotNull Location loc, @NotNull Runnable task, int delayBeforeStart, int delay){
        if(delayBeforeStart < 0 || delay < 1) return null;
        if(loc == null){
            return scheduleSyncRepeatingTask(task, delayBeforeStart, delay);
        }
        if(isFolia){
            if(delayBeforeStart == 0) delayBeforeStart++;
            try {
                Consumer consumer  = t -> task.run();
                return new CycleTask(regionSchedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Location.class, Consumer.class, long.class, long.class).invoke(regionScheduler, plugin, loc, consumer, (long) delayBeforeStart, (long) delay));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
        }
    }


    @Nullable
    public static CycleTask scheduleSyncRepeatingTask(@NotNull Player player, @NotNull Runnable task, int delayBeforeStart, int delay){
        if(delayBeforeStart < 0 || delay < 1) return null;
        if(player == null){
            return scheduleSyncRepeatingTask(task, delayBeforeStart, delay);
        }
        if(isFolia){
            if(delayBeforeStart == 0) delayBeforeStart++;
            try {
                Object scheduler = playerScheduler.invoke(player);
                Consumer consumer  = t -> task.run();
                return new CycleTask(playerSchedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Consumer.class, Runnable.class, long.class, long.class).invoke(scheduler, plugin, consumer, null, delayBeforeStart, delay));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
        }
    }


    public static void callSyncMethod(@NotNull String command){
        if(command == null) return;
        if(isFolia){
            try {
                Runnable runnable  = () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                globalSchedulerClass.getDeclaredMethod("execute", Plugin.class, Runnable.class).invoke(globalScheduler, plugin, runnable);
            }catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }else{
            Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        }
    }


    public static CycleTask runTaskAsynchronously(@NotNull Runnable task){
        if(isFolia){
            try {
                Consumer consumer  = t -> task.run();
                return new CycleTask(asyncSchedulerClass.getDeclaredMethod("runNow", Plugin.class, Consumer.class).invoke(asyncScheduler, plugin, consumer));
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }else{
            return new CycleTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, task).getTaskId());
        }
    }

}
