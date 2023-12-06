package aster.amo.erosianmagic.witch.spellsnspellbooks;

import aster.amo.erosianmagic.ErosianMagic;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class SpellRegistry {
    public static void init(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, ErosianMagic.MODID);
    public static final RegistryObject<AbstractSpell> HEXBLOOD_SPELL = registerSpell(new aster.amo.erosianmagic.witch.spellsnspellbooks.spells.HexbloodSpell());
    public static final RegistryObject<AbstractSpell> PSYCHICSCREAM_SPELL = registerSpell(new aster.amo.erosianmagic.witch.spellsnspellbooks.spells.PsychicScreamSpell());
}
