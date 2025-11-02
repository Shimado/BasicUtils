package org.shimado.basicutils.utils;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
import java.util.*;
import java.util.stream.Collectors;

public class CreateItemUtil {

    private static boolean isCustomModelData = BasicUtils.getVersionControl().isCustomModelData();
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


    private static Inventory convertInv = Bukkit.createInventory(null, 9, "BasicTestInventory");

    public static ItemStack create(Object material, String name, List<String> lore, Boolean enchant, int modelData, boolean hideNames) {
        ItemStack item = getItemStackFrom(material).clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtil.getColor(name));
        meta.setLore(ColorUtil.getColorList(lore));

        if(is1_21_update){
            if(enchant) meta.setEnchantmentGlintOverride(true);
            if(hideNames) meta.setHideTooltip(true);
        }else{
            if(enchant) meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        }

        if(modelData > 0 && isCustomModelData){
            meta.setCustomModelData(modelData);
        }

        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
        convertInv.setItem(0, item);
        return convertInv.getItem(0);
    }


    public static ItemStack create(Object material, String name, List<String> lore, Boolean enchant, int modelData, boolean hideNames, String NBTTag, String NBTTagValue){
        ItemStack item = create(material, name, lore, enchant, modelData, hideNames);
        convertInv.setItem(0, BasicUtils.getVersionControl().getVersionControl().createItemWithTag(item, NBTTag, NBTTagValue));
        return convertInv.getItem(0);
    }


    public static ItemStack getItemStackFrom(Object material){
        if(material instanceof String && ((String) material).length() > 30){
            return getSkull((String) material);
        }
        else if(material instanceof String && ((String) material).length() < 30){
            return MaterialUtil.getItemByName((String) material);
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


    public static ItemStack getHeadFromBase64(String base64) {
        ItemStack head = new ItemStack(MaterialUtil.getHead());
        if (base64 == null || base64.isEmpty()) return head;

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", base64));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(meta);
        return head;
    }


    public static String getPlayerHeadBase64(Player player) {
        try {
            GameProfile profile = BasicUtils.getVersionControl().getVersionControl().getGameProfile(player);
            Collection<Property> textures = profile.getProperties().get("textures");
            if (!textures.isEmpty()) {
                return textures.iterator().next().getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ItemStack getSkull(String url) {
        if(!isHeadMetaUpdated){
            url = "http://textures.minecraft.net/texture/" + url;
            ItemStack skull = MaterialUtil.getHead();
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


    public static ItemStack getHeadOfPlayerOnTheServer(UUID playerUUID){
        ItemStack item = MaterialUtil.getHead();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerUUID);
        if(offPlayer == null) return item;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        item.setItemMeta(meta);
        return item;
    }


    public static ItemStack getCloneAmount1(ItemStack itemToClone){
        ItemStack item = itemToClone.clone();
        item.setAmount(1);
        return item;
    }


    public static boolean isSameItems(ItemStack item1, ItemStack item2, String tag){
        if(item1 == null || item2 == null || item1.getType().equals(Material.AIR)) return false;
        ItemStack itemClone1 = getCloneAmount1(item1);
        ItemStack itemClone2 = getCloneAmount1(item2);
        if(tag != null && !tag.isEmpty()){
            String tag1 = BasicUtils.getVersionControl().getVersionControl().getTag(itemClone1, tag);
            String tag2 = BasicUtils.getVersionControl().getVersionControl().getTag(itemClone2, tag);
            return tag1 != null && tag2 != null && tag1.equals(tag2);
        }
        return itemClone1.equals(itemClone2);
    }


    public static String getItemTag(ItemStack item, String tag){
        return BasicUtils.getVersionControl().getVersionControl().getTag(item, tag);
    }


    public static ItemStack replaceItemPlaceholders(ItemStack item, String title, List<String> lore, Map<String, String> placeholders){
        if(item == null) return null;
        ItemStack itemToEdit = item.clone();
        ItemMeta meta = itemToEdit.getItemMeta();

        String newTitle = title;
        List<String> newLore = new ArrayList<>();

        for(Map.Entry<String, String> a : placeholders.entrySet()){
            if(newTitle != null){
                newTitle = newTitle.replace(a.getKey(), a.getValue());
            }
            if(lore != null){
                newLore = newLore.stream().map(it -> it.replace(a.getKey(), a.getValue())).collect(Collectors.toList());
            }
        }

        if(newTitle != null){
            meta.setDisplayName(ColorUtil.getColor(newTitle));
        }
        if(newLore != null){
            meta.setLore(ColorUtil.getColorList(newLore));
        }

        itemToEdit.setItemMeta(meta);
        return itemToEdit;
    }

}
