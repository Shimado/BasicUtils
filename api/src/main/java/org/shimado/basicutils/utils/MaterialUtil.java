package org.shimado.basicutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.shimado.basicutils.BasicUtils;

import java.util.Arrays;
import java.util.List;

public class MaterialUtil {

    private static List<String> colors = Arrays.asList("WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK");

    public static Material getMaterial(String[] materials) {
        for (String material : materials) {
            if (Material.getMaterial(material) != null) {
                return Material.getMaterial(material);
            }
        }
        return Material.STONE;
    }


    public static ItemStack getHead(){
        if(BasicUtils.getVersionControl().isLegacy()) return new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);
        return new ItemStack(getMaterial((new String[]{"PLAYER_HEAD"})));
    }

    public static ItemStack getSunflower(){
        return new ItemStack(getMaterial((new String[]{"SUNFLOWER", "DOUBLE_PLANT"})));
    }

    public static ItemStack getFireWorks(){
        return new ItemStack(getMaterial((new String[]{"FIREWORKS", "FIREWORK", "FIREWORK_ROCKET"})));
    }

    public static ItemStack getGoldPlate(){
        return new ItemStack(getMaterial((new String[]{"GOLD_PLATE", "LIGHT_WEIGHTED_PRESSURE_PLATE"})));
    }

    public static ItemStack getIronPlate(){
        return new ItemStack(getMaterial((new String[]{"IRON_PLATE", "HEAVY_WEIGHTED_PRESSURE_PLATE"})));
    }

    public static ItemStack getConcrete(String color){
        if(BasicUtils.getVersionControl().isLegacy()){
            return new ItemStack(Material.getMaterial("CONCRETE"), 1, (short) colors.indexOf(color));
        }else{
            return new ItemStack(Material.getMaterial(color + "_CONCRETE"));
        }
    }

    public static ItemStack getWool(String color){
        if(BasicUtils.getVersionControl().isLegacy()){
            return new ItemStack(Material.getMaterial("WOOL"), 1, (short) colors.indexOf(color));
        }else{
            return new ItemStack(Material.getMaterial(color + "_WOOL"));
        }
    }

    public static ItemStack getDye(String color){
        if(BasicUtils.getVersionControl().isLegacy()){
            return new ItemStack(Material.getMaterial("INK_SACK"), 1, (short) colors.indexOf(color));
        }else{
            return new ItemStack(Material.getMaterial(color + "_DYE"));
        }
    }

    public static ItemStack getCarpet(String color){
        if(BasicUtils.getVersionControl().isLegacy()){
            return new ItemStack(Material.getMaterial("CARPET"), 1, (short) colors.indexOf(color));
        }else{
            return new ItemStack(Material.getMaterial(color + "_CARPET"));
        }
    }

    public static ItemStack getGlassPane(String color){
        if(BasicUtils.getVersionControl().isLegacy()){
            return new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) colors.indexOf(color));
        }else{
            return new ItemStack(Material.getMaterial(color + "_STAINED_GLASS_PANE"));
        }
    }


    public static ItemStack getItemByName(String name) {

        if(name.contains("PLAYER_HEAD")){
            return getHead();
        }

        if(name.contains("SUNFLOWER")){
            return getSunflower();
        }

        if(name.contains("_STAINED_GLASS_PANE")){
            return getGlassPane(name.replace("_STAINED_GLASS_PANE", "").toUpperCase());
        }

        if(name.contains("_CONCRETE")){
            return getConcrete(name.split("_")[0].toUpperCase());
        }

        if(name.contains("_WOOL")){
            return getWool(name.split("_")[0].toUpperCase());
        }

        if(name.contains("_DYE")){
            return getDye(name.replace("_DYE", "").toUpperCase());
        }

        if(name.contains("SUNFLOWER")){
            return getSunflower();
        }

        if(name.contains("_CARPET")){
            return getCarpet(name.split("_")[0].toUpperCase());
        }

        if(name.equals("LIGHT_WEIGHTED_PRESSURE_PLATE")){
            return getGoldPlate();
        }

        if(name.equals("HEAVY_WEIGHTED_PRESSURE_PLATE")){
            return getIronPlate();
        }

        try {
            return new ItemStack(Material.getMaterial(name));
        }catch (Exception e){
            return new ItemStack(Material.APPLE);
        }
    }

}
