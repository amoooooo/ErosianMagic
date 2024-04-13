package aster.amo.erosianmagic.rogue.charlatan;

import jackiecrazy.cloakanddagger.capability.action.PermissionData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Charlatan implements ICharlatan, INBTSerializable<CompoundTag> {
    private boolean chosenClass = false;
    private int level = 1;
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

    @Override
    public void onSetClass(Player player) {
        ICharlatan.super.onSetClass(player);
        PermissionData.getCap(player).setStab(true);
    }

    @Override
    public void onSetOtherClass(Player player) {
        ICharlatan.super.onSetOtherClass(player);
        PermissionData.getCap(player).setStab(false);
    }
}
