package aster.amo.erosianmagic.fighter.barbarian;

import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.net.ClientboundUpdateSpellsPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.net.classsync.BarbarianSyncPacket;
import aster.amo.erosianmagic.net.classsync.RogueSyncPacket;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.UUID;

public class Barbarian implements IBarbarian, INBTSerializable<CompoundTag> {
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
        return "Barbarian";
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide()) {
            Networking.sendToTracking(player.level(), player.blockPosition(), new BarbarianSyncPacket(serializeNBT(), player.getUUID()));
            Networking.sendTo(player, new ClientboundUpdateSpellsPacket());
        }
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

    public static final UUID removalUUID = UUID.fromString("f3f8f3f8-1f8f-1f8f-1f8f-1f8f1f8f1f8f");
    @Override
    public void onSetClass(Player player) {
        IBarbarian.super.onSetClass(player);
        SyncedSpellData data = MagicData.getPlayerMagicData(player).getSyncedData();
        data.learnSpell(SpellRegistry.RAGE.get());
//        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
//            handler.getStacksHandler("spellbook").ifPresent(stacks -> {
//                stacks.addPermanentModifier(new AttributeModifier(removalUUID, "nobook", -1.0, AttributeModifier.Operation.ADDITION));
//            });
//        });
    }

    @Override
    public void onSetOtherClass(Player player) {
        IBarbarian.super.onSetOtherClass(player);
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            handler.getStacksHandler("spellbook").ifPresent(stacks -> {
                stacks.removeModifier(removalUUID);
            });
        });
    }
}
