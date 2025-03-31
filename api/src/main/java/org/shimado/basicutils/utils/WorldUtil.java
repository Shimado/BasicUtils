package org.shimado.basicutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.List;

public class WorldUtil {

    public static World checkIfWorldExistsAndLoad(String worldName){
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

}
