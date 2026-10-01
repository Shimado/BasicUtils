package org.shimado.basicutils.utils;

import org.jetbrains.annotations.NotNull;

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


    public static boolean isInt(@NotNull String number){
        try {
            Integer.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static boolean isDouble(@NotNull String number){
        try {
            Double.valueOf(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    @NotNull
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


    @NotNull
    public static String getIntNumber(long number, boolean isFormatting) {
        if(number == 0) return "0";
        DecimalFormat df = new DecimalFormat(isFormatting ? "#,###" : "#", new DecimalFormatSymbols(Locale.US));
        return df.format(number);
    }


    public static boolean inRangeInt(int number, int min, int max){
        return number >= min && number <= max;
    }

    public static boolean inRangeInt(@NotNull String numberString, int min, int max){
        if(!isInt(numberString)) return false;
        return inRangeInt(Integer.parseInt(numberString), min, max);
    }


    public static boolean inRangeDouble(double number, double min, double max){
        return number >= min && number <= max;
    }

    public static boolean inRangeDouble(@NotNull String numberString, double min, double max){
        if(!isDouble(numberString)) return false;
        return inRangeDouble(Double.parseDouble(numberString), min, max);
    }


    public static boolean getChance(double chance, double maxPercent){
        return chance > Math.random() * maxPercent;
    }

    public static boolean getChance(double chance){
        return getChance(chance, 100.0);
    }


    @NotNull
    public static String getDateTimeFormat(@NotNull Date date, @NotNull String dateFormat){
        return new SimpleDateFormat(dateFormat).format(date);
    }


    @NotNull
    public static String getDateTimeFormat(long timeInSeconds, @NotNull String dateFormat){
        boolean hasDays = dateFormat.contains("dd");
        boolean hasHours = dateFormat.contains("HH");
        boolean hasMinutes = dateFormat.contains("mm");
        boolean hasSeconds = dateFormat.contains("ss");

        long days = 0, hours = 0, minutes = 0, seconds = 0;

        if (hasSeconds && !hasMinutes && !hasHours && !hasDays) {
            seconds = timeInSeconds;
        } else {
            days = timeInSeconds / 86400;
            hours = (timeInSeconds % 86400) / 3600;
            minutes = (timeInSeconds % 3600) / 60;
            seconds = timeInSeconds % 60;

            // Если дни не требуются, добавляем часы от дней к обычным часам
            if (!hasDays && days > 0) {
                hours += days * 24;
                days = 0;
            }

            // Если часы не требуются, добавляем часы к минутам
            if (!hasHours && hours > 0) {
                minutes += hours * 60;
                hours = 0;
            }

            // Если минуты не требуются, добавляем минуты к секундам
            if (!hasMinutes && minutes > 0) {
                seconds += minutes * 60;
                minutes = 0;
            }
        }

        // Формируем результат
        String result = dateFormat;

        if (hasDays) {
            result = result.replace("dd", String.format("%02d", days));
        }
        if (hasHours) {
            result = result.replace("HH", String.format("%02d", hours));
        }
        if (hasMinutes) {
            result = result.replace("mm", String.format("%02d", minutes));
        }
        if (hasSeconds) {
            result = result.replace("ss", String.format("%02d", seconds));
        }

        return result;
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
