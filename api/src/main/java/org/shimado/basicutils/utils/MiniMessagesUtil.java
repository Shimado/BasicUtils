package org.shimado.basicutils.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MiniMessagesUtil {

    //private static BukkitAudiences adventure;

    public MiniMessagesUtil(Plugin plugin){
//        try {
//            adventure = BukkitAudiences.create(plugin);
//        }catch (Exception ex){}
    }


    public void closeAdventure(){
//        if(adventure != null){
//            adventure.close();
//            adventure = null;
//        }
    }


    public static void sendMessage(@NotNull Player player, @NotNull String text){
//        if(adventure == null){
//            String formattedText = text
//                    .replaceAll("<color:([^>]+)>", "<$1>")
//                    .replaceAll("<colour:([^>]+)>", "<$1>")
//                    .replaceAll("<c:([^>]+)>", "<$1>")
//                    .replace("<black>", "&0")
//                    .replace("<dark_blue>", "&1")
//                    .replace("<dark_green>", "&2")
//                    .replace("<dark_aqua>", "&3")
//                    .replace("<dark_red>", "&4")
//                    .replace("<dark_purple>", "&5")
//                    .replace("<gold>", "&6")
//                    .replace("<gray>", "&7")
//                    .replace("<dark_gray>", "&8")
//                    .replace("<blue>", "&9")
//                    .replace("<green>", "&a")
//                    .replace("<aqua>", "&b")
//                    .replace("<red>", "&c")
//                    .replace("<light_purple>", "&d")
//                    .replace("<yellow>", "&e")
//                    .replace("<white>", "&f")
//                    .replaceAll("<(#([A-Fa-f0-9]{6}))>", "$1")
//
//                    .replace("bold", "&l")
//                    .replace("b", "&l")
//
//                    .replace("italic", "&o")
//                    .replace("em", "&o")
//                    .replace("i", "&o")
//
//                    .replace("underlined", "&n")
//                    .replace("u", "&n")
//
//                    .replace("strikethrough", "&m")
//                    .replace("st", "&m")
//
//                    .replace("obfuscated", "&k")
//                    .replace("obf", "&k");
//            player.sendMessage(ColorUtil.getColor(formattedText));
//            return;
//        }
//
//        Audience paperPlayer = adventure.player(player);
//        MiniMessage mm = MiniMessage.miniMessage();
//        Component parsed = mm.deserialize(text);
//        paperPlayer.sendMessage(parsed);
    }

}
