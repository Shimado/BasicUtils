package org.shimado.basicutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.shimado.basicutils.BasicUtils;
import org.shimado.basicutils.enums.ERecords;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class MaterialUtil {

    private static final boolean isLegacy = BasicUtils.getVersionControl().isLegacy();
    private static final List<String> colors = Arrays.asList("WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK");

    @Nonnull
    public static Material getMaterial(@Nonnull String[] materials) {
        for (String material : materials) {
            if (Material.getMaterial(material) != null) {
                return Material.getMaterial(material);
            }
        }
        return Material.STONE;
    }


    @Nonnull
    public static ItemStack getHead(){
        if(isLegacy) return new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);
        return new ItemStack(getMaterial((new String[]{"PLAYER_HEAD"})));
    }

    @Nonnull
    public static ItemStack getSunflower(){
        return new ItemStack(getMaterial((new String[]{"SUNFLOWER", "DOUBLE_PLANT"})));
    }

    @Nonnull
    public static ItemStack getFireWorks(){
        return new ItemStack(getMaterial((new String[]{"FIREWORKS", "FIREWORK", "FIREWORK_ROCKET"})));
    }

    @Nonnull
    public static ItemStack getGoldPlate(){
        return new ItemStack(getMaterial((new String[]{"GOLD_PLATE", "LIGHT_WEIGHTED_PRESSURE_PLATE"})));
    }

    @Nonnull
    public static ItemStack getIronPlate(){
        return new ItemStack(getMaterial((new String[]{"IRON_PLATE", "HEAVY_WEIGHTED_PRESSURE_PLATE"})));
    }

    @Nullable
    public static ItemStack getConcrete(@Nonnull String color){
        if(colors.contains(color.toUpperCase())){
            if(isLegacy){
                return new ItemStack(Material.getMaterial("CONCRETE"), 1, (short) colors.indexOf(color.toUpperCase()));
            }else{
                return new ItemStack(Material.getMaterial(color.toUpperCase() + "_CONCRETE"));
            }
        }
        return null;
    }

    @Nullable
    public static ItemStack getWool(@Nonnull String color){
        if(colors.contains(color.toUpperCase())){
            if(isLegacy){
                return new ItemStack(Material.getMaterial("WOOL"), 1, (short) colors.indexOf(color.toUpperCase()));
            }else{
                return new ItemStack(Material.getMaterial(color.toUpperCase() + "_WOOL"));
            }
        }
        return null;
    }

    @Nullable
    public static ItemStack getDye(@Nonnull String color){
        if(colors.contains(color.toUpperCase())){
            if(isLegacy){
                return new ItemStack(Material.getMaterial("INK_SACK"), 1, (short) colors.indexOf(color.toUpperCase()));
            }else{
                return new ItemStack(Material.getMaterial(color.toUpperCase() + "_DYE"));
            }
        }
        return null;
    }

    @Nullable
    public static ItemStack getCarpet(@Nonnull String color){
        if(colors.contains(color.toUpperCase())){
            if(isLegacy){
                return new ItemStack(Material.getMaterial("CARPET"), 1, (short) colors.indexOf(color.toUpperCase()));
            }else{
                return new ItemStack(Material.getMaterial(color.toUpperCase() + "_CARPET"));
            }
        }
        return null;
    }

    @Nullable
    public static ItemStack getGlassPane(@Nonnull String color){
        if(colors.contains(color.toUpperCase())){
            if(isLegacy){
                return new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) colors.indexOf(color.toUpperCase()));
            }else{
                return new ItemStack(Material.getMaterial(color.toUpperCase() + "_STAINED_GLASS_PANE"));
            }
        }
        return null;
    }

    @Nullable
    public static ItemStack getRecord(@Nonnull String recordString){
        ERecords record = ERecords.findByName(recordString);
        return record == null ? null : new ItemStack(record.getMaterial());
    }


    @Nonnull
    public static ItemStack getItemByName(@Nonnull String name) {

        if(name.contains("PLAYER_HEAD")){
            return getHead();
        }

        if(name.contains("SUNFLOWER")){
            return getSunflower();
        }

        if(name.contains("_STAINED_GLASS_PANE")){
            return getGlassPane(name.replace("_STAINED_GLASS_PANE", ""));
        }

        if(name.contains("_CONCRETE")){
            return getConcrete(name.replace("_CONCRETE", ""));
        }

        if(name.contains("_WOOL")){
            return getWool(name.replace("_WOOL", ""));
        }

        if(name.contains("_DYE")){
            return getDye(name.replace("_DYE", ""));
        }

        if(name.contains("SUNFLOWER")){
            return getSunflower();
        }

        if(name.contains("_CARPET")){
            return getCarpet(name.replace("_CARPET", ""));
        }

        if(name.equals("LIGHT_WEIGHTED_PRESSURE_PLATE")){
            return getGoldPlate();
        }

        if(name.equals("HEAVY_WEIGHTED_PRESSURE_PLATE")){
            return getIronPlate();
        }

        if(name.contains("MUSIC_DISC_")){
            return getRecord(name);
        }

        try {
            return new ItemStack(Material.getMaterial(name));
        }catch (Exception e){
            return new ItemStack(Material.APPLE);
        }
    }

}
