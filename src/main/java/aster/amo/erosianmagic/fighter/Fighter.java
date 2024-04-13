package aster.amo.erosianmagic.fighter;

import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.FighterSyncPacket;
import aster.amo.erosianmagic.net.classsync.RogueSyncPacket;
import aster.amo.erosianmagic.rogue.IRogue;
import aster.amo.erosianmagic.util.ClassUtils;
import jackiecrazy.cloakanddagger.capability.action.PermissionData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Fighter implements IFighter, INBTSerializable<CompoundTag> {
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
    public String getClassName() {
        return "Fighter";
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide())
            Networking.sendToTracking(player.level(), player.blockPosition(), new FighterSyncPacket(serializeNBT(), player.getUUID()));
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

    @Override
    public void onSetClass(Player player) {
        IFighter.super.onSetClass(player);
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
    public void onSetOtherClass(Player player) {
        IFighter.super.onSetOtherClass(player);
    }
}
