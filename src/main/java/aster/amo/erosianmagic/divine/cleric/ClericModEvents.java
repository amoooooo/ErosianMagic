package aster.amo.erosianmagic.divine.cleric;

import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = "erosianmagic", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClericModEvents {

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event){
        if(event.getEntity().level().isClientSide) return;
        event.getEntity().getCapability(ICleric.INSTANCE).ifPresent((cleric) -> {
            if(cleric.isChosenClass()){
                int spellLeveLBonus = cleric.getTemple().getTempleLevel() / 2;
                event.setSpellLevel(event.getSpellLevel() + spellLeveLBonus);
            }
        });
    }
}
