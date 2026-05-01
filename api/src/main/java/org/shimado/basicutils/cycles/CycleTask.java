package org.shimado.basicutils.cycles;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class CycleTask{

    private static Method cancelMethod;

    static {
        try {
            cancelMethod = Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask").getDeclaredMethod("cancel");
        } catch (Exception e) {}
    }


    private final int bukkitTask;
    private final Object foliaTask;

    public CycleTask(int bukkitTask) {
        this.bukkitTask = bukkitTask;
        this.foliaTask  = null;
    }

    public CycleTask(@NotNull Object foliaTask) {
        this.bukkitTask = -1;
        this.foliaTask  = foliaTask;
    }


    public void cancelTask() {
        if (bukkitTask != -1) {
            Bukkit.getScheduler().cancelTask(bukkitTask);
        } else if (foliaTask != null && cancelMethod != null) {
            try {
                cancelMethod.invoke(foliaTask);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
