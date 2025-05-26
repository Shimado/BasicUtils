package org.shimado.basicutils.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PermissionUtil {

    public static boolean hasAccess(Player player, String... permissions){
        return player.isOp() || Arrays.stream(permissions).anyMatch(p -> player.hasPermission(p));
    }

    public static boolean hasAccess(CommandSender sender, String... permissions){
        return sender.isOp() || Arrays.stream(permissions).anyMatch(p -> sender.hasPermission(p));
    }


    public static boolean hasAccessAndSendMessage(Player player, String message, String... permissions){
        boolean hasAccess = hasAccess(player, permissions);
        if(!hasAccess) {
            player.sendMessage(ColorUtil.getColor(message));
            SoundUtil.noPermission(player);
        }
        return hasAccess;
    }

    public static boolean hasAccessAndSendMessage(CommandSender sender, String message, String... permissions){
        boolean hasAccess = hasAccess(sender, permissions);
        if(!hasAccess) {
            sender.sendMessage(ColorUtil.getColor(message));
            if(sender instanceof Player) SoundUtil.noPermission((Player) sender);
        }
        return hasAccess;
    }

}
