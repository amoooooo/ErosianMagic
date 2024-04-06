package aster.amo.erosianmagic.divine.witch.eidolon;

import aster.amo.erosianmagic.client.chapter.PredicatedIndex;
import aster.amo.erosianmagic.client.pages.SignClassSpellLockedEntry;
import aster.amo.erosianmagic.client.pages.SortedIndexPage;
import aster.amo.erosianmagic.divine.cleric.PrayerRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import elucent.eidolon.codex.*;
import elucent.eidolon.registries.Registry;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.*;
import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.*;

public class BookRegistry {
    public static Category MYSTIC_ARTS;
    public static Index MYSTIC_ARTS_INDEX;
    public static Chapter MAGIC_MISSILE_CHAPTER;
    public static Chapter MENDING_CHAPTER;
    public static Chapter TOLL_THE_DEAD_CHAPTER;
    public static Chapter INFILCT_WOUNDS_CHAPTER;
    public static Chapter CURE_WOUNDS_CHAPTER;
    public static Chapter HEALING_WORD_CHAPTER;
    public static Chapter BLESS_CHAPTER;
    public static Chapter SHIELD_OF_FAITH_CHAPTER;
    public static Chapter PRAYER_OF_HEALING_CHAPTER;
    public static Chapter WARDING_BOND_CHAPTER;
    public static Chapter MASS_HEALING_WORD_CHAPTER;
    public static Chapter WORD_OF_RECALL_CHAPTER;
    public static Chapter FIRE_STORM_CHAPTER;
    public static Chapter FIREBALL_CHAPTER;
    public static Chapter EARTHQUAKE_CHAPTER;
    public static Chapter BLIGHT_CHAPTER;
    public static Chapter WALL_OF_FIRE_CHAPTER;
    public static Chapter BEACON_OF_HOPE_CHAPTER;
    public static Chapter COUNTERSPELL_CHAPTER = new Chapter("erosianmagic.codex.chapter.counterspell",
            new ChantPage("erosianmagic.codex.page.counterspell.0", ChantRegistry.COUNTERSPELL),
            new TextPage("erosianmagic.codex.page.counterspell.1"));
    public static Chapter DRAGON_BREATH_CHAPTER = new Chapter("erosianmagic.codex.chapter.dragon_breath",
            new ChantPage("erosianmagic.codex.page.dragon_breath.0", ChantRegistry.DRAGON_BREATH),
            new TextPage("erosianmagic.codex.page.dragon_breath.1"));
    public static Chapter STARFALL_CHAPTER = new Chapter("erosianmagic.codex.chapter.starfall",
            new ChantPage("erosianmagic.codex.page.starfall.0", ChantRegistry.STARFALL),
            new TextPage("erosianmagic.codex.page.starfall.1"));
    public static Chapter SUMMON_ENDER_CHEST_CHAPTER = new Chapter("erosianmagic.codex.chapter.summon_ender_chest",
            new ChantPage("erosianmagic.codex.page.summon_ender_chest.0", ChantRegistry.SUMMON_ENDER_CHEST),
            new TextPage("erosianmagic.codex.page.summon_ender_chest.1"));
    public static Chapter RECALL_CHAPTER = new Chapter("erosianmagic.codex.chapter.recall",
            new ChantPage("erosianmagic.codex.page.recall.0", ChantRegistry.RECALL),
            new TextPage("erosianmagic.codex.page.recall.1"));
    public static Chapter PORTAL_CHAPTER = new Chapter("erosianmagic.codex.chapter.portal",
            new ChantPage("erosianmagic.codex.page.portal.0", ChantRegistry.PORTAL),
            new TextPage("erosianmagic.codex.page.portal.1"));
    public static Chapter MAGIC_WEAPON_CHAPTER = new Chapter("erosianmagic.codex.chapter.magic_weapon",
            new ChantPage("erosianmagic.codex.page.magic_weapon.0", ChantRegistry.MAGIC_WEAPON),
            new TextPage("erosianmagic.codex.page.magic_weapon.1"));
    public static Chapter WARDING_WIND_CHAPTER = new Chapter("erosianmagic.codex.chapter.warding_wind",
            new ChantPage("erosianmagic.codex.page.warding_wind.0", ChantRegistry.WARDING_WIND),
            new TextPage("erosianmagic.codex.page.warding_wind.1"));
    public static Chapter FIRE_BREATH_CHAPTER = new Chapter("erosianmagic.codex.chapter.fire_breath",
            new ChantPage("erosianmagic.codex.page.fire_breath.0", ChantRegistry.FIRE_BREATH),
            new TextPage("erosianmagic.codex.page.fire_breath.1"));
    public static Chapter MAGMA_BOMB_CHAPTER = new Chapter("erosianmagic.codex.chapter.magma_bomb",
            new ChantPage("erosianmagic.codex.page.magma_bomb.0", ChantRegistry.MAGMA_BOMB),
            new TextPage("erosianmagic.codex.page.magma_bomb.1"));
    public static Chapter HEAT_SURGE_CHAPTER = new Chapter("erosianmagic.codex.chapter.heat_surge",
            new ChantPage("erosianmagic.codex.page.heat_surge.0", ChantRegistry.HEAT_SURGE),
            new TextPage("erosianmagic.codex.page.heat_surge.1"));
    public static Chapter HEAT_METAL_CHAPTER = new Chapter("erosianmagic.codex.chapter.heat_metal",
            new ChantPage("erosianmagic.codex.page.heat_metal.0", ChantRegistry.HEAT_METAL),
            new TextPage("erosianmagic.codex.page.heat_metal.1"));
    public static Chapter FAERIE_FIRE_CHAPTER = new Chapter("erosianmagic.codex.chapter.faerie_fire",
            new ChantPage("erosianmagic.codex.page.faerie_fire.0", ChantRegistry.FAERIE_FIRE),
            new TextPage("erosianmagic.codex.page.faerie_fire.1"));
    public static Chapter ACUPUNCTURE_CHAPTER = new Chapter("erosianmagic.codex.chapter.acupuncture",
            new ChantPage("erosianmagic.codex.page.acupuncture.0", ChantRegistry.ACUPUNCTURE),
            new TextPage("erosianmagic.codex.page.acupuncture.1"));
    public static Chapter BLOOD_NEEDLES_CHAPTER = new Chapter("erosianmagic.codex.chapter.blood_needles",
            new ChantPage("erosianmagic.codex.page.blood_needles.0", ChantRegistry.BLOOD_NEEDLES),
            new TextPage("erosianmagic.codex.page.blood_needles.1"));
    public static Chapter BLOOD_SLASH_CHAPTER = new Chapter("erosianmagic.codex.chapter.blood_slash",
            new ChantPage("erosianmagic.codex.page.blood_slash.0", ChantRegistry.BLOOD_SLASH),
            new TextPage("erosianmagic.codex.page.blood_slash.1"));
    public static Chapter DEVOUR_CHAPTER = new Chapter("erosianmagic.codex.chapter.devour",
            new ChantPage("erosianmagic.codex.page.devour.0", ChantRegistry.DEVOUR),
            new TextPage("erosianmagic.codex.page.devour.1"));
    public static Chapter RAY_OF_SIPHONING_CHAPTER = new Chapter("erosianmagic.codex.chapter.ray_of_siphoning",
            new ChantPage("erosianmagic.codex.page.ray_of_siphoning.0", ChantRegistry.RAY_OF_SIPHONING),
            new TextPage("erosianmagic.codex.page.ray_of_siphoning.1"));
    public static Chapter TOLL_THE_DEAD_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.toll_the_dead",
            new ChantPage("erosianmagic.codex.page.toll_the_dead.0", PrayerRegistry.TOLL_THE_DEAD),
            new TextPage("erosianmagic.codex.page.toll_the_dead.1"));
    public static Chapter SUMMON_VEX_CHAPTER = new Chapter("erosianmagic.codex.chapter.summon_vex",
            new ChantPage("erosianmagic.codex.page.summon_vex.0", ChantRegistry.SUMMON_VEX),
            new TextPage("erosianmagic.codex.page.summon_vex.1"));
    public static Chapter ABYSSAL_SHROUD_CHAPTER = new Chapter("erosianmagic.codex.chapter.abyssal_shroud",
            new ChantPage("erosianmagic.codex.page.abyssal_shroud.0", ChantRegistry.ABYSSAL_SHROUD),
            new TextPage("erosianmagic.codex.page.abyssal_shroud.1"));
    public static Chapter SCULK_TENTACLES_CHAPTER = new Chapter("erosianmagic.codex.chapter.sculk_tentacles",
            new ChantPage("erosianmagic.codex.page.sculk_tentacles.0", ChantRegistry.SCULK_TENTACLES),
            new TextPage("erosianmagic.codex.page.sculk_tentacles.1"));
    public static Chapter SONIC_BOOM_CHAPTER = new Chapter("erosianmagic.codex.chapter.sonic_boom",
            new ChantPage("erosianmagic.codex.page.sonic_boom.0", ChantRegistry.SONIC_BOOM),
            new TextPage("erosianmagic.codex.page.sonic_boom.1"));
    public static Chapter PLANAR_SIGHT_CHAPTER = new Chapter("erosianmagic.codex.chapter.planar_sight",
            new ChantPage("erosianmagic.codex.page.planar_sight.0", ChantRegistry.PLANAR_SIGHT),
            new TextPage("erosianmagic.codex.page.planar_sight.1"));
    public static Chapter TELEKINESIS_CHAPTER = new Chapter("erosianmagic.codex.chapter.telekinesis",
            new ChantPage("erosianmagic.codex.page.telekinesis.0", ChantRegistry.TELEKINESIS),
            new TextPage("erosianmagic.codex.page.telekinesis.1"));
    public static Chapter ELDRITCH_BLAST_CHAPTER = new Chapter("erosianmagic.codex.chapter.eldritch_blast",
            new ChantPage("erosianmagic.codex.page.eldritch_blast.0", ChantRegistry.ELDRITCH_BLAST),
            new TextPage("erosianmagic.codex.page.eldritch_blast.1"));
    public static Chapter BESTOW_CURSE_CHAPTER = new Chapter("erosianmagic.codex.chapter.bestow_curse",
            new ChantPage("erosianmagic.codex.page.bestow_curse.0", ChantRegistry.BESTOW_CURSE),
            new TextPage("erosianmagic.codex.page.bestow_curse.1"));
    public static Chapter CONE_OF_COLD_CHAPTER = new Chapter("erosianmagic.codex.chapter.cone_of_cold",
            new ChantPage("erosianmagic.codex.page.cone_of_cold.0", ChantRegistry.CONE_OF_COLD),
            new TextPage("erosianmagic.codex.page.cone_of_cold.1"));
    public static Chapter ELECTROCUTE_CHAPTER = new Chapter("erosianmagic.codex.chapter.electrocute",
            new ChantPage("erosianmagic.codex.page.electrocute.0", ChantRegistry.ELECTROCUTE),
            new TextPage("erosianmagic.codex.page.electrocute.1"));
    public static Chapter SHOCKWAVE_CHAPTER = new Chapter("erosianmagic.codex.chapter.shockwave",
        new ChantPage("erosianmagic.codex.page.shockwave.0", ChantRegistry.SHOCKWAVE),
            new TextPage("erosianmagic.codex.page.shockwave.1"));
    public static Chapter BLESS_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.bless",
        new ChantPage("erosianmagic.codex.page.bless.0", PrayerRegistry.BLESS),
            new TextPage("erosianmagic.codex.page.bless.1"));
    public static Chapter PRAYER_OF_HEALING_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.prayer_of_healing",
        new ChantPage("erosianmagic.codex.page.prayer_of_healing.0", PrayerRegistry.PRAYER_OF_HEALING),
            new TextPage("erosianmagic.codex.page.prayer_of_healing.1"));
    public static Chapter WORD_OF_RECALL_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.word_of_recall",
        new ChantPage("erosianmagic.codex.page.word_of_recall.0", PrayerRegistry.WORD_OF_RECALL),
            new TextPage("erosianmagic.codex.page.word_of_recall.1"));
    public static Chapter ACID_ORB_CHAPTER = new Chapter("erosianmagic.codex.chapter.acid_orb",
        new ChantPage("erosianmagic.codex.page.acid_orb.0", ChantRegistry.ACID_ORB),
            new TextPage("erosianmagic.codex.page.acid_orb.1"));
    public static Chapter BLIGHT_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.blight",
        new ChantPage("erosianmagic.codex.page.blight.0", PrayerRegistry.BLIGHT),
            new TextPage("erosianmagic.codex.page.blight.1"));
    public static Chapter SPIDER_ASPECT_CHAPTER = new Chapter("erosianmagic.codex.chapter.spider_aspect",
        new ChantPage("erosianmagic.codex.page.spider_aspect.0", ChantRegistry.SPIDER_ASPECT),
            new TextPage("erosianmagic.codex.page.spider_aspect.1"));
    public static Chapter FIREFLY_SWARM_CHAPTER = new Chapter("erosianmagic.codex.chapter.firefly_swarm",
        new ChantPage("erosianmagic.codex.page.firefly_swarm.0", ChantRegistry.FIREFLY_SWARM),
            new TextPage("erosianmagic.codex.page.firefly_swarm.1"));
    public static Chapter STOMP_CHAPTER = new Chapter("erosianmagic.codex.chapter.stomp",
        new ChantPage("erosianmagic.codex.page.stomp.0", ChantRegistry.STOMP),
            new TextPage("erosianmagic.codex.page.stomp.1"));
    public static Chapter EARTHQUAKE_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.earthquake",
        new ChantPage("erosianmagic.codex.page.earthquake.0", PrayerRegistry.EARTHQUAKE),
            new TextPage("erosianmagic.codex.page.earthquake.1"));
    public static Chapter GLUTTONY_CHAPTER = new Chapter("erosianmagic.codex.chapter.gluttony",
        new ChantPage("erosianmagic.codex.page.gluttony.0", ChantRegistry.GLUTTONY),
            new TextPage("erosianmagic.codex.page.gluttony.1"));

    public static void initClericChapters() {
        MENDING_CHAPTER = new Chapter("erosianmagic.codex.chapter.mending",
                new ChantPage("erosianmagic.codex.page.mending.0", PrayerRegistry.MENDING),
                new TextPage("erosianmagic.codex.page.mending.1"));
        TOLL_THE_DEAD_CHAPTER = new Chapter("erosianmagic.codex.chapter.toll_the_dead",
                new ChantPage("erosianmagic.codex.page.toll_the_dead.0", PrayerRegistry.TOLL_THE_DEAD),
                new TextPage("erosianmagic.codex.page.toll_the_dead.1"));
        INFILCT_WOUNDS_CHAPTER = new Chapter("erosianmagic.codex.chapter.inflict_wounds",
                new ChantPage("erosianmagic.codex.page.inflict_wounds.0", PrayerRegistry.INFLICT_WOUNDS),
                new TextPage("erosianmagic.codex.page.inflict_wounds.1"));
        CURE_WOUNDS_CHAPTER = new Chapter("erosianmagic.codex.chapter.cure_wounds",
                new ChantPage("erosianmagic.codex.page.cure_wounds.0", PrayerRegistry.CURE_WOUNDS),
                new TextPage("erosianmagic.codex.page.cure_wounds.1"));
        HEALING_WORD_CHAPTER = new Chapter("erosianmagic.codex.chapter.healing_word",
                new ChantPage("erosianmagic.codex.page.healing_word.0", PrayerRegistry.HEALING_WORD),
                new TextPage("erosianmagic.codex.page.healing_word.1"));
        BLESS_CHAPTER = new Chapter("erosianmagic.codex.chapter.bless",
                new ChantPage("erosianmagic.codex.page.bless.0", PrayerRegistry.BLESS),
                new TextPage("erosianmagic.codex.page.bless.1"));
        SHIELD_OF_FAITH_CHAPTER = new Chapter("erosianmagic.codex.chapter.shield_of_faith",
                new ChantPage("erosianmagic.codex.page.shield_of_faith.0", PrayerRegistry.SHIELD_OF_FAITH),
                new TextPage("erosianmagic.codex.page.shield_of_faith.1"));
        PRAYER_OF_HEALING_CHAPTER = new Chapter("erosianmagic.codex.chapter.prayer_of_healing",
                new ChantPage("erosianmagic.codex.page.prayer_of_healing.0", PrayerRegistry.PRAYER_OF_HEALING),
                new TextPage("erosianmagic.codex.page.prayer_of_healing.1"));
        WARDING_BOND_CHAPTER = new Chapter("erosianmagic.codex.chapter.warding_bond",
                new ChantPage("erosianmagic.codex.page.warding_bond.0", PrayerRegistry.WARDING_BOND),
                new TextPage("erosianmagic.codex.page.warding_bond.1"));
        MASS_HEALING_WORD_CHAPTER = new Chapter("erosianmagic.codex.chapter.mass_healing_word",
                new ChantPage("erosianmagic.codex.page.mass_healing_word.0", PrayerRegistry.MASS_HEALING_WORD),
                new TextPage("erosianmagic.codex.page.mass_healing_word.1"));
        WORD_OF_RECALL_CHAPTER = new Chapter("erosianmagic.codex.chapter.word_of_recall",
                new ChantPage("erosianmagic.codex.page.word_of_recall.0", PrayerRegistry.WORD_OF_RECALL),
                new TextPage("erosianmagic.codex.page.word_of_recall.1"));
        FIRE_STORM_CHAPTER = new Chapter("erosianmagic.codex.chapter.fire_storm",
                new ChantPage("erosianmagic.codex.page.fire_storm.0", PrayerRegistry.FIRE_STORM),
                new TextPage("erosianmagic.codex.page.fire_storm.1"));
        FIREBALL_CHAPTER = new Chapter("erosianmagic.codex.chapter.fireball",
                new ChantPage("erosianmagic.codex.page.fireball.0", PrayerRegistry.FIREBALL),
                new TextPage("erosianmagic.codex.page.fireball.1"));
        EARTHQUAKE_CHAPTER = new Chapter("erosianmagic.codex.chapter.earthquake",
                new ChantPage("erosianmagic.codex.page.earthquake.0", PrayerRegistry.EARTHQUAKE),
                new TextPage("erosianmagic.codex.page.earthquake.1"));
        BLIGHT_CHAPTER = new Chapter("erosianmagic.codex.chapter.blight",
                new ChantPage("erosianmagic.codex.page.blight.0", PrayerRegistry.BLIGHT),
                new TextPage("erosianmagic.codex.page.blight.1"));
        WALL_OF_FIRE_CHAPTER = new Chapter("erosianmagic.codex.chapter.wall_of_fire",
                new ChantPage("erosianmagic.codex.page.wall_of_fire.0", PrayerRegistry.WALL_OF_FIRE),
                new TextPage("erosianmagic.codex.page.wall_of_fire.1"));
        BEACON_OF_HOPE_CHAPTER = new Chapter("erosianmagic.codex.chapter.beacon_of_hope",
                new ChantPage("erosianmagic.codex.page.beacon_of_hope.0", PrayerRegistry.BEACON_OF_HOPE),
                new TextPage("erosianmagic.codex.page.beacon_of_hope.1"));

    }
    
    public static void initWitchChapters() {
        COUNTERSPELL_CHAPTER = new Chapter("erosianmagic.codex.chapter.counterspell",
                new ChantPage("erosianmagic.codex.page.counterspell.0", ChantRegistry.COUNTERSPELL),
                new TextPage("erosianmagic.codex.page.counterspell.1"));
        DRAGON_BREATH_CHAPTER = new Chapter("erosianmagic.codex.chapter.dragon_breath",
                new ChantPage("erosianmagic.codex.page.dragon_breath.0", ChantRegistry.DRAGON_BREATH),
                new TextPage("erosianmagic.codex.page.dragon_breath.1"));
        STARFALL_CHAPTER = new Chapter("erosianmagic.codex.chapter.starfall",
                new ChantPage("erosianmagic.codex.page.starfall.0", ChantRegistry.STARFALL),
                new TextPage("erosianmagic.codex.page.starfall.1"));
        SUMMON_ENDER_CHEST_CHAPTER = new Chapter("erosianmagic.codex.chapter.summon_ender_chest",
                new ChantPage("erosianmagic.codex.page.summon_ender_chest.0", ChantRegistry.SUMMON_ENDER_CHEST),
                new TextPage("erosianmagic.codex.page.summon_ender_chest.1"));
        RECALL_CHAPTER = new Chapter("erosianmagic.codex.chapter.recall",
                new ChantPage("erosianmagic.codex.page.recall.0", ChantRegistry.RECALL),
                new TextPage("erosianmagic.codex.page.recall.1"));
        PORTAL_CHAPTER = new Chapter("erosianmagic.codex.chapter.portal",
                new ChantPage("erosianmagic.codex.page.portal.0", ChantRegistry.PORTAL),
                new TextPage("erosianmagic.codex.page.portal.1"));
        MAGIC_WEAPON_CHAPTER = new Chapter("erosianmagic.codex.chapter.magic_weapon",
                new ChantPage("erosianmagic.codex.page.magic_weapon.0", ChantRegistry.MAGIC_WEAPON),
                new TextPage("erosianmagic.codex.page.magic_weapon.1"));
        WARDING_WIND_CHAPTER = new Chapter("erosianmagic.codex.chapter.warding_wind",
                new ChantPage("erosianmagic.codex.page.warding_wind.0", ChantRegistry.WARDING_WIND),
                new TextPage("erosianmagic.codex.page.warding_wind.1"));
        FIRE_BREATH_CHAPTER = new Chapter("erosianmagic.codex.chapter.fire_breath",
                new ChantPage("erosianmagic.codex.page.fire_breath.0", ChantRegistry.FIRE_BREATH),
                new TextPage("erosianmagic.codex.page.fire_breath.1"));
        MAGMA_BOMB_CHAPTER = new Chapter("erosianmagic.codex.chapter.magma_bomb",
                new ChantPage("erosianmagic.codex.page.magma_bomb.0", ChantRegistry.MAGMA_BOMB),
                new TextPage("erosianmagic.codex.page.magma_bomb.1"));
        HEAT_SURGE_CHAPTER = new Chapter("erosianmagic.codex.chapter.heat_surge",
                new ChantPage("erosianmagic.codex.page.heat_surge.0", ChantRegistry.HEAT_SURGE),
                new TextPage("erosianmagic.codex.page.heat_surge.1"));
        HEAT_METAL_CHAPTER = new Chapter("erosianmagic.codex.chapter.heat_metal",
                new ChantPage("erosianmagic.codex.page.heat_metal.0", ChantRegistry.HEAT_METAL),
                new TextPage("erosianmagic.codex.page.heat_metal.1"));
        FAERIE_FIRE_CHAPTER = new Chapter("erosianmagic.codex.chapter.faerie_fire",
                new ChantPage("erosianmagic.codex.page.faerie_fire.0", ChantRegistry.FAERIE_FIRE),
                new TextPage("erosianmagic.codex.page.faerie_fire.1"));
        ACUPUNCTURE_CHAPTER = new Chapter("erosianmagic.codex.chapter.acupuncture",
                new ChantPage("erosianmagic.codex.page.acupuncture.0", ChantRegistry.ACUPUNCTURE),
                new TextPage("erosianmagic.codex.page.acupuncture.1"));
        BLOOD_NEEDLES_CHAPTER = new Chapter("erosianmagic.codex.chapter.blood_needles",
                new ChantPage("erosianmagic.codex.page.blood_needles.0", ChantRegistry.BLOOD_NEEDLES),
                new TextPage("erosianmagic.codex.page.blood_needles.1"));
        BLOOD_SLASH_CHAPTER = new Chapter("erosianmagic.codex.chapter.blood_slash",
                new ChantPage("erosianmagic.codex.page.blood_slash.0", ChantRegistry.BLOOD_SLASH),
                new TextPage("erosianmagic.codex.page.blood_slash.1"));
        DEVOUR_CHAPTER = new Chapter("erosianmagic.codex.chapter.devour",
                new ChantPage("erosianmagic.codex.page.devour.0", ChantRegistry.DEVOUR),
                new TextPage("erosianmagic.codex.page.devour.1"));
        RAY_OF_SIPHONING_CHAPTER = new Chapter("erosianmagic.codex.chapter.ray_of_siphoning",
                new ChantPage("erosianmagic.codex.page.ray_of_siphoning.0", ChantRegistry.RAY_OF_SIPHONING),
                new TextPage("erosianmagic.codex.page.ray_of_siphoning.1"));
        TOLL_THE_DEAD_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.toll_the_dead",
                new ChantPage("erosianmagic.codex.page.toll_the_dead.0", PrayerRegistry.TOLL_THE_DEAD),
                new TextPage("erosianmagic.codex.page.toll_the_dead.1"));
        SUMMON_VEX_CHAPTER = new Chapter("erosianmagic.codex.chapter.summon_vex",
                new ChantPage("erosianmagic.codex.page.summon_vex.0", ChantRegistry.SUMMON_VEX),
                new TextPage("erosianmagic.codex.page.summon_vex.1"));
        ABYSSAL_SHROUD_CHAPTER = new Chapter("erosianmagic.codex.chapter.abyssal_shroud",
                new ChantPage("erosianmagic.codex.page.abyssal_shroud.0", ChantRegistry.ABYSSAL_SHROUD),
                new TextPage("erosianmagic.codex.page.abyssal_shroud.1"));
        SCULK_TENTACLES_CHAPTER = new Chapter("erosianmagic.codex.chapter.sculk_tentacles",
                new ChantPage("erosianmagic.codex.page.sculk_tentacles.0", ChantRegistry.SCULK_TENTACLES),
                new TextPage("erosianmagic.codex.page.sculk_tentacles.1"));
        SONIC_BOOM_CHAPTER = new Chapter("erosianmagic.codex.chapter.sonic_boom",
                new ChantPage("erosianmagic.codex.page.sonic_boom.0", ChantRegistry.SONIC_BOOM),
                new TextPage("erosianmagic.codex.page.sonic_boom.1"));
        PLANAR_SIGHT_CHAPTER = new Chapter("erosianmagic.codex.chapter.planar_sight",
                new ChantPage("erosianmagic.codex.page.planar_sight.0", ChantRegistry.PLANAR_SIGHT),
                new TextPage("erosianmagic.codex.page.planar_sight.1"));
        TELEKINESIS_CHAPTER = new Chapter("erosianmagic.codex.chapter.telekinesis",
                new ChantPage("erosianmagic.codex.page.telekinesis.0", ChantRegistry.TELEKINESIS),
                new TextPage("erosianmagic.codex.page.telekinesis.1"));
        ELDRITCH_BLAST_CHAPTER = new Chapter("erosianmagic.codex.chapter.eldritch_blast",
                new ChantPage("erosianmagic.codex.page.eldritch_blast.0", ChantRegistry.ELDRITCH_BLAST),
                new TextPage("erosianmagic.codex.page.eldritch_blast.1"));
        BESTOW_CURSE_CHAPTER = new Chapter("erosianmagic.codex.chapter.bestow_curse",
                new ChantPage("erosianmagic.codex.page.bestow_curse.0", ChantRegistry.BESTOW_CURSE),
                new TextPage("erosianmagic.codex.page.bestow_curse.1"));
        CONE_OF_COLD_CHAPTER = new Chapter("erosianmagic.codex.chapter.cone_of_cold",
                new ChantPage("erosianmagic.codex.page.cone_of_cold.0", ChantRegistry.CONE_OF_COLD),
                new TextPage("erosianmagic.codex.page.cone_of_cold.1"));
        ELECTROCUTE_CHAPTER = new Chapter("erosianmagic.codex.chapter.electrocute",
                new ChantPage("erosianmagic.codex.page.electrocute.0", ChantRegistry.ELECTROCUTE),
                new TextPage("erosianmagic.codex.page.electrocute.1"));
        SHOCKWAVE_CHAPTER = new Chapter("erosianmagic.codex.chapter.shockwave",
                new ChantPage("erosianmagic.codex.page.shockwave.0", ChantRegistry.SHOCKWAVE),
                new TextPage("erosianmagic.codex.page.shockwave.1"));
        BLESS_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.bless",
                new ChantPage("erosianmagic.codex.page.bless.0", PrayerRegistry.BLESS),
                new TextPage("erosianmagic.codex.page.bless.1"));
        PRAYER_OF_HEALING_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.prayer_of_healing",
                new ChantPage("erosianmagic.codex.page.prayer_of_healing.0", PrayerRegistry.PRAYER_OF_HEALING),
                new TextPage("erosianmagic.codex.page.prayer_of_healing.1"));
        WORD_OF_RECALL_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.word_of_recall",
                new ChantPage("erosianmagic.codex.page.word_of_recall.0", PrayerRegistry.WORD_OF_RECALL),
                new TextPage("erosianmagic.codex.page.word_of_recall.1"));
        ACID_ORB_CHAPTER = new Chapter("erosianmagic.codex.chapter.acid_orb",
                new ChantPage("erosianmagic.codex.page.acid_orb.0", ChantRegistry.ACID_ORB),
                new TextPage("erosianmagic.codex.page.acid_orb.1"));
        BLIGHT_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.blight",
                new ChantPage("erosianmagic.codex.page.blight.0", PrayerRegistry.BLIGHT),
                new TextPage("erosianmagic.codex.page.blight.1"));
        SPIDER_ASPECT_CHAPTER = new Chapter("erosianmagic.codex.chapter.spider_aspect",
                new ChantPage("erosianmagic.codex.page.spider_aspect.0", ChantRegistry.SPIDER_ASPECT),
                new TextPage("erosianmagic.codex.page.spider_aspect.1"));
        FIREFLY_SWARM_CHAPTER = new Chapter("erosianmagic.codex.chapter.firefly_swarm",
                new ChantPage("erosianmagic.codex.page.firefly_swarm.0", ChantRegistry.FIREFLY_SWARM),
                new TextPage("erosianmagic.codex.page.firefly_swarm.1"));
        STOMP_CHAPTER = new Chapter("erosianmagic.codex.chapter.stomp",
                new ChantPage("erosianmagic.codex.page.stomp.0", ChantRegistry.STOMP),
                new TextPage("erosianmagic.codex.page.stomp.1"));
        EARTHQUAKE_CHAPTER_WITCH = new Chapter("erosianmagic.codex.chapter.earthquake",
                new ChantPage("erosianmagic.codex.page.earthquake.0", PrayerRegistry.EARTHQUAKE),
                new TextPage("erosianmagic.codex.page.earthquake.1"));
        GLUTTONY_CHAPTER = new Chapter("erosianmagic.codex.chapter.gluttony",
                new ChantPage("erosianmagic.codex.page.gluttony.0", ChantRegistry.GLUTTONY),
                new TextPage("erosianmagic.codex.page.gluttony.1"));
                
    }
    public static void init() {
        initClericChapters();
        initWitchChapters();
        MAGIC_MISSILE_CHAPTER = new Chapter("erosianmagic.codex.chapter.magic_missile",
                new ChantPage("erosianmagic.codex.page.magic_missile.0", ChantRegistry.MAGIC_MISSILE),
                new TextPage("erosianmagic.codex.page.magic_missile.1"));

        MYSTIC_ARTS_INDEX = new PredicatedIndex("erosianmagic.codex.chapter.mystic_arts", new Page[]{new SortedIndexPage(
                new SignClassSpellLockedEntry(MENDING_CHAPTER, "Cleric", MENDING.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.SACRED_SIGN),
                new SignClassSpellLockedEntry(TOLL_THE_DEAD_CHAPTER, "Cleric", TOLL_THE_DEAD.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.DEATH_SIGN, Signs.SACRED_SIGN),
                new SignClassSpellLockedEntry(INFILCT_WOUNDS_CHAPTER, "Cleric", INFLICT_WOUNDS.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.BLOOD_SIGN, Signs.DEATH_SIGN),
                new SignClassSpellLockedEntry(CURE_WOUNDS_CHAPTER, "Cleric", CURE_WOUNDS.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.HARMONY_SIGN),
                new SignClassSpellLockedEntry(HEALING_WORD_CHAPTER, "Cleric", HEALING_WORD.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.HARMONY_SIGN, Signs.HARMONY_SIGN),
                new SignClassSpellLockedEntry(BLESS_CHAPTER, "Cleric", BLESS.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.HARMONY_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(SHIELD_OF_FAITH_CHAPTER, "Cleric", SHIELD_OF_FAITH.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.WARDING_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(PRAYER_OF_HEALING_CHAPTER, "Cleric", PRAYER_OF_HEALING.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.HARMONY_SIGN, Signs.SACRED_SIGN, Signs.HARMONY_SIGN),
                new SignClassSpellLockedEntry(WARDING_BOND_CHAPTER, "Cleric", WARDING_BOND.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SOUL_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(MASS_HEALING_WORD_CHAPTER, "Cleric", MASS_HEALING_WORD.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.HARMONY_SIGN, Signs.SACRED_SIGN, Signs.HARMONY_SIGN, Signs.SACRED_SIGN),
                new SignClassSpellLockedEntry(WORD_OF_RECALL_CHAPTER, "Cleric", WORD_OF_RECALL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.SOUL_SIGN),
                new SignClassSpellLockedEntry(FIRE_STORM_CHAPTER, "Cleric", BLAZE_STORM_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(FIREBALL_CHAPTER, "Cleric", FIREBALL_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(EARTHQUAKE_CHAPTER, "Cleric", EARTHQUAKE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.WINTER_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(BLIGHT_CHAPTER, "Cleric", BLIGHT_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WICKED_SIGN, Signs.DEATH_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(WALL_OF_FIRE_CHAPTER, "Cleric", WALL_OF_FIRE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.WINTER_SIGN),
                new SignClassSpellLockedEntry(BEACON_OF_HOPE_CHAPTER, "Cleric", BEACON_OF_HOPE.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.MIND_SIGN)
        )}, new Page[]{new SortedIndexPage(
                new SignClassSpellLockedEntry(MAGIC_MISSILE_CHAPTER, "Witch", MAGIC_MISSILE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(COUNTERSPELL_CHAPTER, "Witch", COUNTERSPELL_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.MIND_SIGN),
                new SignClassSpellLockedEntry(DRAGON_BREATH_CHAPTER, "Witch", DRAGON_BREATH_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(STARFALL_CHAPTER, "Witch", STARFALL_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WINTER_SIGN, Signs.SACRED_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(SUMMON_ENDER_CHEST_CHAPTER, "Witch", SUMMON_ENDER_CHEST_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.WARDING_SIGN, Signs.SOUL_SIGN),
                new SignClassSpellLockedEntry(RECALL_CHAPTER, "Witch", RECALL_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.SOUL_SIGN),
                new SignClassSpellLockedEntry(PORTAL_CHAPTER, "Witch", PORTAL_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(MAGIC_WEAPON_CHAPTER, "Witch", MAGIC_WEAPON.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(WARDING_WIND_CHAPTER, "Witch", WARDING_WIND.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.WARDING_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(FIRE_BREATH_CHAPTER, "Witch", FIRE_BREATH_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(MAGMA_BOMB_CHAPTER, "Witch", MAGMA_BOMB_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(HEAT_SURGE_CHAPTER, "Witch", HEAT_SURGE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(HEAT_METAL_CHAPTER, "Witch", HEAT_METAL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(FAERIE_FIRE_CHAPTER, "Witch", FAERIE_FIRE.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(ACUPUNCTURE_CHAPTER, "Witch", ACUPUNCTURE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.MIND_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(BLOOD_NEEDLES_CHAPTER, "Witch", BLOOD_NEEDLES_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(BLOOD_SLASH_CHAPTER, "Witch", BLOOD_SLASH_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.DEATH_SIGN),
                new SignClassSpellLockedEntry(DEVOUR_CHAPTER, "Witch", DEVOUR_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.BLOOD_SIGN, Signs.SOUL_SIGN, Signs.WARDING_SIGN, Signs.DEATH_SIGN),
                new SignClassSpellLockedEntry(RAY_OF_SIPHONING_CHAPTER, "Witch", RAY_OF_SIPHONING_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.DEATH_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(TOLL_THE_DEAD_CHAPTER_WITCH, "Witch", TOLL_THE_DEAD.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.DEATH_SIGN, Signs.DEATH_SIGN, Signs.SACRED_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(SUMMON_VEX_CHAPTER, "Witch", SUMMON_VEX_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.SOUL_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(ABYSSAL_SHROUD_CHAPTER, "Witch", ABYSSAL_SHROUD_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.DEATH_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(SCULK_TENTACLES_CHAPTER, "Witch", SCULK_TENTACLES_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.DEATH_SIGN),
                new SignClassSpellLockedEntry(SONIC_BOOM_CHAPTER, "Witch", SONIC_BOOM_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(PLANAR_SIGHT_CHAPTER, "Witch", PLANAR_SIGHT_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WINTER_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(TELEKINESIS_CHAPTER, "Witch", TELEKINESIS_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(ELDRITCH_BLAST_CHAPTER, "Witch", ELDRITCH_BLAST.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(BESTOW_CURSE_CHAPTER, "Witch", BESTOW_CURSE.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.DEATH_SIGN, Signs.FLAME_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(CONE_OF_COLD_CHAPTER, "Witch", CONE_OF_COLD_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WINTER_SIGN, Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(ELECTROCUTE_CHAPTER, "Witch", ELECTROCUTE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(SHOCKWAVE_CHAPTER, "Witch", SHOCKWAVE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(BLESS_CHAPTER_WITCH, "Witch", BLESS.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.MAGIC_SIGN),
                new SignClassSpellLockedEntry(PRAYER_OF_HEALING_CHAPTER_WITCH, "Witch", PRAYER_OF_HEALING.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.SOUL_SIGN),
                new SignClassSpellLockedEntry(WORD_OF_RECALL_CHAPTER_WITCH, "Witch", WORD_OF_RECALL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.SOUL_SIGN, Signs.FLAME_SIGN, Signs.SACRED_SIGN),
                new SignClassSpellLockedEntry(ACID_ORB_CHAPTER, "Witch", ACID_ORB_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.MAGIC_SIGN, Signs.SACRED_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN)
        ), new SortedIndexPage(
                new SignClassSpellLockedEntry(BLIGHT_CHAPTER_WITCH, "Witch", BLIGHT_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WICKED_SIGN, Signs.DEATH_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(SPIDER_ASPECT_CHAPTER, "Witch", SPIDER_ASPECT_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.SACRED_SIGN, Signs.WARDING_SIGN, Signs.SOUL_SIGN, Signs.WINTER_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(FIREFLY_SWARM_CHAPTER, "Witch", FIREFLY_SWARM_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.FLAME_SIGN, Signs.FLAME_SIGN, Signs.SACRED_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN),
                new SignClassSpellLockedEntry(STOMP_CHAPTER, "Witch", STOMP_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.WARDING_SIGN, Signs.MAGIC_SIGN, Signs.FLAME_SIGN, Signs.WINTER_SIGN, Signs.SACRED_SIGN),
                new SignClassSpellLockedEntry(EARTHQUAKE_CHAPTER_WITCH, "Witch", EARTHQUAKE_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.MAGIC_SIGN, Signs.WARDING_SIGN, Signs.WINTER_SIGN, Signs.FLAME_SIGN, Signs.WARDING_SIGN),
                new SignClassSpellLockedEntry(GLUTTONY_CHAPTER, "Witch", GLUTTONY_SPELL.get(), new ItemStack(Items.AMETHYST_SHARD), Signs.BLOOD_SIGN, Signs.BLOOD_SIGN, Signs.SOUL_SIGN, Signs.DEATH_SIGN, Signs.FLAME_SIGN)
        )});
        CodexChapters.categories.add(MYSTIC_ARTS = new Category(
                "mystic_arts",
                new ItemStack(Registry.ANGELS_SIGHT.get()),
                ColorUtil.packColor(255, 134, 67, 255),
                MYSTIC_ARTS_INDEX
        ));
    }
}
