package org.shimado.basicutils.utils;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecordsSoundUtil {

    private static final Map<String, Sound> SOUNDS = new HashMap<>();

    static {
        Arrays.stream(Sound.class.getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .forEach(field -> {
                    try {
                        Object sound = field.get(null);
                        if(sound != null){
                            String fN = field.getName();
                            String fNL = fN.toUpperCase();
                            if(fNL.startsWith("MUSIC_DISC") || fNL.startsWith("RECORD")){
                                SOUNDS.put(fN, (Sound) sound);
                            }
                        }
                    } catch (IllegalAccessException e) {
                    }
                });
    }


    @NotNull
    public static Sound getSound(@NotNull String... sounds) {
        for (String soundName : sounds) {
            Sound sound = SOUNDS.get(soundName);
            if (sound != null) {
                return sound;
            }
        }
        return Sound.UI_TOAST_IN;
    }

}
