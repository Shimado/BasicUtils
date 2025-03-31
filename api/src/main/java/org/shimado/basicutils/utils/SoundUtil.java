package org.shimado.basicutils.utils;

import org.bukkit.Sound;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoundUtil {

    private static List<Sound> SOUNDS = Arrays.stream(Sound.class.getDeclaredFields()).map(it -> {
        try {
            if(!Modifier.isPublic(it.getModifiers())) return null;
            Object obj = it.get(null);
            if(obj == null || !(obj instanceof Sound)) return null;
            return (Sound) it.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }).filter(it -> it != null).collect(Collectors.toList());


    public static Sound getSound(String... sounds) {
        for(Object sound : SOUNDS){
            for(String s : sounds){
                if(sound.toString().equals(s)){
                    return (Sound) sound;
                }
            }
        }
        return Sound.UI_TOAST_IN;
    }

}
