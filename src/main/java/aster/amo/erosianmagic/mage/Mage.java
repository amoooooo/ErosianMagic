package aster.amo.erosianmagic.mage;

import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.MageSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Mage implements IMage, INBTSerializable<CompoundTag> {
    boolean chosenClass = false;
    @Override
    public void setChosenClass(boolean isClass, Player player) {
        chosenClass = isClass;
    }

    @Override
    public boolean isChosenClass() {
        return chosenClass;
    }

    @Override
    public void sync(Player player) {
        if (!player.level().isClientSide()) {
             Networking.sendToTracking(player.level(), player.blockPosition(), new MageSyncPacket(serializeNBT(), player.getUUID()));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("chosenClass", chosenClass);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        chosenClass = nbt.getBoolean("chosenClass");
    }
}
