package aster.amo.erosianmagic.mage.wizard;

import aster.amo.erosianmagic.mage.IMage;
import aster.amo.erosianmagic.mage.Mage;
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

public interface IWizard extends IMage {
    Capability<IWizard> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Override
    default String getClassName() { return "Wizard"; }

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Wizard impl = new Wizard();

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
