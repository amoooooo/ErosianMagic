package aster.amo.erosianmagic.divine.witch;

import aster.amo.erosianmagic.divine.cleric.ICleric;
import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "erosianmagic", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WitchModEvents {

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent event){
        if(event.getEntity().level().isClientSide) return;
        event.getEntity().getCapability(IWitch.INSTANCE).ifPresent((witch) -> {
            if(witch.isChosenClass()){
                AbstractSpell spell = SpellRegistry.getSpell(event.getSpellId());
                witch.getCoven().handleManaSplit((ServerLevel) event.getEntity().level(), spell, (ServerPlayer) event.getEntity(), event.getSpellLevel());
            }
        });
    }
}
