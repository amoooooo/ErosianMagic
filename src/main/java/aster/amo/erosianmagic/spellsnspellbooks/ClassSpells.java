package aster.amo.erosianmagic.spellsnspellbooks;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ClassSpells {
    public static Map<String, List<Supplier<AbstractSpell>>> CLASS_SPELLS = new HashMap<>();
    public static void init() {
        initBardSpells();
        initClericSpells();
    }

    private static void initBardSpells() {
        CLASS_SPELLS.put("Bard", List.of(
                SpellRegistry.MENDING,
                SpellRegistry.VICIOUS_MOCKERY,
                SpellRegistry.HEALING_WORD,
                SpellRegistry.FAERIE_FIRE,
                SpellRegistry.HEAT_METAL,
                SpellRegistry.WARDING_WIND,
                SpellRegistry.MANTLE_OF_INSPIRATION,
                SpellRegistry.MISLEAD,
                SpellRegistry.IRRESISTIBLE_DANCE,
                SpellRegistry.MASS_HEALING_WORD,
                SpellRegistry.MAGIC_WEAPON,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.RAISE_DEAD_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.MAGIC_ARROW_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.FIRECRACKER_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.INVISIBILITY_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.GUST_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.SHIELD_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.MAGMA_BOMB_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.FORTIFY_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.RAY_OF_FROST_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.CONE_OF_COLD_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.LIGHTNING_LANCE_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.ELECTROCUTE_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.ROOT_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.OAKSKIN_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.FIREFLY_SWARM_SPELL
        ));
    }

    /**
     *  ✅ = implemented
     * Cleric Spells:
     * Mending✅
     * Sacred Flame✅
     * Toll the Dead✅
     * Guiding Bolt✅📷
     * Inflict Wounds✅
     * Cure Wounds✅
     * Healing Word✅
     * Bless✅
     * Shield of Faith✅
     * Prayer of Healing✅
     * Warding Bond✅
     * Mass Healing Word✅
     * Daylight
     * Revivify (touch corpses revives player)
     * Spirit Guardians
     * Death Ward
     * Sunbeam
     * Word of Recall ✅
     * Find the Path
     * Fire Storm ✅📷
     * Fireball ✅📷
     * Earthquake✅📷
     * Blight ✅📷
     * Wall of fire ✅📷
     * Beacon of hope ✅
     */
    private static void initClericSpells() {
        CLASS_SPELLS.put("Cleric", List.of(
                SpellRegistry.MENDING,
                SpellRegistry.SACRED_FLAME,
                SpellRegistry.TOLL_THE_DEAD,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.GUIDING_BOLT_SPELL,
                SpellRegistry.INFLICT_WOUNDS,
                SpellRegistry.CURE_WOUNDS,
                SpellRegistry.HEALING_WORD,
                SpellRegistry.BLESS,
                SpellRegistry.SHIELD_OF_FAITH,
                SpellRegistry.PRAYER_OF_HEALING,
                SpellRegistry.WARDING_BOND,
                SpellRegistry.MASS_HEALING_WORD,
                SpellRegistry.WORD_OF_RECALL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.BLAZE_STORM_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.FIREBALL_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.EARTHQUAKE_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.BLIGHT_SPELL,
                io.redspace.ironsspellbooks.api.registry.SpellRegistry.WALL_OF_FIRE_SPELL,
                SpellRegistry.BEACON_OF_HOPE
        ));
    }
}
