package aster.amo.erosianmagic.cleric;

import aster.amo.erosianmagic.cleric.prayers.GoodNewsSpell;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.registries.Spells;
import net.minecraft.resources.ResourceLocation;

public class PrayerRegistry {
    public static void init() {}
    public static final Spell GOOD_NEWS = Spells.register(new GoodNewsSpell(new ResourceLocation("erosianmagic", "good_news"), Deities.LIGHT_DEITY, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN));
}
