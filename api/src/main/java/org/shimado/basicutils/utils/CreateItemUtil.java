package org.shimado.basicutils.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.BasicUtils;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreateItemUtil {

    private static final boolean isCustomModelData = BasicUtils.getVersionControl().isCustomModelData();
    private static final boolean isItemModel = BasicUtils.getVersionControl().isItemModel();
    private static final boolean isGlowingAndHiddenNamesUpdated = BasicUtils.getVersionControl().isGlowingAndHiddenNamesUpdated();
    private static final boolean isHeadMetaUpdated = BasicUtils.getVersionControl().isHeadMetaUpdated();
    private static final ItemFlag[] itemFlags = Arrays.stream(new String[]{"HIDE_ENCHANTS", "HIDE_ATTRIBUTES", "HIDE_UNBREAKABLE", "HIDE_DESTROYS", "HIDE_PLACED_ON", "HIDE_ADDITIONAL_TOOLTIP", "HIDE_DYE", "HIDE_ARMOR_TRIM", "HIDE_CUSTOM_DATA"})
            .map(it -> {
                try {
                    return ItemFlag.valueOf(it);
                }catch (Exception e){
                    return null;
                }
            }).filter(it -> it != null)
            .toArray(ItemFlag[]::new);


    private static Inventory convertInv = Bukkit.createInventory(null, 9, "BasicTestInventory");

    @NotNull
    public static ItemStack create(@NotNull Object materialOrHeadURL, @NotNull String displayName, @NotNull List<String> lore, boolean glowing, @NotNull Object modelData, boolean hideNames) {
        ItemStack item = getItemStackFrom(materialOrHeadURL).clone();
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
        meta.setDisplayName(ColorUtil.getColor(displayName));
        meta.setLore(ColorUtil.getColorList(lore));

        if(isGlowingAndHiddenNamesUpdated){
            meta.setEnchantmentGlintOverride(glowing);
            meta.setHideTooltip(hideNames || displayName.isEmpty() || displayName.equals(" "));
        }else{
            if(glowing) meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        }

        // Custom model data
        if(modelData instanceof Integer && isCustomModelData && (int) modelData > 0){
            meta.setCustomModelData((int) modelData);
        }

        // Item model
        else if(modelData instanceof String && isItemModel && ((String) modelData).length() > 0){
            String modelDataString = (String) modelData;
            if(modelDataString.contains(":")){
                String[] arr = modelDataString.split(":");
                meta.setItemModel(new NamespacedKey(arr[0], arr[1]));
            }
        }

        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
        convertInv.setItem(0, item);

        return convertInv.getItem(0);
    }


    @NotNull
    public static ItemStack create(@NotNull Object materialOrHeadURL, @NotNull String displayName, @NotNull List<String> lore, boolean glowing, @NotNull Object modelData, boolean hideNames, @NotNull String NBTTag, @NotNull String NBTTagValue){
        ItemStack item = create(materialOrHeadURL, displayName, lore, glowing, modelData, hideNames);
        convertInv.setItem(0, BasicUtils.getVersionControl().getVersionControl().createItemWithTag(item, NBTTag, NBTTagValue));
        return convertInv.getItem(0);
    }


    @NotNull
    public static ItemStack getItemStackFrom(@NotNull Object materialOrHeadURL){
        if(materialOrHeadURL instanceof String && ((String) materialOrHeadURL).length() > 30){
            return getSkull((String) materialOrHeadURL);
        }
        else if(materialOrHeadURL instanceof String && ((String) materialOrHeadURL).length() < 30){
            String materialRaw = ((String) materialOrHeadURL).toUpperCase();
            String regex = "^LEATHER_[A-Z_]+\\[#[0-9A-Fa-f]{6}\\]$";

            if(Pattern.compile(regex).matcher(materialRaw).matches()){
                String materialName = materialRaw.replaceAll("\\[#[0-9A-Fa-f]{6}\\]$", "");
                String hexColor = materialRaw
                        .replaceAll("^LEATHER_[A-Z_]+\\[", "")
                        .replaceAll("\\]$", "");
                ItemStack item = new ItemStack(Material.getMaterial(materialName));
                LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

                int[] arr = ColorUtil.hexToRgbArray(hexColor);
                meta.setColor(Color.fromRGB(arr[0], arr[1], arr[2]));
                item.setItemMeta(meta);
                return item;
            }

            return MaterialUtil.getItemByName(materialRaw);
        }
        else if(materialOrHeadURL instanceof Material){
            return new ItemStack((Material) materialOrHeadURL);
        }
        else if(materialOrHeadURL instanceof ItemStack){
            return (ItemStack) materialOrHeadURL;
        }
        else return new ItemStack(Material.STONE);
    }


    @NotNull
    public static GameProfile getGameProfile(@NotNull UUID uuid, @NotNull String url){
        GameProfile profile = new GameProfile(uuid, "");
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        return profile;
    }


    @NotNull
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
    public static String getPlayerHeadBase64(@NotNull Player player) {
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


    @NotNull
    public static ItemStack getSkull(@NotNull String url) {
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


    @NotNull
    public static ItemStack getHeadOfPlayerOnTheServer(@NotNull UUID playerUUID){
        ItemStack item = MaterialUtil.getHead();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerUUID);
        if(offPlayer == null) return item;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        item.setItemMeta(meta);
        return item;
    }


    @NotNull
    public static ItemStack getHeadOfPlayerOnTheServerWithCustomModelData(@NotNull UUID playerUUID, @NotNull Object modelData){
        ItemStack item = MaterialUtil.getHead();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerUUID);
        if(offPlayer == null) return item;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));

        // Custom model data
        if(modelData instanceof Integer && isCustomModelData && (int) modelData > 0){
            meta.setCustomModelData((int) modelData);
        }

        // Item model
        else if(modelData instanceof String && isItemModel && ((String) modelData).length() > 0){
            String modelDataString = (String) modelData;
            if(modelDataString.contains(":")){
                String[] arr = modelDataString.split(":");
                meta.setItemModel(new NamespacedKey(arr[0], arr[1]));
            }
        }

        item.setItemMeta(meta);
        return item;
    }


    @NotNull
    public static ItemStack getCloneAmount(@NotNull ItemStack itemToClone, int amount){
        ItemStack item = itemToClone.clone();
        item.setAmount(amount);
        return item;
    }


    @NotNull
    public static ItemStack getCloneAmount1(@NotNull ItemStack itemToClone){
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


    @NotNull
    public static String getItemTag(@NotNull ItemStack item, @NotNull String tag){
        return BasicUtils.getVersionControl().getVersionControl().getTag(item, tag);
    }

    @NotNull
    public static ItemStack setTag(@NotNull ItemStack item, @NotNull String tag, @NotNull String value){
        return BasicUtils.getVersionControl().getVersionControl().createItemWithTag(item, tag, value);
    }


    @Nullable
    public static ItemStack replaceItemPlaceholders(boolean clone, @Nullable ItemStack item, @NotNull String displayName, @NotNull List<String> lore, @NotNull Map<String, String> placeholders){
        if(item == null) return null;

        String newTitle = displayName;
        List<String> newLore = new ArrayList<>(lore);

        for(Map.Entry<String, String> a : placeholders.entrySet()){
            newTitle = newTitle.replace(a.getKey(), a.getValue());
            newLore = newLore.stream().map(it -> it.replace(a.getKey(), a.getValue())).collect(Collectors.toList());
        }

        ItemStack itemToEdit = clone ? item.clone() : item;
        ItemMeta meta = itemToEdit.getItemMeta();
        meta.setDisplayName(ColorUtil.getColor(newTitle));
        meta.setLore(ColorUtil.getColorList(newLore));
        itemToEdit.setItemMeta(meta);
        return itemToEdit;
    }

}
