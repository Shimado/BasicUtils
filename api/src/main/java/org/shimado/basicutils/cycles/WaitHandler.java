package org.shimado.basicutils.cycles;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shimado.basicutils.utils.ColorUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaitHandler {

    private static Map<UUID, CycleTask> playerCycleIDs = new HashMap<>();
    private static CycleTask commonCycleID;


    public static boolean createPlayerDelay(@NotNull Player player, double delayInSeconds, String rawMessage){
        UUID playerUUID = player.getUniqueId();

        if(playerCycleIDs.containsKey(playerUUID)){
            player.sendMessage(ColorUtil.getColor(rawMessage));
            return false;
        }

        CycleTask id = SchedulerAdapter.scheduleSyncDelayedTask(player, () -> {
            playerCycleIDs.remove(playerUUID);
        }, (int) (20 * delayInSeconds));
        playerCycleIDs.put(playerUUID, id);

        return true;
    }


    public static boolean createCommonDelay(@NotNull CommandSender sender, double delayInSeconds, String rawMessage){
        if(commonCycleID != null){
            sender.sendMessage(ColorUtil.getColor(rawMessage));
            return false;
        }

        commonCycleID = SchedulerAdapter.scheduleSyncDelayedTask(() -> {
            commonCycleID = null;
        }, (int) (20 * delayInSeconds));

        return true;
    }


    public static void reload(){
        playerCycleIDs.values().forEach(it -> it.cancelTask());
        playerCycleIDs.clear();
        if(commonCycleID != null){
            commonCycleID.cancelTask();
            commonCycleID = null;
        }
    }

}
