package aster.amo.erosianmagic.fighter.barbarian;

import aster.amo.erosianmagic.client.ClientSpellHandler;
import aster.amo.erosianmagic.mage.IMage;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.ClientClassUtils;
import aster.amo.erosianmagic.util.IClass;
import dev.shadowsoffire.placebo.events.ItemUseEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class BarbarianForgeEvents {
    @SubscribeEvent
    public static void barbarianRageSpell(SpellSelectionManager.SpellSelectionEvent event) {
        Player player = event.getEntity();
        boolean isBarbarian = Objects.equals(ClassUtils.getChosenClassName(player), "Barbarian");
        player.getCapability(IBarbarian.INSTANCE).ifPresent((barbarian) -> {
            if(isBarbarian) {
                boolean noneAreRage = event.getManager().getAllSpells().stream().noneMatch(option -> option.spellData.getSpell().equals(SpellRegistry.RAGE.get()));
                if(noneAreRage) {
                    event.addSelectionOption(new SpellData(SpellRegistry.RAGE.get(), barbarian.getLevel()), "custom", 0);
                }
            }
        });
    }
}
