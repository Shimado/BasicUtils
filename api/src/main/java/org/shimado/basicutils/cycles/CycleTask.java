package org.shimado.basicutils.cycles;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class CycleTask{

    private int bukkitTask = -1;
    private Object foliaTask;

    public CycleTask(int bukkitTask){
        this.bukkitTask = bukkitTask;
    }

    public CycleTask(@NotNull Object foliaTask){
        this.foliaTask = foliaTask;
    }


    public void cancelTask(){
        if(bukkitTask != -1){
            Bukkit.getScheduler().cancelTask(bukkitTask);
        }
        else if(foliaTask != null){
            try {
                Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask").getDeclaredMethod("cancel").invoke(foliaTask);
            }catch (Exception ex){}
        }
    }

}
