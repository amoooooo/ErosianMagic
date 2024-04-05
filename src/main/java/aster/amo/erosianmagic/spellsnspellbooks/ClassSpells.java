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
        initWitchSpells();
        initMachinistSpells();
    }

    private static void initBardSpells() {
        CLASS_SPELLS.put("Bard", List.of(
                SpellRegistry.VICIOUS_MOCKERY
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
                SpellRegistry.SACRED_FLAME
        ));
    }

    private static void initWitchSpells() {
        CLASS_SPELLS.put("Witch", List.of(
                SpellRegistry.ELDRITCH_BLAST
        ));
    }

    private static void initMachinistSpells() {
        CLASS_SPELLS.put("Machinist", List.of(
                SpellRegistry.FAERIE_FIRE
        ));
    }
}
