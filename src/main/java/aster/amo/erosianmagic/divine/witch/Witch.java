package aster.amo.erosianmagic.divine.witch;

import aster.amo.erosianmagic.divine.witch.coven.Coven;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.WitchSyncPacket;
import aster.amo.erosianmagic.spellsnspellbooks.ClassSpells;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.function.Supplier;

public class Witch implements IWitch, INBTSerializable<CompoundTag> {
    boolean chosenClass = false;
    Coven coven = new Coven();
    int level = 1;
    @Override
    public void setChosenClass(boolean isClass, Player player) {
        chosenClass = isClass;
        if(isClass) {
            List<Supplier<AbstractSpell>> spells = ClassSpells.CLASS_SPELLS.get("Witch");
            MagicData.getPlayerMagicData(player).getSyncedData().forgetAllSpells();
//            spells.forEach(spell -> MagicData.getPlayerMagicData(player).getSyncedData().learnSpell(spell.get()));
        }
    }

    @Override
    public boolean isChosenClass() {
        return chosenClass;
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide())
            Networking.sendToTracking(player.level(), player.blockPosition(), new WitchSyncPacket(serializeNBT(), player.getUUID()));
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
        nbt.put("coven", coven.serializeNBT());
        nbt.putInt("level", level);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        chosenClass = nbt.getBoolean("chosenClass");
        coven = Coven.deserializeNBT(nbt.getCompound("coven"));
        level = nbt.getInt("level");
    }

    @Override
    public Coven getCoven() {
        return coven;
    }

    @Override
    public void setCoven(Coven coven) {
        this.coven = coven;
    }
}
