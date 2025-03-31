package org.shimado.basicutils.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {

    public static String encode(BufferedImage image){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return null;
        }
    }


    public static BufferedImage decode(Object image64){
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

}
