package aster.amo.erosianmagic.divine.witch.coven;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;

public interface IDedicant {
    Capability<IDedicant> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    void setLeader(UUID leader);

    UUID getLeader();

    boolean canListen();
    void setCanListen(boolean canListen);

    void tick(ServerPlayer player);

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Dedicant impl = new Dedicant();
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
