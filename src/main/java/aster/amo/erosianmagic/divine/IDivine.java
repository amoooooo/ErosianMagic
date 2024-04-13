package aster.amo.erosianmagic.divine;

import aster.amo.erosianmagic.util.IClass;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IDivine extends IClass {
    Capability<IDivine> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Override
    default String getClassName() {
        return "Divine";
    }

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Divine impl = new Divine();

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
