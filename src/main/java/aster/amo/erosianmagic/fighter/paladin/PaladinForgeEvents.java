package aster.amo.erosianmagic.fighter.paladin;

import aster.amo.erosianmagic.util.ClassUtils;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class PaladinForgeEvents {
    @SubscribeEvent
    public static void paladinDivineSmite(SpellSelectionManager.SpellSelectionEvent event) {
        Player player = event.getEntity();
        boolean isPaladin = Objects.equals(ClassUtils.getChosenClassName(player), "Paladin");
        player.getCapability(IPaladin.INSTANCE).ifPresent((paladin) -> {
            if(isPaladin) {
                boolean noneAreRage = event.getManager().getAllSpells().stream().noneMatch(option -> option.spellData.getSpell().equals(SpellRegistry.DIVINE_SMITE_SPELL.get()));
                if(noneAreRage) {
                    event.addSelectionOption(new SpellData(SpellRegistry.DIVINE_SMITE_SPELL.get(), paladin.getLevel()), "custom", 0);
                }
            }
        });
    }
}
