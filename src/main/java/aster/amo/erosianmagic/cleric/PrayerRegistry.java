package aster.amo.erosianmagic.cleric;

import aster.amo.erosianmagic.cleric.prayers.GoodNewsSpell;
import aster.amo.erosianmagic.cleric.prayers.HallowSpell;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import aster.amo.erosianmagic.witch.eidolon.spells.CastingSpell;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.registries.Spells;
import net.minecraft.resources.ResourceLocation;

public class PrayerRegistry {
    public static void init() {}
    public static final Spell HALLOW = Spells.register(new HallowSpell(new ResourceLocation("erosianmagic", "hallow"), Deities.LIGHT_DEITY, Signs.WARDING_SIGN, Signs.SOUL_SIGN, Signs.SACRED_SIGN, Signs.SOUL_SIGN, Signs.WARDING_SIGN));
    public static final Spell GOOD_NEWS = Spells.register(new GoodNewsSpell(new ResourceLocation("erosianmagic", "good_news"), Deities.LIGHT_DEITY, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN));
    public static final Spell SACRED_FLAME = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "sacred_flame"), Deities.LIGHT_DEITY, SpellRegistry.SACRED_FLAME, 1, 0.1f, Signs.SACRED_SIGN));

}
