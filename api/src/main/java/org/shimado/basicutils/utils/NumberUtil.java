package org.shimado.basicutils.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class NumberUtil {

    public static int randomInt(int min, int max) {
        return min + ((int) Math.floor(Math.random() * (max - min)));
    }

    public static double randomDouble(double min, double max) {
        return min + (Math.floor(Math.random() * (max - min)));
    }


    public static boolean isInt(String number){
        try {
            Integer.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static boolean isDouble(String number){
        try {
            Double.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static String getIntNumber(double number) {
        String[] chars = String.valueOf(number).replace(",", ".").split("\\.");
        if (chars.length == 3 && !chars[2].startsWith("0")) {
            return String.valueOf(number);
        } else {
            return chars[0];
        }
    }


    public static boolean getChance(double chance){
        return chance > Math.random() * 100.0;
    }


    public static String getDateTimeFormat(Date date, String dateFormat){
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
