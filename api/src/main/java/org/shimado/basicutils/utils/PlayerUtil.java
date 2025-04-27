package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerUtil {

    private final static List<String> END_MATERIALS = List.of(
            "AIR",
            "_PLATE",
            "_BUTTON",
            "_CARPET",
            "_BANNER",
            "SNOW",
            "_MOSS",
            "_SAPLING",
            "_MUSHROOM",
            "_FUNGUS",
            "SCULK_VEIN",
            "POPPY",
            "DANDELION",
            "GRASS",
            "FERN",
            "BUSH",
            "ORCHID",
            "ALLIUM",
            "_BLUET",
            "_TULIP",
            "BLOSSOM",
            "_PETALS",
            "_ROSE",
            "_DAISY",
            "CORNFLOWER",
            "LILY_OF_THE_VALLEY",
            "TORCHFLOWER",
            "_CANE",
            "ROOTS",
            "_SPROUTS",
            "_VINES",
            "SUNFLOWER",
            "LILAC",
            "PEONY",
            "PITCHER_PLANT",
            "LICHEN",
            "KELP",
            "_CORAL",
            "_CORAL_FAN",
            "_CORAL_WALL_FAN",
            "NETHER_WART",
            "WHEAT",
            "POTATOES",
            "CARROTS",
            "TORCH",
            "ITEM_FRAME",
            "ARMOR_STAND",
            "_SIGN",
            "REDSTONE",
            "REDSTONE_WIRE",
            "REPEATER",
            "COMPARATOR",
            "STRING",
            "TRIPWIRE_HOOK",
            "RAIL"
    );


    public static boolean isPlayerOnline(UUID playerUUID){
        Player player = Bukkit.getPlayer(playerUUID);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerOnline(String playerName){
        Player player = Bukkit.getPlayer(playerName);
        return player != null && player.isOnline();
    }


    public static boolean isPlayerVanished(Player player){
        return player.getMetadata("vanished").stream().anyMatch(m -> m.asBoolean());
    }

}
