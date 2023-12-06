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

    public static final Spell MAGIC_MISSILE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "magic_missile"), Deities.DARK_DEITY, SpellRegistry.MAGIC_MISSILE_SPELL, 1, 0.1f, Signs.MAGIC_SIGN));
    public static final Spell HEXBLOOD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "hexblood"), Deities.DARK_DEITY, HEXBLOOD_SPELL, 5, Signs.WICKED_SIGN, Signs.BLOOD_SIGN));
    public static final Spell RAY_OF_SIPHONING = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "ray_of_siphoning"), Deities.DARK_DEITY, SpellRegistry.RAY_OF_SIPHONING_SPELL, 10, Signs.WICKED_SIGN, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN));
    public static final Spell WITHER_SKULL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "wither_skull"), Deities.DARK_DEITY, SpellRegistry.WITHER_SKULL_SPELL, 15, Signs.WICKED_SIGN, Signs.DEATH_SIGN));
    public static final Spell BLACK_HOLE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "black_hole"), Deities.DARK_DEITY, SpellRegistry.BLACK_HOLE_SPELL, 20, Signs.WICKED_SIGN, Signs.BLOOD_SIGN, Signs.DEATH_SIGN, Signs.DEATH_SIGN, Signs.DEATH_SIGN, Signs.SOUL_SIGN));//, Signs.WICKED_SIGN, Signs.DEATH_SIGN));
    public static void init() {}
}
