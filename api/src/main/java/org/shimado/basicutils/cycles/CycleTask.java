package org.shimado.basicutils.cycles;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class CycleTask {

    private int bukkitTask = -1;
    private ScheduledTask foliaTask;

    public CycleTask(int bukkitTask){
        this.bukkitTask = bukkitTask;
    }

    public CycleTask(@NotNull ScheduledTask foliaTask){
        this.foliaTask = foliaTask;
    }


    public void cancelTask(){
        if(bukkitTask != -1){
            Bukkit.getScheduler().cancelTask(bukkitTask);
        }
        else if(foliaTask != null){
            foliaTask.cancel();
        }
    }

}
