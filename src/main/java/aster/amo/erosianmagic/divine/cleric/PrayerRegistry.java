package aster.amo.erosianmagic.divine.cleric;

import aster.amo.erosianmagic.divine.cleric.prayers.CastQuickcastSpell;
import aster.amo.erosianmagic.divine.cleric.prayers.GoodNewsSpell;
import aster.amo.erosianmagic.divine.cleric.prayers.HallowSpell;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import aster.amo.erosianmagic.divine.witch.eidolon.spells.CastingSpell;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.registries.Spells;
import net.minecraft.resources.ResourceLocation;

/**
 * Mending✅ WARDING-SACRED
 * Quick Cast: Sacred Flame✅
 * Toll the Dead✅ DEATH-SACRED
 * Guiding Bolt✅📷
 * Inflict Wounds✅ BLOOD-DEATH
 * Cure Wounds✅ SACRED-HARMONY
 * Healing Word✅ HARMONY-HARMONY
 * Bless✅ SACRED-HARMONY-MAGIC
 * Shield of Faith✅ SACRED-WARDING
 * Prayer of Healing✅ HARMONY-SACRED-HARMONY
 * Warding Bond✅ SOUL-WARDING
 * Mass Healing Word✅ HARMONY-SACRED-HARMONY-SACRED
 * Daylight
 * Revivify (touch corpses revives player)
 * Death Ward
 * Sunbeam✅SACRED-FLAME-SACRED
 * Word of Recall ✅ WARDING-MAGIC-SOUL
 * Fire Storm ✅📷FLAME-MAGIC-FLAME
 * Fireball ✅📷FLAME-MAGIC
 * Earthquake✅📷MAGIC-WARDING-WINTER
 * Blight ✅📷WICKED-DEATH-MAGIC
 * Wall of fire ✅📷FLAME-FLAME-WINTER
 * Beacon of hope ✅SACRED-WARDING-MIND
 */
public class PrayerRegistry {
    public static void init() {}
    public static final Spell HALLOW = Spells.register(new HallowSpell(new ResourceLocation("erosianmagic", "hallow"), Deities.LIGHT_DEITY, Signs.WARDING_SIGN, Signs.SOUL_SIGN, Signs.SACRED_SIGN, Signs.SOUL_SIGN, Signs.WARDING_SIGN));
    public static final Spell GOOD_NEWS = Spells.register(new GoodNewsSpell(new ResourceLocation("erosianmagic", "good_news"), Deities.LIGHT_DEITY, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN));
    public static final Spell SACRED_FLAME = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "sacred_flame"), Deities.LIGHT_DEITY, SpellRegistry.SACRED_FLAME, 1, 0.1f, Signs.SACRED_SIGN));
    public static final Spell CLERIC_QUICK_CAST = Spells.register(new CastQuickcastSpell(new ResourceLocation("erosianmagic", "cleric_quick_cast"), Deities.LIGHT_DEITY, Signs.MAGIC_SIGN, Signs.SACRED_SIGN));
    public static final Spell MENDING = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "mending"), Deities.LIGHT_DEITY, SpellRegistry.MENDING, 5, Signs.WARDING_SIGN, Signs.SACRED_SIGN));
    public static final Spell TOLL_THE_DEAD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "toll_the_dead"), Deities.LIGHT_DEITY, SpellRegistry.TOLL_THE_DEAD, 10, Signs.DEATH_SIGN, Signs.SACRED_SIGN));
    public static final Spell INFLICT_WOUNDS = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "inflict_wounds"), Deities.LIGHT_DEITY, SpellRegistry.INFLICT_WOUNDS, 15, Signs.BLOOD_SIGN, Signs.DEATH_SIGN));
    public static final Spell CURE_WOUNDS = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "cure_wounds"), Deities.LIGHT_DEITY, SpellRegistry.CURE_WOUNDS, 20, Signs.SACRED_SIGN, Signs.HARMONY_SIGN));
    public static final Spell HEALING_WORD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "healing_word"), Deities.LIGHT_DEITY, SpellRegistry.HEALING_WORD, 25, Signs.HARMONY_SIGN, Signs.HARMONY_SIGN));
    public static final Spell BLESS = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "bless"), Deities.LIGHT_DEITY, SpellRegistry.BLESS, 30, Signs.SACRED_SIGN, Signs.HARMONY_SIGN, Signs.MAGIC_SIGN));
    public static final Spell SHIELD_OF_FAITH = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "shield_of_faith"), Deities.LIGHT_DEITY, SpellRegistry.SHIELD_OF_FAITH, 35, Signs.SACRED_SIGN, Signs.WARDING_SIGN));
    public static final Spell PRAYER_OF_HEALING = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "prayer_of_healing"), Deities.LIGHT_DEITY, SpellRegistry.PRAYER_OF_HEALING, 40, Signs.HARMONY_SIGN, Signs.SACRED_SIGN, Signs.HARMONY_SIGN));
    public static final Spell WARDING_BOND = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "warding_bond"), Deities.LIGHT_DEITY, SpellRegistry.WARDING_BOND, 45, Signs.SOUL_SIGN, Signs.WARDING_SIGN));
    public static final Spell MASS_HEALING_WORD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "mass_healing_word"), Deities.LIGHT_DEITY, SpellRegistry.MASS_HEALING_WORD, 50, Signs.HARMONY_SIGN, Signs.SACRED_SIGN, Signs.HARMONY_SIGN, Signs.SACRED_SIGN));
    public static final Spell WORD_OF_RECALL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "word_of_recall"), Deities.LIGHT_DEITY, SpellRegistry.WORD_OF_RECALL, 60, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.SOUL_SIGN));
    public static final Spell FIRE_STORM = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "fire_storm"), Deities.LIGHT_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.BLAZE_STORM_SPELL, 65, Signs.FLAME_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN));
    public static final Spell FIREBALL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "fireball"), Deities.LIGHT_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.FIREBALL_SPELL, 70, Signs.FLAME_SIGN, Signs.MAGIC_SIGN));
    public static final Spell EARTHQUAKE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "earthquake"), Deities.LIGHT_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.EARTHQUAKE_SPELL, 75, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.WINTER_SIGN));
    public static final Spell BLIGHT = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "blight"), Deities.LIGHT_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.BLIGHT_SPELL, 80, Signs.WICKED_SIGN, Signs.DEATH_SIGN, Signs.MAGIC_SIGN));
    public static final Spell WALL_OF_FIRE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "wall_of_fire"), Deities.LIGHT_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.WALL_OF_FIRE_SPELL, 85, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.WINTER_SIGN));
    public static final Spell BEACON_OF_HOPE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "beacon_of_hope"), Deities.LIGHT_DEITY, SpellRegistry.BEACON_OF_HOPE, 90, Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.MIND_SIGN));

}
