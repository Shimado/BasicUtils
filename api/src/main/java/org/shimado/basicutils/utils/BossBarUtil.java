package org.shimado.basicutils.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BossBarUtil {

    @NotNull
    public static BossBar createBossBar(@NotNull Player player, @NotNull String text, @NotNull BarColor barColor, @NotNull BarStyle barStyle){
        BossBar bossBar = Bukkit.createBossBar(
                ColorUtil.getColor(text),
                barColor,
                barStyle
        );
        bossBar.setVisible(true);
        bossBar.addPlayer(player);
        return bossBar;
    }


    @NotNull
    public static BossBar createBossBar(@NotNull List<Player> players, @NotNull String text, @NotNull BarColor barColor, @NotNull BarStyle barStyle){
        BossBar bossBar = Bukkit.createBossBar(
                ColorUtil.getColor(text),
                barColor,
                barStyle
        );
        bossBar.setVisible(true);
        players.forEach(bossBar::addPlayer);
        return bossBar;
    }


    public static void clearBossBar(@Nullable BossBar bossBar){
        if(bossBar != null){
            bossBar.setVisible(false);
            bossBar.removeAll();
        }
    }


    public static void sendActionBar(@NotNull Player player, @NotNull String text){
        try{
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorUtil.getColor(text)));
        }catch (Exception e1){}
    }

}
