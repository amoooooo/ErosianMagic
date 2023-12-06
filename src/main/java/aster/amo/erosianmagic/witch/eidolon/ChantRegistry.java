package aster.amo.erosianmagic.witch.eidolon;

import aster.amo.erosianmagic.witch.eidolon.spells.CastingSpell;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.registries.Spells;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.resources.ResourceLocation;

import static aster.amo.erosianmagic.witch.spellsnspellbooks.SpellRegistry.HEXBLOOD_SPELL;

// TODO: Possible JSON-ify this?
public class ChantRegistry {

    public static final Spell MAGIC_MISSILE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "magic_missile"), Deities.DARK_DEITY, SpellRegistry.MAGIC_MISSILE_SPELL, Signs.WICKED_SIGN));//, Signs.WICKED_SIGN, Signs.DEATH_SIGN));
    public static final Spell HEXBLOOD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "hexblood"), Deities.DARK_DEITY, HEXBLOOD_SPELL, Signs.WICKED_SIGN, Signs.BLOOD_SIGN));//, Signs.WICKED_SIGN, Signs.DEATH_SIGN));
    public static final Spell BLACK_HOLE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "black_hole"), Deities.DARK_DEITY, SpellRegistry.BLACK_HOLE_SPELL, Signs.WICKED_SIGN, Signs.BLOOD_SIGN, Signs.DEATH_SIGN, Signs.DEATH_SIGN, Signs.DEATH_SIGN, Signs.SOUL_SIGN));//, Signs.WICKED_SIGN, Signs.DEATH_SIGN));
    public static void init() {}
}
