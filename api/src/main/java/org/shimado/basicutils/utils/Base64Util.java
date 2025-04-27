package org.shimado.basicutils.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Util {

    public static String encodeImage(BufferedImage image){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return null;
        }
    }


    public static BufferedImage decodeImage(Object image64){
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


    public static String encodeItemStack(ItemStack itemStack){
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


    public static ItemStack decodeItemStack(String itemStack64){
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
