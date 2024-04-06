package aster.amo.erosianmagic.fighter.barbarian;

import aster.amo.erosianmagic.mage.IMage;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import aster.amo.erosianmagic.util.ClassUtils;
import dev.shadowsoffire.placebo.events.ItemUseEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "erosianmagic", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BarbarianForgeEvents {
    @SubscribeEvent
    public void barbarianRageSpell(SpellSelectionManager.SpellSelectionEvent event) {
        Player player = event.getEntity();
        if(player == null) return;
        player.getCapability(IBarbarian.INSTANCE).ifPresent((barbarian) -> {
            if(barbarian.isChosenClass()) {
                if(event.getManager().getAllSpells().stream().noneMatch(option -> option.spellData.getSpell().equals(SpellRegistry.RAGE.get()))) {
                    event.addSelectionOption(new SpellData(SpellRegistry.RAGE.get(), 5), "custom", 0, 0);
                }
            }
        });
    }
}
