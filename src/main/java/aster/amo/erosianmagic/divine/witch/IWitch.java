package aster.amo.erosianmagic.divine.witch;

import aster.amo.erosianmagic.divine.IDivine;
import aster.amo.erosianmagic.divine.witch.coven.Coven;
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

import java.util.List;
import java.util.UUID;

public interface IWitch extends IDivine {
    Capability<IWitch> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    Coven getCoven();
    void setCoven(Coven coven);
    @Override
    default String getClassName() { return "Witch"; }

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Witch impl = new Witch();

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
