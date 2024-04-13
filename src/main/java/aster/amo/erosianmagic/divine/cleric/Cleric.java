package aster.amo.erosianmagic.divine.cleric;

import aster.amo.erosianmagic.divine.cleric.chapel.Temple;
import aster.amo.erosianmagic.net.classsync.ClericSyncPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.spellsnspellbooks.ClassSpells;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class Cleric implements ICleric, INBTSerializable<CompoundTag> {
    private float prayerPower = 0;
    private long sermonCooldown = 0;
    private Temple temple;
    private boolean chosenClass = false;
    private BlockPos recallPos;
    @Override
    public float getPrayerPower() {
        if(hasTemple()) {
            prayerPower = temple.getTempleLevel();
        }
        return prayerPower;
    }

    @Override
    public void setPrayerPower(float power) {
        prayerPower = power;
    }

    @Override
    public void addWorshipper(UUID uuid) {
        temple.addWorshipper(uuid);
    }

    @Override
    public void removeWorshipper(UUID uuid) {
        temple.removeWorshipper(uuid);
    }

    @Override
    public List<UUID> getWorshippers() {
        if(temple == null) return List.of();
        return temple.getWorshippers();
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
    public Temple getTemple() {
        return temple;
    }

    @Override
    public void setTemple(Temple temple) {
        this.temple = temple;
    }

    @Override
    public boolean hasTemple() {
        return temple != null;
    }

    @Override
    public BlockPos getRecallPos() {
        return recallPos;
    }

    @Override
    public void setRecallPos(BlockPos pos) {
        recallPos = pos;
    }

    @Override
    public void setChosenClass(boolean isClass, Player player) {
        chosenClass = isClass;
        if(isClass) {
            List<Supplier<AbstractSpell>> spells = ClassSpells.CLASS_SPELLS.get("Cleric");
            MagicData.getPlayerMagicData(player).getSyncedData().forgetAllSpells();
//            spells.forEach(spell -> MagicData.getPlayerMagicData(player).getSyncedData().learnSpell(spell.get()));
        }
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
        if(temple != null){
            nbt.put("temple", temple.toNbt());
        }
        if (recallPos != null) {
            nbt.put("recallPos", net.minecraft.nbt.NbtUtils.writeBlockPos(recallPos));
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        prayerPower = nbt.getFloat("prayerPower");
        sermonCooldown = nbt.getLong("sermonCooldown");
        chosenClass = nbt.getBoolean("chosenClass");
        if(nbt.contains("temple"))
            temple = Temple.fromNbt(nbt.getCompound("temple"));
        if (nbt.contains("recallPos")) {
            recallPos = net.minecraft.nbt.NbtUtils.readBlockPos(nbt.getCompound("recallPos"));
        }
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide()) Networking.sendToTracking(player.level(), player.blockPosition(), new ClericSyncPacket(serializeNBT(), player.getUUID()));
    }

    @Override
    public int getLevel() {
        if (hasTemple()) {
            return temple.getTempleLevel();
        }
        return 1;
    }

    @Override
    public void setLevel(int level) {
        if(hasTemple()) {
            temple.setTempleLevel(level);
        }
    }
}
