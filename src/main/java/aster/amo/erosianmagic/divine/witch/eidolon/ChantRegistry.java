package aster.amo.erosianmagic.divine.witch.eidolon;

import aster.amo.erosianmagic.divine.cleric.prayers.CastQuickcastSpell;
import aster.amo.erosianmagic.divine.witch.eidolon.spells.CastingSpell;
import aster.amo.erosianmagic.divine.witch.eidolon.spells.CovenChant;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.registries.Spells;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.resources.ResourceLocation;

import static aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.HEXBLOOD_SPELL;

// TODO: Implement "Chant Power" attribute
public class ChantRegistry {

    public static final Spell WITCH_QUICK_CAST = Spells.register(new CastQuickcastSpell(new ResourceLocation("erosianmagic", "witch_quick_cast"), Deities.DARK_DEITY, Signs.MAGIC_SIGN, Signs.WICKED_SIGN));
    public static final Spell COVEN_RECRUITMENT = Spells.register(new CovenChant(new ResourceLocation("erosianmagic", "coven_recruitment"), Signs.WICKED_SIGN, Signs.BLOOD_SIGN));
    public static final Spell COUNTERSPELL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "counterspell"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.COUNTERSPELL_SPELL, 55, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.MIND_SIGN));

    public static final Spell DRAGON_BREATH = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "dragon_breath"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.DRAGON_BREATH_SPELL, 60, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN));

    public static final Spell MAGIC_MISSILE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "magic_missile"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.MAGIC_MISSILE_SPELL, 65, Signs.MAGIC_SIGN, Signs.MIND_SIGN, Signs.MAGIC_SIGN));

    public static final Spell STARFALL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "starfall"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.STARFALL_SPELL, 70, Signs.WINTER_SIGN, Signs.SACRED_SIGN, Signs.MAGIC_SIGN));

    public static final Spell SUMMON_ENDER_CHEST = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "summon_ender_chest"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.SUMMON_ENDER_CHEST_SPELL, 75, Signs.WARDING_SIGN, Signs.WARDING_SIGN, Signs.SOUL_SIGN));

    public static final Spell RECALL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "recall"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.RECALL_SPELL, 80, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.SOUL_SIGN));

    public static final Spell PORTAL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "portal"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.PORTAL_SPELL, 85, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN));

    public static final Spell HEXBLOOD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "hexblood"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.HEXBLOOD_SPELL, 90, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN));

    public static final Spell ACUPUNCTURE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "acupuncture"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.ACUPUNCTURE_SPELL, 95, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.MIND_SIGN, Signs.WARDING_SIGN));

    public static final Spell BLOOD_NEEDLES = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "blood_needles"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.BLOOD_NEEDLES_SPELL, 100, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.WARDING_SIGN));

    public static final Spell BLOOD_SLASH = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "blood_slash"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.BLOOD_SLASH_SPELL, 105, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.DEATH_SIGN));

    public static final Spell DEVOUR = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "devour"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.DEVOUR_SPELL, 110, Signs.BLOOD_SIGN, Signs.SOUL_SIGN, Signs.WARDING_SIGN, Signs.DEATH_SIGN));

    public static final Spell RAY_OF_SIPHONING = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "ray_of_siphoning"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.RAY_OF_SIPHONING_SPELL, 115, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.DEATH_SIGN, Signs.WARDING_SIGN));

    public static final Spell SUMMON_VEX = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "summon_vex"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.SUMMON_VEX_SPELL, 120, Signs.WARDING_SIGN, Signs.SOUL_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN));

    public static final Spell ABYSSAL_SHROUD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "abyssal_shroud"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.ABYSSAL_SHROUD_SPELL, 125, Signs.WARDING_SIGN, Signs.DEATH_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN));

    public static final Spell SCULK_TENTACLES = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "sculk_tentacles"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.SCULK_TENTACLES_SPELL, 130, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.DEATH_SIGN));

    public static final Spell SONIC_BOOM = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "sonic_boom"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.SONIC_BOOM_SPELL, 135, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN));

    public static final Spell PLANAR_SIGHT = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "planar_sight"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.PLANAR_SIGHT_SPELL, 140, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WINTER_SIGN, Signs.WARDING_SIGN));

    public static final Spell TELEKINESIS = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "telekinesis"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.TELEKINESIS_SPELL, 145, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN));

    public static final Spell ELDRITCH_BLAST = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "eldritch_blast"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.ELDRITCH_BLAST, 150, 0.1f, Signs.WICKED_SIGN));

    public static final Spell BESTOW_CURSE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "bestow_curse"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.BESTOW_CURSE, 155, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.DEATH_SIGN, Signs.FLAME_SIGN));

    public static final Spell SHOCKWAVE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "shockwave"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.SHOCKWAVE_SPELL, 160, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN));

    public static final Spell ACID_ORB = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "acid_orb"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.ACID_ORB_SPELL, 165, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.SACRED_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN));

    public static final Spell SPIDER_ASPECT = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "spider_aspect"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPIDER_ASPECT_SPELL, 170, Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.SOUL_SIGN, Signs.WINTER_SIGN, Signs.FLAME_SIGN));

    public static final Spell FIREFLY_SWARM = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "firefly_swarm"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.FIREFLY_SWARM_SPELL, 175, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.SACRED_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN));

    public static final Spell STOMP = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "stomp"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.STOMP_SPELL, 180, Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN, Signs.WINTER_SIGN, Signs.SACRED_SIGN));

    public static final Spell GLUTTONY = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "gluttony"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.GLUTTONY_SPELL, 190, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.SOUL_SIGN, Signs.DEATH_SIGN, Signs.FLAME_SIGN));
    public static final Spell FIRE_BREATH = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "fire_breath"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.FIRE_BREATH_SPELL, 200, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN));

    public static final Spell MAGMA_BOMB = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "magma_bomb"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.MAGMA_BOMB_SPELL, 205, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN));

    public static final Spell HEAT_SURGE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "heat_surge"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.HEAT_SURGE_SPELL, 210, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.MAGIC_SIGN));

    public static final Spell CONE_OF_COLD = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "cone_of_cold"), Deities.DARK_DEITY, io.redspace.ironsspellbooks.api.registry.SpellRegistry.CONE_OF_COLD_SPELL, 220, Signs.WINTER_SIGN, Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN));
    public static final Spell MAGIC_WEAPON = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "magic_weapon"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.MAGIC_WEAPON, 245, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.HARMONY_SIGN));

    public static final Spell WARDING_WIND = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "warding_wind"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.WARDING_WIND, 250, Signs.WARDING_SIGN, Signs.WARDING_SIGN, Signs.WARDING_SIGN));

    public static final Spell HEAT_METAL = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "heat_metal"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.HEAT_METAL, 255, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN));

    public static final Spell FAERIE_FIRE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "faerie_fire"), Deities.DARK_DEITY, aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.FAERIE_FIRE, 260, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN));

    public static final Spell ELECTROCUTE = Spells.register(new CastingSpell(new ResourceLocation("erosianmagic", "electrocute"), Deities.DARK_DEITY, SpellRegistry.ELECTROCUTE_SPELL, 265, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN));

    public static void init() {}
}
