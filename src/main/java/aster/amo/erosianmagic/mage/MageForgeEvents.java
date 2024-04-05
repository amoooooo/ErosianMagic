package aster.amo.erosianmagic.mage;

import aster.amo.erosianmagic.util.ClassUtils;
import dev.shadowsoffire.placebo.events.ItemUseEvent;
import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "erosianmagic", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MageForgeEvents {
    @SubscribeEvent
    public void scrollUse(ItemUseEvent event) {
        Player player = event.getEntity();
        if(player == null) return;
        if(event.getItemStack().is(ItemRegistry.SCROLL.get())){
            if(!(ClassUtils.getChosenClass(player) instanceof IMage)) {
                player.displayClientMessage(Component.literal("The symbols on the scroll are indecipherable to you."), true);
                event.setCancellationResult(InteractionResult.FAIL);
            }
        }
    }
}
