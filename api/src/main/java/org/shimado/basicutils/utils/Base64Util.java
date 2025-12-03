package org.shimado.basicutils.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Util {

    @Nullable
    public static String encodeImage(@Nonnull BufferedImage image){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return null;
        }
    }


    @Nullable
    public static BufferedImage decodeImage(@Nonnull Object image64){
        BufferedImage image = null;
        if(image64 != null){
            byte[] imageBytes = Base64.getDecoder().decode((String) image64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            try {
                image = ImageIO.read(bais);
            } catch (IOException e) {
                return null;
            }
        }
        return image;
    }


    @Nullable
    public static String encodeItemStack(@Nonnull ItemStack itemStack){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }


    @Nullable
    public static ItemStack decodeItemStack(@Nonnull String itemStack64){
        byte[] bytes = Base64.getDecoder().decode(itemStack64);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (Exception e) {
            return null;
        }
    }

}
