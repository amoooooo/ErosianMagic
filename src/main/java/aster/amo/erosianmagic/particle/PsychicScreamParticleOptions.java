package aster.amo.erosianmagic.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3f;

public class PsychicScreamParticleOptions extends SimpleParticleType {
    public static final ParticleOptions.Deserializer<PsychicScreamParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<PsychicScreamParticleOptions>() {
        public PsychicScreamParticleOptions fromCommand(ParticleType<PsychicScreamParticleOptions> p_123846_, StringReader sr) throws CommandSyntaxException {
            Vector3f facing = DustParticleOptionsBase.readVector3f(sr);
            return new PsychicScreamParticleOptions(p_123846_.getOverrideLimiter(), facing);
        }

        public PsychicScreamParticleOptions fromNetwork(ParticleType<PsychicScreamParticleOptions> p_123849_, FriendlyByteBuf p_123850_) {
            return new PsychicScreamParticleOptions(p_123849_.getOverrideLimiter(), DustParticleOptionsBase.readVector3f(p_123850_));
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf p_123840_) {
        p_123840_.writeFloat(facing.x());
        p_123840_.writeFloat(facing.y());
        p_123840_.writeFloat(facing.z());
    }

    @Override
    public String writeToString() {
        return String.format("%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), facing.x(), facing.y(), facing.z());
    }

    Vector3f facing;
    public PsychicScreamParticleOptions(boolean p_123837_, Vector3f facing) {
        super(p_123837_);
        this.facing = facing;
    }

    public PsychicScreamParticleOptions(float x, float y, float z) {
        super(false);
        this.facing = new Vector3f(x, y, z);
    }

    public Vector3f getFacing() {
        return facing;
    }

    public void setFacing(Vector3f facing) {
        this.facing = facing;
    }

    public float getX() {
        return facing.x();
    }

    public float getY() {
        return facing.y();
    }

    public float getZ() {
        return facing.z();
    }


}
