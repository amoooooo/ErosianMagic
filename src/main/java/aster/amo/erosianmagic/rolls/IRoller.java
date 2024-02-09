package aster.amo.erosianmagic.rolls;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IRoller {
    Capability<IRoller> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    int getRoll();
    void tickRoll();
    int getRollTicks();
    boolean shouldDisplayRoll();
    void setShouldDisplayRoll(boolean shouldDisplayRoll);
    void setRollTicks(int rollTicks);
    void sync(LivingEntity entity);
    void setRoll(int roll);
    void setFakeRoll(int fakeRoll);
    int getFakeRoll();
    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Roller impl = new Roller();
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
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
