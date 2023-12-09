package aster.amo.erosianmagic.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class PsychicScreamParticle extends TextureSheetParticle {
    Vector3f facing;
    protected PsychicScreamParticle(SpriteSet sprite, ClientLevel p_108323_, double p_108324_, double p_108325_, double p_108326_, Vector3f facing) {
        super(p_108323_, p_108324_, p_108325_, p_108326_);
        pickSprite(sprite);
        this.facing = facing;
        this.lifetime = 20;
        this.xd = facing.x() * 5;
        this.yd = facing.y() * 5;
        this.zd = facing.z() * 5;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return new ParticleRenderType() {
            public void begin(BufferBuilder p_107448_, TextureManager p_107449_) {
                RenderSystem.disableCull();
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.setShader(GameRenderer::getParticleShader);
                RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
                p_107448_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
            }

            public void end(Tesselator p_107451_) {
                p_107451_.end();
            }

            public String toString() {
                return "SCREAM";
            }
        };
    }

    float yaw = 0;
    float pitch = 0;
    float roll = 0;
    float oYaw = 0;
    float oPitch = 0;
    float oRoll = 0;
    Vec3 motion = new Vec3(0,0,0);
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.quadSize *= 1.05;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.alpha = 1 - (float) this.age / (float) this.lifetime;
        }
        motion = motion.normalize();
        pitch = (float) Math.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z));
        yaw = (float) Math.atan2(motion.x, motion.z);
        yaw += Math.PI / 2;
    }
    public static final Vec3[] PLANE = {
            new Vec3(-1, 1, 0), new Vec3(1, 1, 0), new Vec3(1, -1, 0), new Vec3(-1, -1, 0)
    };

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        float lerpedX = (float) Mth.lerp(partialTicks, xo, x);
        float lerpedY = (float) Mth.lerp(partialTicks, yo, y);
        float lerpedZ = (float) Mth.lerp(partialTicks, zo, z);
        float lerpedYaw = Mth.lerp(partialTicks, oYaw, yaw);
        float lerpedPitch = Mth.lerp(partialTicks, oPitch, pitch);
        float lerpedRoll = Mth.lerp(partialTicks, oRoll,roll);
        Vec3[] faceVerts = new Vec3[]{
                PLANE[0],
                PLANE[1],
                PLANE[2],
                PLANE[3]
        };

        Quaternionf faceCameraRotation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        RenderSystem.setShaderTexture(0, sprite.atlasLocation());
        // turn quat into pitch and yaw
        for (int j = 0; j < 4; j++) {
            Vector3f vec = faceVerts[j].scale(-1).toVector3f();
//            vec = vec
//                    .rotateX(lerpedRoll)
//                    .rotateY(lerpedPitch)
//                    .rotateZ(lerpedYaw);
//                vec = vec.xRot(lerpedPitch).yRot(lerpedYaw).zRot(lerpedRoll);
            vec = vec.add(new Vector3f(lerpedX, lerpedY, lerpedZ));

            float u = 0;
            float v = 0;
            if (j == 0) {
                u = 0;
                v = 0;
            } else if (j == 1) {
                u = 1;
                v = 0;
            } else if (j == 2) {
                u = 1;
                v = 1;
            } else {
                u = 0;
                v = 1;
            }
            consumer.vertex(vec.x, vec.y, vec.z)
                    .uv(u, v)
                    .color(1f,1f,1f,1f)
                    .uv2(LightTexture.FULL_BRIGHT)
                    .endVertex();
        }
    }

    public static class Factory implements ParticleProvider<PsychicScreamParticleOptions> {
        private final SpriteSet sprites;
        public Factory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }
        @Nullable
        @Override
        public Particle createParticle(PsychicScreamParticleOptions p_107421_, ClientLevel p_107422_, double p_107423_, double p_107424_, double p_107425_, double p_107426_, double p_107427_, double p_107428_) {
            return new PsychicScreamParticle(sprites, p_107422_, p_107423_, p_107424_, p_107425_, p_107421_.getFacing());
        }
    }
}
