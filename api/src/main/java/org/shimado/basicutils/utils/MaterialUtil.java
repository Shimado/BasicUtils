package org.shimado.basicutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.BasicUtils;
import org.shimado.basicutils.enums.ERecords;

import java.util.Arrays;
import java.util.List;

public class MaterialUtil {

    private static final boolean isLegacy = BasicUtils.getVersionControl().isLegacy();
    private static final List<String> colors = Arrays.asList("WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK");

    @NotNull
    public static Material getMaterial(@NotNull String[] materials) {
        for (String material : materials) {
            if (Material.getMaterial(material) != null) {
                return Material.getMaterial(material);
            }
        }
        return Material.STONE;
    }


    public static boolean isPlayerHead(@NotNull ItemStack item){
        if(isLegacy) return item.getType().toString().equals("SKULL_ITEM") && item.getData().getData() == 3;
        return item.getType().toString().equals("PLAYER_HEAD");
    }


    @NotNull
    public static ItemStack getHead(){
        if(isLegacy) return new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);
        return new ItemStack(getMaterial((new String[]{"PLAYER_HEAD"})));
    }


    @NotNull
    public static ItemStack getSunflower(){
        return new ItemStack(getMaterial((new String[]{"SUNFLOWER", "DOUBLE_PLANT"})));
    }


    @NotNull
    public static ItemStack getFireWorks(){
        return new ItemStack(getMaterial((new String[]{"FIREWORKS", "FIREWORK", "FIREWORK_ROCKET"})));
    }


    @Nullable
    public static ItemStack getConcrete(@NotNull String color){
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
    public static ItemStack getWool(@NotNull String color){
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
    public static ItemStack getDye(@NotNull String color){
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
    public static ItemStack getCarpet(@NotNull String color){
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
    public static ItemStack getGlassPane(@NotNull String color){
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
    public static ItemStack getDoor(@NotNull String name){
        if(isLegacy){
            return new ItemStack(getMaterial((new String[]{"IRON_DOOR", "TNT"})));
        }
        return new ItemStack(getMaterial((new String[]{name, "WOODEN_DOOR", "TNT"})));
    }


    @Nullable
    public static ItemStack getRecord(@NotNull String recordString){
        ERecords record = ERecords.findByName(recordString);
        return record == null ? null : new ItemStack(record.getMaterial());
    }


    @NotNull
    public static ItemStack getItemByName(@NotNull String name) {

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

        if(name.contains("CLOCK")){
            return new ItemStack(getMaterial((new String[]{"CLOCK", "WATCH"})));
        }

        if(name.contains("IRON_BARS")){
            return new ItemStack(getMaterial((new String[]{"IRON_BARS", "IRON_FENCE"})));
        }

        if(isLegacy && (name.endsWith("_HEAD") || name.endsWith("_SKULL"))){
            short type = -1;
            switch (name){
                case "ZOMBIE_HEAD": type = 2; break;
                case "SKELETON_HEAD": type = 0; break;
                case "WITHER_SKELETON_SKULL": type = 1; break;
                case "CREEPER_HEAD": type = 4; break;
                case "DRAGON_HEAD": type = 5; break;
            }
            if(type != -1){
                return new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, type);
            }
        }

        if(name.contains("_CARPET")){
            return getCarpet(name.replace("_CARPET", ""));
        }

        if(name.equals("LIGHT_WEIGHTED_PRESSURE_PLATE")){
            return new ItemStack(getMaterial((new String[]{"GOLD_PLATE", "LIGHT_WEIGHTED_PRESSURE_PLATE"})));
        }

        if(name.equals("HEAVY_WEIGHTED_PRESSURE_PLATE")){
            return new ItemStack(getMaterial((new String[]{"IRON_PLATE", "HEAVY_WEIGHTED_PRESSURE_PLATE"})));
        }

        if(name.endsWith("_HORSE_ARMOR")){
            String type = name.replace("_HORSE_ARMOR", "");
            switch (type){
                case "IRON": return new ItemStack(getMaterial((new String[]{"IRON_HORSE_ARMOR", "IRON_BARDING"})));
                case "GOLDEN": return new ItemStack(getMaterial((new String[]{"GOLDEN_HORSE_ARMOR", "GOLD_BARDING"})));
                case "DIAMOND": return new ItemStack(getMaterial((new String[]{"DIAMOND_HORSE_ARMOR", "DIAMOND_BARDING"})));
                default: return new ItemStack(getMaterial((new String[]{name, "IRON_BARDING"})));
            }
        }

        if(name.contains("MUSIC_DISC_")){
            return getRecord(name);
        }

        if(name.contains("OAK_DOOR")){
            return getDoor(name);
        }

        try {
            return new ItemStack(Material.getMaterial(name));
        }catch (Exception e){
            return new ItemStack(Material.APPLE);
        }
    }

}
