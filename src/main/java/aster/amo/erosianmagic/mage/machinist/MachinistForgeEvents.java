package aster.amo.erosianmagic.mage.machinist;

import dev.shadowsoffire.placebo.events.ItemUseEvent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = "erosianmagic", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MachinistForgeEvents {
    @SubscribeEvent
    public void machinist$onItemUsedEvent(ItemUseEvent event) {
        AtomicBoolean isMachinist = new AtomicBoolean(false);
        event.getEntity().getCapability(IMachinist.INSTANCE).ifPresent((k) -> {
            if(k.isChosenClass()) {
                isMachinist.set(true);
            }
        });
        if(!isMachinist.get()) {
            event.getEntity().displayClientMessage(Component.literal("You don't understand how to use this item!"), true);
            event.setCanceled(true);
        }
    }
}
