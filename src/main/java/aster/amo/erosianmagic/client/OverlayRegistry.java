package aster.amo.erosianmagic.client;

import aster.amo.erosianmagic.bard.BardSpellBarOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class OverlayRegistry {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "bard_cooldowns", BardSpellBarOverlay.instance);
    }
}
