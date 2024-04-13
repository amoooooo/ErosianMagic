package aster.amo.erosianmagic.mage.wizard;

import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.WizardSyncPacket;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Wizard implements IWizard, INBTSerializable<CompoundTag> {
    boolean chosenClass = false;
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
        if(!player.level().isClientSide())
            Networking.sendToTracking(player.level(), player.blockPosition(), new WizardSyncPacket(serializeNBT(), player.getUUID()));
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
    public void onSetClass(Player player) {
        IWizard.super.onSetClass(player);
        SpellRegistry.SPELLS.getEntries().forEach(spellRegistryObject -> {
            AbstractSpell spell = spellRegistryObject.get();
            if(!(spell instanceof AbstractEldritchSpell)){
                MagicData.getPlayerMagicData(player).getSyncedData().learnSpell(spell);
            }
        });
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
