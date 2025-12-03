package org.shimado.basicutils.utils;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;

public class NumberUtil {

    public static int randomInt(int min, int max) {
        return min + ((int) Math.floor(Math.random() * (max - min)));
    }

    public static int randomIntInclusive(int min, int max) {
        return min + ((int) Math.floor(Math.random() * ((max + 1) - min)));
    }


    public static double randomDouble(double min, double max) {
        return min + (Math.random() * (max - min));
    }

    public static double randomDoubleInclusive(double min, double max) {
        return min + (Math.random() * ((max + 1) - min));
    }


    public static boolean isInt(@Nonnull String number){
        try {
            Integer.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static boolean isDouble(@Nonnull String number){
        try {
            Double.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    @Nonnull
    public static String getIntNumber(double number, boolean isFormatting) {
        if(number == 0) return "0";
        DecimalFormat df = new DecimalFormat(isFormatting ? "#,###.##" : "#.##", new DecimalFormatSymbols(Locale.US));
        String formatted = df.format(number);

        if (formatted.contains(".")) {
            formatted = formatted.replaceAll("0*$", "");
            if (formatted.endsWith(".")) {
                formatted = formatted.substring(0, formatted.length() - 1);
            }
        }

        return formatted;
    }


    public static boolean inRangeInt(int number, int min, int max){
        return number >= min && number <= max;
    }

    public static boolean inRangeInt(@Nonnull String numberString, int min, int max){
        if(!isInt(numberString)) return false;
        return inRangeInt(Integer.parseInt(numberString), min, max);
    }


    public static boolean inRangeDouble(double number, double min, double max){
        return number >= min && number <= max;
    }

    public static boolean inRangeDouble(@Nonnull String numberString, double min, double max){
        if(!isDouble(numberString)) return false;
        return inRangeDouble(Double.parseDouble(numberString), min, max);
    }


    public static boolean getChance(double chance, double maxPercent){
        return chance > Math.random() * maxPercent;
    }

    public static boolean getChance(double chance){
        return getChance(chance, 100.0);
    }


    @Nonnull
    public static String getDateTimeFormat(@Nonnull Date date, @Nonnull String dateFormat){
        return new SimpleDateFormat(dateFormat).format(date);
    }


    public static long[] getTime(int time){
        Duration d = Duration.ofSeconds(time);
        return new long[]{
                d.toDays(),
                d.toHoursPart(),
                d.toMinutesPart(),
                d.toSecondsPart()
        };
    }


}
