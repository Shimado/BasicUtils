package org.shimado.basicutils;

import org.bukkit.ChatColor;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ColorUtil {

    private static final Pattern pattern1 = Pattern.compile("(?<=#\\([a-fA-F0-9]{6}\\))(.*)(?=#\\([a-fA-F0-9]{6}\\))");
    private static final Pattern pattern2 = Pattern.compile("#[a-fA-F0-9]{6}");


    public static String getColor(String text) {
        Matcher matcher1 = pattern1.matcher(text);
        while (matcher1.find()){
            String fullMessage = text.substring(matcher1.start() - 9, matcher1.end() + 9); //#(FF00FF)Example#(FF00FF)
            String message = text.substring(matcher1.start(), matcher1.end()); //Example

            boolean bold = matcher1.start() - 11 >= 0 && text.substring(matcher1.start() - 11, matcher1.start() - 9).equals("&l");

            String color1 = text.substring(matcher1.start() - 9, matcher1.start()).replaceAll("[()]", "");
            String color2 = text.substring(matcher1.end(), matcher1.end() + 9).replaceAll("[()]", "");

            int[] colorArr1 = new int[]{
                    Integer.parseInt(color1.substring(1, 3), 16),
                    Integer.parseInt(color1.substring(3, 5), 16),
                    Integer.parseInt(color1.substring(5, 7), 16)
            };

            int[] colorArr2 = new int[]{
                    Integer.parseInt(color2.substring(1, 3), 16),
                    Integer.parseInt(color2.substring(3, 5), 16),
                    Integer.parseInt(color2.substring(5, 7), 16)
            };

            int[] differenceArr = new int[]{
                    (colorArr2[0] - colorArr1[0]) / message.length(),
                    (colorArr2[1] - colorArr1[1]) / message.length(),
                    (colorArr2[2] - colorArr1[2]) / message.length()
            };
            int[][] colorArr = new int[3][message.length()];

            for (int i = 0; i < message.length(); i++) {
                colorArr[0][i] = colorArr1[0] + differenceArr[0] * i;
                colorArr[1][i] = colorArr1[1] + differenceArr[1] * i;
                colorArr[2][i] = colorArr1[2] + differenceArr[2] * i;
            }

            String editedMessage = "";
            for (int i = 0; i < message.length(); i++) {
                float[] c = java.awt.Color.RGBtoHSB(colorArr[0][i], colorArr[1][i], colorArr[2][i], new float[3]);
                editedMessage = editedMessage + net.md_5.bungee.api.ChatColor.of(java.awt.Color.getHSBColor(c[0], c[1], c[2])) + (bold ? ChatColor.BOLD : "") + Character.toString(message.charAt(i));
            }

            text = text.replace(fullMessage, editedMessage);
            matcher1 = pattern1.matcher(text);
        }

        Matcher matcher2 = pattern2.matcher(text);
        while (matcher2.find()){
            String color = text.substring(matcher2.start(), matcher2.end());
            text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher2 = pattern2.matcher(text);
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }


    public static List<String> getColorList(List<String> text) {
        return text.stream().map(l -> getColor(l)).collect(Collectors.toList());
    }

}
