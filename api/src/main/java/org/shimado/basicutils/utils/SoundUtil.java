package org.shimado.basicutils.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SoundUtil {

    private static final Map<String, Sound> SOUNDS = new HashMap<>();

    static {
        Arrays.stream(Sound.class.getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .forEach(field -> {
                    try {
                        Object sound = field.get(null);
                        if(sound != null) SOUNDS.put(field.getName(), (Sound) sound);
                    } catch (IllegalAccessException e) {
                    }
                });
    }


    @NotNull
    public static Sound getSound(@NotNull String... sounds) {
        for (String soundName : sounds) {
            Sound sound = SOUNDS.get(soundName);
            if (sound != null) {
                return sound;
            }
        }
        return Sound.UI_TOAST_IN;
    }


    public static void placeBlock(@NotNull Location locBlock){
        locBlock.getWorld().playSound(locBlock, Sound.BLOCK_STONE_PLACE, 1f, 1f);
    }

    public static void breakBlock(@NotNull Location locBlock){
        locBlock.getWorld().playSound(locBlock, Sound.BLOCK_STONE_BREAK, 1f, 1f);
    }

    public static void click(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("UI.BUTTON.CLICK", "UI_BUTTON_CLICK"), 1f, 0.7f);
    }

    public static void accept(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_CLUSTER.HIT", "BLOCK_AMETHYST_CLUSTER_HIT", "ENTITY_VILLAGER_YES"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.RESONATE", "BLOCK_AMETHYST_BLOCK_RESONATE", "BLOCK_CHEST_OPEN"), 1f, 1.1f);
    }

    public static void reject(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.IRON_DOOR.CLOSE", "BLOCK_IRON_DOOR_CLOSE"), 1f, 0.9f);
    }

    public static void importPainting(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 0.7f, 1f);
    }

    public static void openBook(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.7f);
    }

    public static void delete(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.CLOSE", "BLOCK_CHEST_CLOSE"), 0.5f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.ENDER_CHEST.CLOSE", "BLOCK_ENDER_CHEST_CLOSE"), 0.8f, 0.6f);
    }

    public static void movePainting(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 1f, 1f);
    }

    public static void download(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN"), 1f, 0.8f);
    }

    public static void purchase(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.YES", "ENTITY_VILLAGER_YES"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void sell(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.YES", "ENTITY_VILLAGER_YES"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP"), 1f, 1f);
    }

    public static void like(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.FIREWORK_ROCKET.TWINKLE_FAR", "ENTITY_FIREWORK_ROCKET_TWINKLE_FAR"), 1f, 0.7f);
    }

    public static void dislike(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE"), 1f, 0.7f);
    }

    public static void changePage(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.7f);
    }

    public static void change(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.LEVER.CLICK", "BLOCK_LEVER_CLICK"), 0.5f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.ARMOR_STAND.HIT", "ENTITY_ARMOR_STAND_HIT", "ENTITY_ARMORSTAND_HIT"), 1f, 0.7f);
    }

    public static void installation(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.WORK_TOOLSMITH", "ENTITY_VILLAGER_WORK_TOOLSMITH"), 1f, 0.7f);
    }

    public static void startDrawing(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.CLOSE", "BLOCK_BARREL_CLOSE"), 0.7f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void changeTool(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.WORK_TOOLSMITH", "ENTITY_VILLAGER_WORK_TOOLSMITH"), 0.7f, 0.7f);
    }

    public static void rollBack(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.FISHING_BOBBER.RETRIEVE", "ENTITY_FISHING_BOBBER_RETRIEVE"), 1f, 0.3f);
    }

    public static void preview(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.AMETHYST_BLOCK.CHIME", "BLOCK_AMETHYST_BLOCK_CHIME"), 2f, 1f);
    }

    public static void no(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO"), 1f, 1f);
    }

    public static void noPermission(@NotNull Player player) {
        no(player);
    }

    public static void openPage(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.LEVER.CLICK", "BLOCK_LEVER_CLICK"), 0.8f, 1f);
    }

    public static void step(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.METAL.STEP", "BLOCK_METAL_STEP"), 0.8f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.WOOL.BREAK", "BLOCK_WOOL_BREAK", "DIG_WOOL", "BLOCK_CLOTH_BREAK"), 0.3f, 0.8f);
    }

    public static void victory(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.CHALLENGE_COMPLETE", "UI_TOAST_CHALLENGE_COMPLETE"), 0.3f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 0.3f, 1f);
    }

    public static void jackpot(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.CHALLENGE_COMPLETE", "UI_TOAST_CHALLENGE_COMPLETE"), 0.3f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 0.3f, 1f);
    }

    public static void bonus(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.ITEM.PICKUP", "ENTITY_ITEM_PICKUP"), 0.8f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.ZOMBIE.INFECT", "ENTITY_ZOMBIE_INFECT", "ZOMBIE_INFECT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE", "ENTITY_WITCH_AMBIENT"), 0.7f, 0.5f);
    }

    public static void hit(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ITEM.TRIDENT.HIT", "ITEM_TRIDENT_HIT", "ENTITY_ARROW_HIT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("ITEM.TRIDENT.HIT_GROUND", "ITEM_TRIDENT_HIT_GROUND", "BLOCK_ANVIL_HIT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE", "ENTITY_WITCH_AMBIENT"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.IN", "UI_TOAST_IN"), 1f, 1f);
    }

    public static void lost(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.BLAZE.DEATH", "ENTITY_BLAZE_DEATH"), 0.7f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 0.8f, 0.5f);
        player.playSound(player.getLocation(), getSound("ENTITY.ZOMBIE.DEATH", "ENTITY_ZOMBIE_DEATH"), 0.5f, 0.7f);
    }

    public static void bet(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.HORSE.BREATHE", "ENTITY_HORSE_BREATHE", "ENTITY_HORSE_BREATHE"), 0.8f, 0.8f);
        player.playSound(player.getLocation(), getSound("BLOCK.BARREL.OPEN", "BLOCK_BARREL_OPEN", "BLOCK_CHEST_OPEN"), 1f, 0.8f);
        player.playSound(player.getLocation(), getSound("UI.LOOM.SELECT_PATTERN", "UI_LOOM_SELECT_PATTERN", "UI_TOAST_IN"), 1f, 1f);
    }

    public static void giveBet(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN", "CHEST_OPEN", "ENTITY_CHEST_OPEN"), 1f, 1f);
    }

    public static void activate(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("ENTITY.ITEM.PICKUP", "ENTITY_ITEM_PICKUP"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("UI.TOAST.IN", "UI_TOAST_IN"), 1f, 1f);
        player.playSound(player.getLocation(), getSound("BLOCK.STONE_BUTTON.CLICK_ON", "BLOCK_STONE_BUTTON_CLICK_ON", "ENTITY_ITEM_PICKUP"), 1f, 1f);
    }

    public static void explode(@NotNull Player player) {
        player.playSound(player.getLocation(), getSound("UI.TOAST.IN", "UI_TOAST_IN"), 1f, 0.4f);
        player.playSound(player.getLocation(), getSound("ENTITY.GENERIC.EXPLODE", "ENTITY_GENERIC_EXPLODE", "ENTITY_DRAGON_FIREBALL_EXPLODE"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.WITCH.CELEBRATE", "ENTITY_WITCH_CELEBRATE", "ENTITY_WITCH_AMBIENT"), 0.8f, 0.5f);
    }

    public static void timerPreStart(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.EXPERIENCE_ORB.PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1f, 0.9f);
    }

    public static void timerStart(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.FIREWORK_ROCKET.LAUNCH", "ENTITY_FIREWORK_ROCKET_LAUNCH", "ENTITY_ENDERDRAGON_HURT"), 1f, 0.8f);
    }

    public static void horseMove(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.HORSE.GALLOP", "ENTITY_HORSE_GALLOP"), 1f, 0.9f);
    }

    public static void notGameSlot(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.GRAVEL.FALL", "BLOCK_GRAVEL_FALL"), 1f, 1f);
    }

    public static void doubleBet(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.ZOMBIE_VILLAGER.CURE", "ENTITY_ZOMBIE_VILLAGER_CURE", "ENTITY_ZOMBIE_DEATH"), 1f, 1f);
    }

    public static void giveCards(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ITEM.BOOK.PAGE_TURN", "ITEM_BOOK_PAGE_TURN", "ENTITY_EGG_THROW"), 1f, 0.9f);
    }

    public static void leave(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.WOODEN_DOOR.CLOSE", "BLOCK_WOODEN_DOOR_CLOSE", "BLOCK_WOODEN_DOOR_CLOSE"), 0.5f, 1f);
    }

    public static void draw(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.NO", "ENTITY_VILLAGER_NO", "VILLAGER_NO"), 1f, 0.9f);
        player.playSound(player.getLocation(), getSound("ENTITY.WOLF.SHAKE", "ENTITY_WOLF_SHAKE", "WOLF_SHAKE"), 1f, 0.9f);
    }

    public static void openChest(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.CHEST.OPEN", "BLOCK_CHEST_OPEN"), 0.7f, 1f);
    }

    public static void restriction(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.ANVIL.FALL", "BLOCK_ANVIL_FALL"), 1f, 0.9f);
    }

    public static void laugh(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("ENTITY.VILLAGER.CELEBRATE", "ENTITY_VILLAGER_CELEBRATE", "ENTITY_VILLAGER_AMBIENT"), 1f, 0.9f);
    }

    public static void raise(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.ANVIL.PLACE", "BLOCK_ANVIL_PLACE"), 1f, 0.9f);
    }

    public static void shake(@NotNull Player player){
        player.playSound(player.getLocation(), getSound("BLOCK.WOOD.HIT", "BLOCK_WOOD_HIT"), 1f, 0.9f);
    }

}
