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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CreateItemUtil {

    private static final boolean isCustomModelData = BasicUtils.getVersionControl().isCustomModelData();
    private static final boolean isGlowingAndHiddenNamesUpdated = BasicUtils.getVersionControl().isGlowingAndHiddenNamesUpdated();
    private static final boolean isHeadMetaUpdated = BasicUtils.getVersionControl().isHeadMetaUpdated();
    private static final ItemFlag[] itemFlags = Arrays.stream(new String[]{"HIDE_ENCHANTS", "HIDE_ATTRIBUTES", "HIDE_UNBREAKABLE", "HIDE_DESTROYS", "HIDE_PLACED_ON", "HIDE_ADDITIONAL_TOOLTIP", "HIDE_DYE", "HIDE_ARMOR_TRIM"})
            .map(it -> {
                try {
                    return ItemFlag.valueOf(it);
                }catch (Exception e){
                    return null;
                }
            }).filter(Objects::nonNull)
            .toArray(ItemFlag[]::new);


    private static Inventory convertInv = Bukkit.createInventory(null, 9, "BasicTestInventory");

    @Nonnull
    public static ItemStack create(@Nonnull Object materialOrHeadURL, @Nonnull String displayName, @Nonnull List<String> lore, boolean glowing, int customModelData, boolean hideNames) {
        ItemStack item = getItemStackFrom(materialOrHeadURL).clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtil.getColor(displayName));
        meta.setLore(ColorUtil.getColorList(lore));

        if(isGlowingAndHiddenNamesUpdated){
            if(glowing) meta.setEnchantmentGlintOverride(true);
            if(hideNames) meta.setHideTooltip(true);
        }else{
            if(glowing) meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        }

        if(customModelData > 0 && isCustomModelData){
            meta.setCustomModelData(customModelData);
        }

        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
        convertInv.setItem(0, item);
        return convertInv.getItem(0);
    }


    @Nonnull
    public static ItemStack create(@Nonnull Object materialOrHeadURL, @Nonnull String displayName, @Nonnull List<String> lore, boolean glowing, int customModelData, boolean hideNames, @Nonnull String NBTTag, @Nonnull String NBTTagValue){
        ItemStack item = create(materialOrHeadURL, displayName, lore, glowing, customModelData, hideNames);
        convertInv.setItem(0, BasicUtils.getVersionControl().getVersionControl().createItemWithTag(item, NBTTag, NBTTagValue));
        return convertInv.getItem(0);
    }


    @Nonnull
    public static ItemStack getItemStackFrom(@Nonnull Object materialOrHeadURL){
        if(materialOrHeadURL instanceof String && ((String) materialOrHeadURL).length() > 30){
            return getSkull((String) materialOrHeadURL);
        }
        else if(materialOrHeadURL instanceof String && ((String) materialOrHeadURL).length() < 30){
            return MaterialUtil.getItemByName((String) materialOrHeadURL);
        }
        else if(materialOrHeadURL instanceof Material){
            return new ItemStack((Material) materialOrHeadURL);
        }
        else{
            return (ItemStack) materialOrHeadURL;
        }
    }


    @Nonnull
    public static GameProfile getGameProfile(@Nonnull UUID uuid, @Nonnull String url){
        GameProfile profile = new GameProfile(uuid, "");
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        return profile;
    }


    @Nonnull
    public static ItemStack getHeadFromBase64(@Nullable String base64) {
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


    @Nullable
    public static String getPlayerHeadBase64(@Nonnull Player player) {
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


    @Nonnull
    public static ItemStack getSkull(@Nonnull String url) {
        url = "http://textures.minecraft.net/texture/" + url;

        if(!isHeadMetaUpdated){
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


    @Nonnull
    public static ItemStack getHeadOfPlayerOnTheServer(@Nonnull UUID playerUUID){
        ItemStack item = MaterialUtil.getHead();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerUUID);
        if(offPlayer == null) return item;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        item.setItemMeta(meta);
        return item;
    }


    @Nonnull
    public static ItemStack getCloneAmount(@Nonnull ItemStack itemToClone, int amount){
        ItemStack item = itemToClone.clone();
        item.setAmount(amount);
        return item;
    }


    @Nonnull
    public static ItemStack getCloneAmount1(@Nonnull ItemStack itemToClone){
        return getCloneAmount(itemToClone, 1);
    }


    public static boolean isSameItems(@Nullable ItemStack item1, @Nullable ItemStack item2, @Nullable String tag){
        if(item1 == null || item2 == null || item1.getAmount() <= 0 || item2.getAmount() <= 0) return false;
        ItemStack itemClone1 = getCloneAmount1(item1);
        ItemStack itemClone2 = getCloneAmount1(item2);
        if(tag != null && !tag.isEmpty()){
            String tag1 = BasicUtils.getVersionControl().getVersionControl().getTag(itemClone1, tag);
            String tag2 = BasicUtils.getVersionControl().getVersionControl().getTag(itemClone2, tag);
            return tag1.equals(tag2);
        }
        return itemClone1.equals(itemClone2);
    }


    @Nonnull
    public static String getItemTag(@Nonnull ItemStack item, @Nonnull String tag){
        return BasicUtils.getVersionControl().getVersionControl().getTag(item, tag);
    }


    @Nullable
    public static ItemStack replaceItemPlaceholders(@Nullable ItemStack item, @Nonnull String displayName, @Nonnull List<String> lore, @Nonnull Map<String, String> placeholders){
        if(item == null) return null;

        String newTitle = displayName;
        List<String> newLore = new ArrayList<>(lore);

        for(Map.Entry<String, String> a : placeholders.entrySet()){
            newTitle = newTitle.replace(a.getKey(), a.getValue());
            newLore = newLore.stream().map(it -> it.replace(a.getKey(), a.getValue())).collect(Collectors.toList());
        }

        ItemStack itemToEdit = item.clone();
        ItemMeta meta = itemToEdit.getItemMeta();
        meta.setDisplayName(ColorUtil.getColor(newTitle));
        meta.setLore(ColorUtil.getColorList(newLore));
        itemToEdit.setItemMeta(meta);
        return itemToEdit;
    }

}
