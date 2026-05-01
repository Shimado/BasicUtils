package org.shimado.basicutils.cycles;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shimado.basicutils.utils.PluginsHook;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class TaskUtil {

    private static Plugin plugin;
    private static boolean isFolia;

    private static Class<?> regionSchedulerClass;
    private static Object regionScheduler;
    private static Method regionRunMethod;

    public TaskUtil(Plugin plugin){
        this.plugin = plugin;
        isFolia = PluginsHook.isFolia();
        if (isFolia) {
            try {
                regionSchedulerClass = Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
                regionScheduler = plugin.getServer().getClass().getDeclaredMethod("getRegionScheduler").invoke(plugin.getServer());
                regionRunMethod = regionSchedulerClass.getDeclaredMethod("run", Plugin.class, Location.class, Consumer.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Folia RegionScheduler", e);
            }
        }
    }

    public static void runTask(@NotNull Location loc, @NotNull Runnable task) {
        if (isFolia) {
            try {
                Consumer<Object> consumer = t -> task.run();
                regionRunMethod.invoke(regionScheduler, plugin, loc, consumer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            task.run();
        }
    }

}
