package aster.amo.erosianmagic.mage.machinist;

import aster.amo.erosianmagic.net.classsync.MachinistSyncPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.spellsnspellbooks.ClassSpells;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.function.Supplier;

public class Machinist implements IMachinist, INBTSerializable<CompoundTag> {
    boolean chosenClass = false;
    @Override
    public void setChosenClass(boolean isClass, Player player) {
        chosenClass = isClass;
        if(isClass) {
            List<Supplier<AbstractSpell>> spells = ClassSpells.CLASS_SPELLS.get("Machinist");
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
            Networking.sendToTracking(player.level(), player.blockPosition(), new MachinistSyncPacket(serializeNBT(), player.getUUID()));
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
