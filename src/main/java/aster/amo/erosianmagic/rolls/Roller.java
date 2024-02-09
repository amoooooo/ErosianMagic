package aster.amo.erosianmagic.rolls;

import aster.amo.erosianmagic.cleric.chapel.IWorshipper;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.RollSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public class Roller implements IRoller, INBTSerializable<CompoundTag> {
    int roll;
    int rollTicks;
    boolean shouldDisplayRoll;
    int internalTicks;
    int fakeRoll;
    @Override
    public int getRoll() {
        return roll;
    }

    @Override
    public void tickRoll() {
        if(rollTicks < 10) {
            rollTicks++;
            shouldDisplayRoll = true;
        }
        if(internalTicks < 40) internalTicks++;
        else {
            internalTicks = 0;
            roll = 0;
            shouldDisplayRoll = false;
        }
    }

    @Override
    public int getRollTicks() {
        return rollTicks;
    }

    @Override
    public boolean shouldDisplayRoll() {
        return shouldDisplayRoll;
    }

    @Override
    public void setShouldDisplayRoll(boolean shouldDisplayRoll) {
        this.shouldDisplayRoll = shouldDisplayRoll;
    }

    @Override
    public void setRollTicks(int rollTicks) {
        this.rollTicks = rollTicks;
    }

    @Override
    public void sync(LivingEntity entity) {
        if(!entity.level().isClientSide)
            Networking.sendToTracking(entity.level(), entity.blockPosition(), new RollSyncPacket(serializeNBT(), entity.getId()));
    }

    @Override
    public void setRoll(int roll) {
        this.roll = roll;
        this.internalTicks = 0;
        this.shouldDisplayRoll = true;
        this.rollTicks = 0;
    }

    @Override
    public void setFakeRoll(int fakeRoll) {
        this.fakeRoll = fakeRoll;
    }

    @Override
    public int getFakeRoll() {
        return fakeRoll;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("roll", roll);
        nbt.putInt("rollTicks", rollTicks);
        nbt.putBoolean("shouldDisplayRoll", shouldDisplayRoll);
        nbt.putInt("internalTicks", internalTicks);
        nbt.putInt("fakeRoll", fakeRoll);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        roll = nbt.getInt("roll");
        rollTicks = nbt.getInt("rollTicks");
        shouldDisplayRoll = nbt.getBoolean("shouldDisplayRoll");
        internalTicks = nbt.getInt("internalTicks");
        fakeRoll = nbt.getInt("fakeRoll");
    }
}
