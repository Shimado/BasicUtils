package org.shimado.basicutils.utils;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.shimado.basicutils.BasicUtils;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class CreateItemUtil {

    private static boolean is1_21_update = BasicUtils.getVersionControl().is1_21_update();
    private static boolean isHeadMetaUpdated = BasicUtils.getVersionControl().isHeadMetaUpdated();

    private static final ItemFlag[] itemFlags = Arrays.stream(new String[]{"HIDE_ENCHANTS", "HIDE_ATTRIBUTES", "HIDE_UNBREAKABLE", "HIDE_DESTROYS", "HIDE_PLACED_ON", "HIDE_ADDITIONAL_TOOLTIP", "HIDE_DYE", "HIDE_ARMOR_TRIM"})
            .map(it -> {
                try {
                    return ItemFlag.valueOf(it);
                }catch (Exception e){
                    return null;
                }
            }).filter(it -> it != null)
            .toArray(ItemFlag[]::new);


    public static ItemStack create(Object material, String name, List<String> lore, Boolean enchant, int modelData, boolean hideNames) {
        ItemStack item = getItemStackFrom(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtil.getColor(name));
        meta.setLore(ColorUtil.getColorList(lore));

        if(is1_21_update){
            if(enchant) meta.setEnchantmentGlintOverride(true);
            if(hideNames) meta.setHideTooltip(true);
        }else{
            if(enchant) meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        }

        if(modelData > 0){
            meta.setCustomModelData(modelData);
        }

        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
        return item;
    }


    private static ItemStack getItemStackFrom(Object material){
        if(material instanceof String && ((String) material).length() > 30){
            return getSkull((String) material);
        }
        else if(material instanceof String && ((String) material).length() < 30){
            return new ItemStack(Material.getMaterial((String) material));
        }
        else if(material instanceof Material){
            return new ItemStack((Material) material);
        }
        else{
            return (ItemStack) material;
        }
    }


    public static GameProfile getGameProfile(UUID uuid, String url){
        GameProfile profile = new GameProfile(uuid, "");
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        return profile;
    }


    public static ItemStack getSkull(String url) {
        if(!isHeadMetaUpdated){
            url = "http://textures.minecraft.net/texture/" + url;
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            GameProfile profile = getGameProfile(UUID.randomUUID(), url);
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            assert profileField != null;
            profileField.setAccessible(true);
            try {
                profileField.set(skullMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            skull.setItemMeta(skullMeta);
            return skull;
        }else{
            url = "http://textures.minecraft.net/texture/" + url;
            PlayerProfile profile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID(), "");
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL(url));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwnerProfile(profile);
            skull.setItemMeta(skullMeta);
            return skull;
        }
    }

}
