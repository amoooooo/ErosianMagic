package aster.amo.erosianmagic.rogue.monk;

import aster.amo.erosianmagic.rogue.IRogue;
import aster.amo.erosianmagic.rogue.charlatan.Charlatan;
import aster.amo.erosianmagic.rogue.charlatan.ICharlatan;
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

public interface IMonk extends IRogue {
    Capability<IMonk> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    @Override
    default String getClassName() {
        return "Monk";
    }

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Monk impl = new Monk();

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
