package aster.amo.erosianmagic.client;

import elucent.eidolon.api.deity.Deity;
import foundry.veil.quasar.data.QuasarParticles;
import foundry.veil.quasar.data.module.force.VortexForceData;
import foundry.veil.quasar.emitters.ParticleEmitter;
import foundry.veil.quasar.emitters.ParticleSystemManager;
import foundry.veil.quasar.emitters.modules.particle.force.VortexForceModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3dc;

public class ClientSpellHandler {
    public static void spiritGuardians(LivingEntity entity, Deity deity) {
        ParticleEmitter emitter = ParticleSystemManager.getInstance().createEmitter(entity.level(), new ResourceLocation("erosianmagic:spirit_guardians"));
        if (emitter == null) {
            return;
        }
        ParticleSystemManager.getInstance().addParticleSystem(emitter);
    }
}
