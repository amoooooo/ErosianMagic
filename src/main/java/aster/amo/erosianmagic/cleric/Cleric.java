package aster.amo.erosianmagic.cleric;

import aster.amo.erosianmagic.cleric.chapel.Chapel;
import aster.amo.erosianmagic.net.BardSyncPacket;
import aster.amo.erosianmagic.net.ClericSyncPacket;
import aster.amo.erosianmagic.net.Networking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.UUID;

public class Cleric implements ICleric, INBTSerializable<CompoundTag> {
    private float prayerPower = 0;
    private long sermonCooldown = 0;
    private Chapel chapel;
    private boolean chosenClass = false;
    @Override
    public float getPrayerPower() {
        return prayerPower;
    }

    @Override
    public void setPrayerPower(float power) {
        prayerPower = power;
    }

    @Override
    public void addWorshipper(UUID uuid) {
        chapel.addWorshipper(uuid);
    }

    @Override
    public void removeWorshipper(UUID uuid) {
        chapel.removeWorshipper(uuid);
    }

    @Override
    public List<UUID> getWorshippers() {
        return chapel.getWorshippers();
    }

    @Override
    public long getSermonCooldown() {
        return sermonCooldown;
    }

    @Override
    public void setSermonCooldown(long cooldown) {
        sermonCooldown = cooldown;
    }

    @Override
    public boolean isSermonCooldown() {
        return sermonCooldown > 0;
    }

    @Override
    public Chapel getChapel() {
        return chapel;
    }

    @Override
    public void setChapel(Chapel chapel) {
        this.chapel = chapel;
    }

    @Override
    public boolean hasChapel() {
        return chapel != null;
    }

    @Override
    public void setChosenClass(boolean isClass) {
        chosenClass = isClass;
    }

    @Override
    public boolean isChosenClass() {
        return chosenClass;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("prayerPower", prayerPower);
        nbt.putLong("sermonCooldown", sermonCooldown);
        nbt.putBoolean("chosenClass", chosenClass);
        if(chapel != null){
            nbt.put("chapel", chapel.toNbt());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        prayerPower = nbt.getFloat("prayerPower");
        sermonCooldown = nbt.getLong("sermonCooldown");
        chosenClass = nbt.getBoolean("chosenClass");
        if(nbt.contains("chapel"))
            chapel = Chapel.fromNbt(nbt.getCompound("chapel"));
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide()) Networking.sendToTracking(player.level(), player.blockPosition(), new ClericSyncPacket(serializeNBT(), player.getUUID()));
    }
}
