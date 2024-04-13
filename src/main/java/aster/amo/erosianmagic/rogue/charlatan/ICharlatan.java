package aster.amo.erosianmagic.rogue.charlatan;

import aster.amo.erosianmagic.rogue.IRogue;
import aster.amo.erosianmagic.rogue.Rogue;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICharlatan extends IRogue {
    Capability<ICharlatan> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    @Override
    default String getClassName() {
        return "Charlatan";
    }

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Charlatan impl = new Charlatan();

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
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
