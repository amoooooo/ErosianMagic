package aster.amo.erosianmagic.fighter.paladin;

import aster.amo.erosianmagic.net.ClientboundUpdateSpellsPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.PaladinSyncPacket;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Paladin implements IPaladin, INBTSerializable<CompoundTag> {
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
        if(!player.level().isClientSide()) {
            Networking.sendToTracking(player.level(), player.blockPosition(), new PaladinSyncPacket(serializeNBT(), player.getUUID()));
            Networking.sendTo(player, new ClientboundUpdateSpellsPacket());
        }
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
        IPaladin.super.onSetClass(player);
        SyncedSpellData data = MagicData.getPlayerMagicData(player).getSyncedData();
        data.learnSpell(SpellRegistry.DIVINE_SMITE_SPELL.get());
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }
}
