package aster.amo.erosianmagic.fighter.barbarian;

import aster.amo.erosianmagic.fighter.Fighter;
import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.util.IClass;
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

public interface IBarbarian extends IFighter {
    Capability<IBarbarian> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    @Override
    default String getClassName() {
        return "Barbarian";
    }

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Barbarian impl = new Barbarian();

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
