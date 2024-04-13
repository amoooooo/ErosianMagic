package aster.amo.erosianmagic.fighter.paladin;

import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.fighter.barbarian.Barbarian;
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

public interface IPaladin extends IFighter {
    Capability<IPaladin> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Override
    default String getClassName() {
        return "Paladin";
    }

    int getLevel();
    void setLevel(int level);

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Paladin impl = new Paladin();

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
