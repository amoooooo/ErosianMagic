package aster.amo.erosianmagic.bard;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBard {
    Capability<IBard> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Bard impl = new Bard();
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

    int getInspirationTime();
    void setInspirationTime(int time);
    boolean isInspiring();
    void sync(Player player);
    boolean isBard();
    void setBard(boolean bard);
    void tick(Player player);
}
