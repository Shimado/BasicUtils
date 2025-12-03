package org.shimado.basicutils.utils;

import org.bukkit.Particle;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticlesUtil {

    private static List<Particle> savedParticles = new ArrayList<>();

    @Nonnull
    public static Particle getParticle(@Nonnull String[] particles) {
        for(String stringParticle : particles){

            for(Particle particle : savedParticles){
                if(particle.toString().equalsIgnoreCase(stringParticle)){
                    return particle;
                }
            }

            if (Arrays.stream(Particle.values()).anyMatch(it -> it.name().equalsIgnoreCase(stringParticle))) {
                Particle particle = Particle.valueOf(stringParticle.toUpperCase());
                savedParticles.add(particle);
                return particle;
            }
        }

        return Particle.FLAME;
    }

}
