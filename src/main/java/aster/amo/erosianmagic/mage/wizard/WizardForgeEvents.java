package aster.amo.erosianmagic.mage.wizard;

import aster.amo.erosianmagic.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = "erosianmagic")
public class WizardForgeEvents {

    @SubscribeEvent
    public static void onWizardSpellCast(SpellOnCastEvent event) {
        Player player = event.getEntity();
        player.getCapability(IWizard.INSTANCE).ifPresent(wizard -> {
            if(wizard.isChosenClass()) {
                int overPowerLevel = (int) player.getAttributeValue(AttributeRegistry.OVERPOWER_LEVEL.get());
                if(overPowerLevel > 0) {
                    event.setSpellLevel(event.getSpellLevel() + overPowerLevel);
                }
            }
        });
    }
}
