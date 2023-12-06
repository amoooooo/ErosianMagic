package aster.amo.erosianmagic.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PsychicScreamParticle extends TextureSheetParticle {
    Vector3f facing;
    protected PsychicScreamParticle(SpriteSet sprite, ClientLevel p_108323_, double p_108324_, double p_108325_, double p_108326_, Vector3f facing) {
        super(p_108323_, p_108324_, p_108325_, p_108326_);
        pickSprite(sprite);
        this.facing = facing;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.quadSize *= 1.01;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            // slowly move in the direction of facing and grow to 2x size
            this.xd += facing.x() * 0.01;
            this.yd += facing.y() * 0.01;
            this.zd += facing.z() * 0.01;
            this.alpha = 1 - (float) this.age / (float) this.lifetime;
        }
    }

    public void render(VertexConsumer p_107678_, Camera p_107679_, float p_107680_) {
        Vec3 vec3 = p_107679_.getPosition();
        float f = (float)(Mth.lerp((double)p_107680_, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)p_107680_, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)p_107680_, this.zo, this.z) - vec3.z());

        Quaternionf quaternionf = new Quaternionf();
        // turn facing into a quat
        quaternionf.rotateXYZ(facing.x(), facing.y(), facing.z());

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(p_107680_);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
//            vector3f.rotate(quaternionf);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(p_107680_);
        p_107678_.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        p_107678_.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        p_107678_.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        p_107678_.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
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
