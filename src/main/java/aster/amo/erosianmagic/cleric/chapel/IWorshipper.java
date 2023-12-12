package aster.amo.erosianmagic.cleric.chapel;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IWorshipper {
    Capability<IWorshipper> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    void setLeader(UUID leader);
    UUID getLeader();
    boolean canListen();
    void tick(Level level);
    Vec3 getBeforePosition();
    void setBeforePosition(Vec3 beforePosition);
    Vec3 getSeatPosition();
    void setSeatPosition(Vec3 seatPosition);
    float getLoseFaithChance();
    void setLoseFaithChance(float loseFaithChance);
    void setLastSermon(long lastSermon);
    long getLastSermon();

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Worshipper impl = new Worshipper();
        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            if(cap == INSTANCE) return (LazyOptional<T>) LazyOptional.of(() -> impl);
            else return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return impl.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            impl.deserializeNBT(nbt);
        }
    }
}
