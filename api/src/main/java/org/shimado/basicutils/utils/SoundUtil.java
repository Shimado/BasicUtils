package org.shimado.basicutils.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoundUtil {

    private static List<Sound> SOUNDS = Arrays.stream(Sound.class.getDeclaredFields()).map(it -> {
        try {
            if(!Modifier.isPublic(it.getModifiers())) return null;
            Object obj = it.get(null);
            if(obj == null || !(obj instanceof Sound)) return null;
            return (Sound) it.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }).filter(it -> it != null).collect(Collectors.toList());


    public static Sound getSound(String... sounds) {
        for(Object sound : SOUNDS){
            for(String s : sounds){
                if(sound.toString().equals(s)){
                    return (Sound) sound;
                }
            }
        }
        return Sound.UI_TOAST_IN;
    }


    public static void click(Player player){
        player.playSound(player.getLocation(), getSound("UI.BUTTON.CLICK", "UI_BUTTON_CLICK"), 1f, 0.7f);
    }

    public static void accept(Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_CLUSTER.HIT", "BLOCK_AMETHYST_CLUSTER_HIT", "ENTITY_VILLAGER_YES"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.RESONATE", "BLOCK_AMETHYST_BLOCK_RESONATE", "BLOCK_CHEST_OPEN"), 1f, 1.1f);
    }

    public static void reject(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.IRON_DOOR.CLOSE", "BLOCK_IRON_DOOR_CLOSE"), 1f, 0.9f);
    }

    public static void importPainting(Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 0.7f, 1f);
    }

    public static void openBook(Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.7f);
    }

    public static void delete(Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.CLOSE", "BLOCK_CHEST_CLOSE"), 0.5f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.ENDER_CHEST.CLOSE", "BLOCK_ENDER_CHEST_CLOSE"), 0.8f, 0.6f);
    }

    public static void movePainting(Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 1f, 1f);
    }

    public static void download(Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN"), 1f, 0.8f);
    }

    public static void purchase(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.YES", "ENTITY_VILLAGER_YES"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void like(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.FIREWORK_ROCKET.TWINKLE_FAR", "ENTITY_FIREWORK_ROCKET_TWINKLE_FAR"), 1f, 0.7f);
    }

    public static void dislike(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE"), 1f, 0.7f);
    }

    public static void changePage(Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.7f);
    }

    public static void change(Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.ANVIL.USE", "BLOCK_ANVIL_USE"), 0.5f, 0.7f);
    }

    public static void installation(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.WORK_TOOLSMITH", "ENTITY_VILLAGER_WORK_TOOLSMITH"), 1f, 0.7f);
    }

    public static void startDrawing(Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 0.7f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void changeTool(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.WORK_TOOLSMITH", "ENTITY_VILLAGER_WORK_TOOLSMITH"), 0.7f, 0.7f);
    }

    public static void rollBack(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.FISHING_BOBBER.RETRIEVE", "ENTITY_FISHING_BOBBER_RETRIEVE"), 1f, 0.3f);
    }

    public static void preview(Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void noPermission(Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO"), 1f, 1f);
    }

}
