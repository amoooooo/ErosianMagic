package aster.amo.erosianmagic.rogue;

import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.RogueSyncPacket;
import aster.amo.erosianmagic.util.ClassUtils;
import jackiecrazy.cloakanddagger.capability.action.PermissionData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Rogue implements IRogue, INBTSerializable<CompoundTag> {
    private boolean chosenClass = false;
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
        return "Rogue";
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide())
            Networking.sendToTracking(player.level(), player.blockPosition(), new RogueSyncPacket(serializeNBT(), player.getUUID()));
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

    @Override
    public void onSetClass(Player player) {
        IRogue.super.onSetClass(player);
        PermissionData.getCap(player).setStab(true);
        if(!player.level().isClientSide) {
            ClassUtils.enableParcool((ServerPlayer) player);
        }
    }

    @Override
    public void onSetOtherClass(Player player) {
        IRogue.super.onSetOtherClass(player);
        PermissionData.getCap(player).setStab(false);
        if(!player.level().isClientSide) {
            ClassUtils.disableParcool((ServerPlayer) player);
        }
    }
}
