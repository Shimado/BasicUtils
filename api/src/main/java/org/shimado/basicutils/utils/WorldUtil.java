package org.shimado.basicutils.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.BasicUtils;

import java.io.File;
import java.util.List;

public class WorldUtil {

    @Nullable
    public static World checkIfWorldExistsAndLoad(@NotNull String worldName){
        World world = Bukkit.getWorld(worldName);
        if(world == null){
            for(File file : Bukkit.getServer().getWorldContainer().listFiles()){
                if(file.getName().equals(worldName) && List.of("world", "world_nether", "world_the_end").stream().noneMatch(it -> file.getName().equals(it)) && file.isDirectory() && new File(file, "level.dat").exists()){
                    return Bukkit.getServer().createWorld(new WorldCreator(worldName));
                }
            }
        }
        return world;
    }


    @NotNull
    public static Location getRandomLocInRadius(@NotNull Location loc, double radius) {
        return loc.clone().add(
                Math.random() * (NumberUtil.getChance(50.0) ? radius : -radius),
                Math.random() * (NumberUtil.getChance(50.0) ? radius : -radius),
                Math.random() * (NumberUtil.getChance(50.0) ? radius : -radius));
    }


    public static void spawnFireWorks(@NotNull List<Player> playersToShow, @NotNull Location spawnLoc, @NotNull FireworkEffect.Type type, int power, @NotNull List<Color> colors){
        ItemStack firework = MaterialUtil.getFireWorks();
        FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(false)
                .withColor(colors)
                .with(type)
                .build();
        meta.addEffect(effect);
        meta.setPower(power);
        firework.setItemMeta(meta);

        playersToShow.forEach(p -> BasicUtils.getVersionControl().getVersionControl().createFirework(playersToShow, spawnLoc, firework));
    }

}
