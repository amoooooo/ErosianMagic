package aster.amo.erosianmagic.witch.spellsnspellbooks;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.RegistryObject;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.registerSpell;

public class SpellRegistry {
    public static void init(){}
    public static final RegistryObject<AbstractSpell> HEXBLOOD_SPELL = registerSpell(new aster.amo.erosianmagic.witch.spellsnspellbooks.spells.HexbloodSpell());
}
