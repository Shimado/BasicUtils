package org.shimado.basicutils.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class PermissionUtil {

    public static boolean hasAccess(@Nonnull Player player, @Nullable String... permissions){
        return player.isOp() || Arrays.stream(permissions).anyMatch(p -> p == null || p.isEmpty() || player.hasPermission(p));
    }

    public static boolean hasAccess(@Nonnull CommandSender sender, @Nullable String... permissions){
        return sender.isOp() || Arrays.stream(permissions).anyMatch(p -> p == null || p.isEmpty() || sender.hasPermission(p));
    }


    public static boolean hasAccessAndSendMessage(@Nonnull Player player, @Nonnull String message, @Nullable String... permissions){
        boolean hasAccess = hasAccess(player, permissions);
        if(!hasAccess) {
            player.sendMessage(ColorUtil.getColor(message));
            SoundUtil.noPermission(player);
        }
        return hasAccess;
    }

    public static boolean hasAccessAndSendMessage(@Nonnull CommandSender sender, @Nonnull String message, @Nullable String... permissions){
        boolean hasAccess = hasAccess(sender, permissions);
        if(!hasAccess) {
            sender.sendMessage(ColorUtil.getColor(message));
            if(sender instanceof Player) SoundUtil.noPermission((Player) sender);
        }
        return hasAccess;
    }

}
