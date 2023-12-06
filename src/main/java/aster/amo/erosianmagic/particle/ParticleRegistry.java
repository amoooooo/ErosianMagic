package aster.amo.erosianmagic.particle;

import aster.amo.erosianmagic.ErosianMagic;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ErosianMagic.MODID);
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    public static final RegistryObject<ParticleType<PsychicScreamParticleOptions>> PSYCHIC_SCREAM_PARTICLE_TYPE = PARTICLE_TYPES.register("psychic_scream", () -> new ParticleType<PsychicScreamParticleOptions>(false, PsychicScreamParticleOptions.DESERIALIZER) {
        @Override
        public Codec<PsychicScreamParticleOptions> codec() {
            return RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(PsychicScreamParticleOptions::getX),
                    Codec.FLOAT.fieldOf("y").forGetter(PsychicScreamParticleOptions::getY),
                    Codec.FLOAT.fieldOf("z").forGetter(PsychicScreamParticleOptions::getZ)
            ).apply(instance, PsychicScreamParticleOptions::new));
        }
    });

}
