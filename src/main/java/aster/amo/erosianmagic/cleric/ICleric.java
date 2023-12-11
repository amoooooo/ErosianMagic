package aster.amo.erosianmagic.cleric;

import aster.amo.erosianmagic.cleric.chapel.Chapel;
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

public interface ICleric extends IClass {
    Capability<ICleric> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        final Cleric impl = new Cleric();

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

    float getPrayerPower();
    void setPrayerPower(float power);
    void addWorshipper(UUID uuid);
    void removeWorshipper(UUID uuid);
    List<UUID> getWorshippers();
    long getSermonCooldown();
    void setSermonCooldown(long cooldown);
    boolean isSermonCooldown();
    Chapel getChapel();
    void setChapel(Chapel chapel);
    boolean hasChapel();

    @Override
    default String getClassName() {
        return "Cleric";
    }
}
