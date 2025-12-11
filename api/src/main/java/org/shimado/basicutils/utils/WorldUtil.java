package org.shimado.basicutils.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.shimado.basicutils.BasicUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class WorldUtil {

    @Nullable
    public static World checkIfWorldExistsAndLoad(@Nonnull String worldName){
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


    @Nonnull
    public static Location getRandomLocInRadius(@Nonnull Location loc, double radius) {
        return loc.clone().add(
                Math.random() * (NumberUtil.getChance(50.0) ? radius : -radius),
                Math.random() * (NumberUtil.getChance(50.0) ? radius : -radius),
                Math.random() * (NumberUtil.getChance(50.0) ? radius : -radius));
    }


    public static void spawnFireWorks(@Nonnull List<Player> playersToShow, @Nonnull Location spawnLoc, @Nonnull FireworkEffect.Type type, int power, @Nonnull List<Color> colors){
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

        playersToShow.forEach(p -> BasicUtils.getVersionControl().getVersionControl().createFirework(p, spawnLoc, firework));
    }

}
