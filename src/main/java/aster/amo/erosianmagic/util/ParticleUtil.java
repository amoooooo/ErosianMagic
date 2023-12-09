package aster.amo.erosianmagic.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {
    public static Vec3 getProjectileSpawnPos(LivingEntity player, InteractionHand hand, float distance, float spread) {
        int angle = hand == InteractionHand.MAIN_HAND ? 225 : 90;
        return player.position().add(player.getLookAngle().scale(distance)).add(spread * Math.sin(Math.toRadians(angle - player.yHeadRot)), player.getBbHeight() * 0.9f, spread * Math.cos(Math.toRadians(angle - player.yHeadRot)));
    }
}
