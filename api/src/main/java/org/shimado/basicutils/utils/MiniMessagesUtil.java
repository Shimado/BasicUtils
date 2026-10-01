package org.shimado.basicutils.utils;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiniMessagesUtil {

    public static void sendMessage(@NotNull CommandSender player, @NotNull String text){
        player.sendMessage(ColorUtil.getColor(text));
    }

    public static void sendMessage(@NotNull CommandSender player, @NotNull List<String> text){
        text.forEach(it -> player.sendMessage(ColorUtil.getColor(it)));
    }

    public static void sendMessage(@NotNull CommandSender player, @NotNull List<String> text, @NotNull Map<String, String> placeholders){
        List<String> formattedText = text.stream().map(it -> {
            String lR = it;
            for(Map.Entry<String, String> a : placeholders.entrySet()){
                lR = lR.replace(a.getKey(), a.getValue());
            }
            return lR;
        }).collect(Collectors.toList());
        sendMessage(player, formattedText);
    }

}
