package org.shimado.basicutils.utils;

import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ParticlesUtil {

    private static Map<String, Particle> savedParticles = new HashMap<>();

    @NotNull
    public static Particle getParticle(@NotNull String[] particles) {
        for(String stringParticle : particles){
            String particleName = stringParticle.toUpperCase();

            if(savedParticles.containsKey(particleName)){
                return savedParticles.get(particleName);
            }

            if (Arrays.stream(Particle.values()).anyMatch(it -> it.name().equals(particleName))) {
                Particle particle = Particle.valueOf(particleName);
                savedParticles.put(particleName, particle);
                return particle;
            }
        }

        return Particle.FLAME;
    }

}
