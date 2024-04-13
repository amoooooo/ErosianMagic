package aster.amo.erosianmagic.fighter.champion;

import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.ChampionSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import top.theillusivec4.curios.api.CuriosApi;

import static aster.amo.erosianmagic.fighter.barbarian.Barbarian.removalUUID;

public class Champion implements IChampion, INBTSerializable<CompoundTag> {
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
        if(!player.level().isClientSide) {
            Networking.sendToTracking(player.level(), player.blockPosition(), new ChampionSyncPacket(serializeNBT(), player.getUUID()));
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
        IChampion.super.onSetClass(player);
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            handler.getStacksHandler("spellbook").ifPresent(stacks -> {
                stacks.addPermanentModifier(new AttributeModifier(removalUUID, "nobook", -1.0, AttributeModifier.Operation.ADDITION));
            });
        });
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
        IChampion.super.onSetOtherClass(player);
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            handler.getStacksHandler("spellbook").ifPresent(stacks -> {
                stacks.removeModifier(removalUUID);
            });
        });
    }
}
