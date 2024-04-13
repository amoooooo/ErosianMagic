package aster.amo.erosianmagic.divine;

import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.DivineSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Divine implements IDivine, INBTSerializable<CompoundTag> {
    private boolean chosenClass = false;
    int level = 1;
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
             Networking.sendToTracking(player.level(), player.blockPosition(), new DivineSyncPacket(serializeNBT(), player.getUUID()));
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("chosenClass", chosenClass);
        nbt.putInt("level", level);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        chosenClass = nbt.getBoolean("chosenClass");
        level = nbt.getInt("level");
    }
}
