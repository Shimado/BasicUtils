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

    private static Class<?> regionSchedulerClass;
    private static Class<?> globalSchedulerClass;
    private static Class<?> asyncSchedulerClass;
    private static Class<?> playerSchedulerClass;

    private static Object regionScheduler;
    private static Object globalScheduler;
    private static Object asyncScheduler;
    private static Method playerSchedulerGetter;

    private static Method regionRunDelayed;
    private static Method regionRunAtFixedRate;
    private static Method globalRunDelayed;
    private static Method globalRunAtFixedRate;
    private static Method globalExecute;
    private static Method asyncRunNow;
    private static Method playerRunDelayed;
    private static Method playerRunAtFixedRate;

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
                playerSchedulerGetter = Entity.class.getDeclaredMethod("getScheduler");

                regionRunDelayed = regionSchedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Location.class, Consumer.class, long.class);
                regionRunAtFixedRate = regionSchedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Location.class, Consumer.class, long.class, long.class);
                globalRunDelayed = globalSchedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Consumer.class, long.class);
                globalRunAtFixedRate = globalSchedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);
                globalExecute = globalSchedulerClass.getDeclaredMethod("execute", Plugin.class, Runnable.class);
                asyncRunNow = asyncSchedulerClass.getDeclaredMethod("runNow", Plugin.class, Consumer.class);
                playerRunDelayed = playerSchedulerClass.getDeclaredMethod("runDelayed", Plugin.class, Consumer.class, Runnable.class, long.class);
                playerRunAtFixedRate = playerSchedulerClass.getDeclaredMethod("runAtFixedRate", Plugin.class, Consumer.class, Runnable.class, long.class, long.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Folia schedulers", e);
            }
        }
    }


    @Nullable
    public static CycleTask scheduleSyncDelayedTask(@NotNull Runnable task, int delay) {
        if (delay < 1) return null;
        if (isFolia) {
            try {
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(globalRunDelayed.invoke(globalScheduler, plugin, consumer, (long) delay));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
    }


    @Nullable
    public static CycleTask scheduleSyncDelayedTask(@Nullable Location loc, @NotNull Runnable task, int delay) {
        if (delay < 1) return null;
        if (loc == null) return scheduleSyncDelayedTask(task, delay);
        if (isFolia) {
            try {
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(regionRunDelayed.invoke(regionScheduler, plugin, loc, consumer, (long) delay));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
    }


    @Nullable
    public static CycleTask scheduleSyncDelayedTask(@Nullable Player player, @NotNull Runnable task, int delay) {
        if (delay < 1) return null;
        if (player == null) return scheduleSyncDelayedTask(task, delay);
        if (isFolia) {
            try {
                Object scheduler = playerSchedulerGetter.invoke(player);
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(playerRunDelayed.invoke(scheduler, plugin, consumer, null, delay));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay));
    }


    @Nullable
    public static CycleTask scheduleSyncRepeatingTask(@NotNull Runnable task, int delayBeforeStart, int delay) {
        if (delayBeforeStart < 0 || delay < 1) return null;
        if (isFolia) {
            if (delayBeforeStart == 0) delayBeforeStart = 1;
            try {
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(globalRunAtFixedRate.invoke(globalScheduler, plugin, consumer, (long) delayBeforeStart, (long) delay));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
    }


    @Nullable
    public static CycleTask scheduleSyncRepeatingTask(@Nullable Location loc, @NotNull Runnable task, int delayBeforeStart, int delay) {
        if (delayBeforeStart < 0 || delay < 1) return null;
        if (loc == null) return scheduleSyncRepeatingTask(task, delayBeforeStart, delay);
        if (isFolia) {
            if (delayBeforeStart == 0) delayBeforeStart = 1;
            try {
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(regionRunAtFixedRate.invoke(regionScheduler, plugin, loc, consumer, (long) delayBeforeStart, (long) delay));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
    }


    @Nullable
    public static CycleTask scheduleSyncRepeatingTask(@Nullable Player player, @NotNull Runnable task, int delayBeforeStart, int delay) {
        if (delayBeforeStart < 0 || delay < 1) return null;
        if (player == null) return scheduleSyncRepeatingTask(task, delayBeforeStart, delay);
        if (isFolia) {
            if (delayBeforeStart == 0) delayBeforeStart = 1;
            try {
                Object scheduler = playerSchedulerGetter.invoke(player);
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(playerRunAtFixedRate.invoke(scheduler, plugin, consumer, null, delayBeforeStart, delay));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delayBeforeStart, delay));
    }


    public static void callSyncMethod(@Nullable String command) {
        if (command == null) return;
        if (isFolia) {
            try {
                Runnable runnable = () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                globalExecute.invoke(globalScheduler, plugin, runnable);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        }
    }


    @NotNull
    public static CycleTask runTaskAsynchronously(@NotNull Runnable task) {
        if (isFolia) {
            try {
                Consumer<Object> consumer = t -> task.run();
                return new CycleTask(asyncRunNow.invoke(asyncScheduler, plugin, consumer));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new CycleTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, task).getTaskId());
    }

}
