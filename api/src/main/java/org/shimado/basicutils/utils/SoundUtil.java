package org.shimado.basicutils.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoundUtil {

//    private static List<Sound> SOUNDS = Arrays.stream(Sound.class.getDeclaredFields()).map(it -> {
//        try {
//            if (!Modifier.isPublic(it.getModifiers())) return null;
//            Object obj = it.get(null);
//            if (obj == null || !(obj instanceof Sound)) return null;
//            return (Sound) it.get(null);
//        } catch (IllegalAccessException e) {
//            return null;
//        }
//    }).filter(it -> it != null).collect(Collectors.toList());
//
//
//    public static Sound getSound(String... sounds) {
//        for (Object sound : SOUNDS) {
//            for (String s : sounds) {
//                if (sound.toString().equals(s) || (sound instanceof Sound && ((Sound) sound).getKey().getKey().equalsIgnoreCase(s))) {
//                    return (Sound) sound;
//                }
//            }
//        }
//        return Sound.UI_TOAST_IN;
//    }


    private static Field[] SOUNDS = getAllSounds();


    private static Field[] getAllSounds(){
        Field[] fields;
        try {
            fields = Class.forName("org.bukkit.Sound").getFields();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fields;
    }


    public static Sound getSound(@Nonnull String... sounds){
        try {
            for(Field f : SOUNDS){
                for(String s : sounds){
                    if(s.equals(f.getName())){
                        return (Sound) f.get(null);
                    }
                }
            }
        }catch (IllegalAccessException e){}

        return Sound.UI_TOAST_IN;
    }


    public static void click(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("UI.BUTTON.CLICK", "UI_BUTTON_CLICK"), 1f, 0.7f);
    }

    public static void accept(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_CLUSTER.HIT", "BLOCK_AMETHYST_CLUSTER_HIT", "ENTITY_VILLAGER_YES"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.RESONATE", "BLOCK_AMETHYST_BLOCK_RESONATE", "BLOCK_CHEST_OPEN"), 1f, 1.1f);
    }

    public static void reject(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.IRON_DOOR.CLOSE", "BLOCK_IRON_DOOR_CLOSE"), 1f, 0.9f);
    }

    public static void importPainting(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 0.7f, 1f);
    }

    public static void openBook(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.7f);
    }

    public static void delete(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.CLOSE", "BLOCK_CHEST_CLOSE"), 0.5f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.ENDER_CHEST.CLOSE", "BLOCK_ENDER_CHEST_CLOSE"), 0.8f, 0.6f);
    }

    public static void movePainting(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 1f, 1f);
    }

    public static void download(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN"), 1f, 0.8f);
    }

    public static void purchase(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.YES", "ENTITY_VILLAGER_YES"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void sell(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.YES", "ENTITY_VILLAGER_YES"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP"), 1f, 1f);
    }

    public static void like(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.FIREWORK_ROCKET.TWINKLE_FAR", "ENTITY_FIREWORK_ROCKET_TWINKLE_FAR"), 1f, 0.7f);
    }

    public static void dislike(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE"), 1f, 0.7f);
    }

    public static void changePage(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.7f);
    }

    public static void change(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.LEVER.CLICK", "BLOCK_LEVER_CLICK"), 0.5f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.ARMOR_STAND.HIT", "ENTITY_ARMOR_STAND_HIT", "ENTITY_ARMORSTAND_HIT"), 1f, 0.7f);
    }

    public static void installation(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.WORK_TOOLSMITH", "ENTITY_VILLAGER_WORK_TOOLSMITH"), 1f, 0.7f);
    }

    public static void startDrawing(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 0.7f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void changeTool(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.WORK_TOOLSMITH", "ENTITY_VILLAGER_WORK_TOOLSMITH"), 0.7f, 0.7f);
    }

    public static void rollBack(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.FISHING_BOBBER.RETRIEVE", "ENTITY_FISHING_BOBBER_RETRIEVE"), 1f, 0.3f);
    }

    public static void preview(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void no(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO"), 1f, 1f);
    }

    public static void noPermission(@Nonnull Player player) {
        no(player);
    }

    public static void openPage(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.LEVER.CLICK", "BLOCK_LEVER_CLICK"), 0.8f, 1f);
    }

    public static void step(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.METAL.STEP", "BLOCK_METAL_STEP"), 0.8f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.WOOL.BREAK", "BLOCK_WOOL_BREAK", "DIG_WOOL", "BLOCK_CLOTH_BREAK"), 0.3f, 0.8f);
    }

    public static void victory(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.CHALLENGE_COMPLETE", "UI_TOAST_CHALLENGE_COMPLETE"), 0.3f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 0.3f, 1f);
    }

    public static void jackpot(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.CHALLENGE_COMPLETE", "UI_TOAST_CHALLENGE_COMPLETE"), 0.3f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 0.3f, 1f);
    }

    public static void bonus(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.ITEM.PICKUP", "ENTITY_ITEM_PICKUP"), 0.8f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.ZOMBIE.INFECT", "ENTITY_ZOMBIE_INFECT", "ZOMBIE_INFECT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE", "ENTITY_WITCH_AMBIENT"), 0.7f, 0.5f);
    }

    public static void hit(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ITEM.TRIDENT.HIT", "ITEM_TRIDENT_HIT", "ENTITY_ARROW_HIT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("ITEM.TRIDENT.HIT_GROUND", "ITEM_TRIDENT_HIT_GROUND", "BLOCK_ANVIL_HIT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE", "ENTITY_WITCH_AMBIENT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.IN", "UI_TOAST_IN"), 1f, 1f);
    }

    public static void lost(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.BLAZE.DEATH", "ENTITY_BLAZE_DEATH"), 0.7f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 0.8f, 0.5f);
        player.playSound(player.getLocation(), getSound("ENTITY.ZOMBIE.DEATH", "ENTITY_ZOMBIE_DEATH"), 0.5f, 0.7f);
    }

    public static void bet(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.HORSE.BREATHE", "ENTITY_HORSE_BREATHE", "ENTITY_HORSE_BREATHE"), 0.8f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.OPEN", "BLOCK_BARREL_OPEN", "BLOCK_CHEST_OPEN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("UI.LOOM.SELECT_PATTERN", "UI_LOOM_SELECT_PATTERN", "UI_TOAST_IN"), 1f, 1f);
    }

    public static void giveBet(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN", "CHEST_OPEN", "ENTITY_CHEST_OPEN"), 1f, 1f);
    }

    public static void activate(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.ITEM.PICKUP", "ENTITY_ITEM_PICKUP"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.IN", "UI_TOAST_IN"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.STONE_BUTTON.CLICK_ON", "BLOCK_STONE_BUTTON_CLICK_ON", "ENTITY_ITEM_PICKUP"), 1f, 1f);
    }

    public static void explode(@Nonnull Player player) {
        player.playSound(player.getLocation(), getSound("UI.TOAST.IN", "UI_TOAST_IN"), 1f, 0.4f);
        player.playSound(player.getLocation(), getSound("ENTITY.GENERIC.EXPLODE", "ENTITY_GENERIC_EXPLODE", "ENTITY_DRAGON_FIREBALL_EXPLODE"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE", "ENTITY_WITCH_AMBIENT"), 0.8f, 0.5f);
    }

    public static void timerPreStart(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1f, 0.9f);
    }

    public static void timerStart(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.FIREWORK_ROCKET.LAUNCH", "ENTITY_FIREWORK_ROCKET_LAUNCH", "ENTITY_ENDERDRAGON_HURT"), 1f, 0.8f);
    }

    public static void horseMove(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.HORSE.GALLOP", "ENTITY_HORSE_GALLOP"), 1f, 0.9f);
    }

    public static void notGameSlot(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.GRAVEL.FALL", "BLOCK_GRAVEL_FALL"), 1f, 1f);
    }

    public static void doubleBet(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.ZOMBIE_VILLAGER.CURE", "ENTITY_ZOMBIE_VILLAGER_CURE", "ENTITY_ZOMBIE_DEATH"), 1f, 1f);
    }

    public static void giveCards(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN", "ENTITY_EGG_THROW"), 1f, 0.9f);
    }

    public static void leave(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.WOODEN_DOOR.CLOSE", "BLOCK_WOODEN_DOOR_CLOSE", "BLOCK_WOODEN_DOOR_CLOSE"), 0.5f, 1f);
    }

    public static void draw(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO", "VILLAGER_NO"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.WOLF.SHAKE", "ENTITY_WOLF_SHAKE", "WOLF_SHAKE"), 1f, 0.9f);
    }

    public static void openChest(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN"), 0.7f, 1f);
    }

    public static void restriction(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.ANVIL.FALL", "BLOCK_ANVIL_FALL"), 1f, 0.9f);
    }

    public static void laugh(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 1f, 0.9f);
    }

    public static void raise(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.ANVIL.PLACE", "BLOCK_ANVIL_PLACE"), 1f, 0.9f);
    }

    public static void shake(@Nonnull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.WOOD.HIT", "BLOCK_WOOD_HIT"), 1f, 0.9f);
    }

}
