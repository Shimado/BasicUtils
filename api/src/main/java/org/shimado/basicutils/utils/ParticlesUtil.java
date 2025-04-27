package org.shimado.basicutils.utils;


import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticlesUtil {

    private static List<Particle> savedParticles = new ArrayList<>();

    public static Particle getParticle(String[] particles) {
        for(String stringParticle : particles){
            for(Particle particle : savedParticles){
                if(particle.toString().equalsIgnoreCase(stringParticle)){
                    return particle;
                }
            }
        }

        for (String stringParticle : particles) {
            if (Arrays.stream(Particle.values()).anyMatch(it -> it.name().equalsIgnoreCase(stringParticle))) {
                Particle particle = Particle.valueOf(stringParticle.toUpperCase());
                savedParticles.add(particle);
                return particle;
            }
        }

        return Particle.FLAME;
    }

}
