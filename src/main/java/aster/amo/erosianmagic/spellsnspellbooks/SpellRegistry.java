package aster.amo.erosianmagic.spellsnspellbooks;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.spellsnspellbooks.spells.*;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class SpellRegistry {
    public static void init(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, ErosianMagic.MODID);
    public static final RegistryObject<AbstractSpell> HEXBLOOD_SPELL = registerSpell(new HexbloodSpell());
    public static final RegistryObject<AbstractSpell> PSYCHICSCREAM_SPELL = registerSpell(new PsychicScreamSpell());
    public static final RegistryObject<AbstractSpell> BREATHE_EASY = registerSpell(new BreatheEasySpell());
    public static final RegistryObject<AbstractSpell> MANTLE_OF_INSPIRATION = registerSpell(new MantleOfInspirationSpell());
    public static final RegistryObject<AbstractSpell> HEAT_METAL = registerSpell(new HeatMetalSpell());
    public static final RegistryObject<AbstractSpell> FAERIE_FIRE = registerSpell(new FaerieFireSpell());
    public static final RegistryObject<AbstractSpell> MENDING = registerSpell(new MendingSpell());
    public static final RegistryObject<AbstractSpell> IRRESISTIBLE_DANCE = registerSpell(new IrresistibleDanceSpell());
    public static final RegistryObject<AbstractSpell> WARDING_WIND = registerSpell(new WardingWindSpell());
    public static final RegistryObject<AbstractSpell> MISLEAD = registerSpell(new MisleadSpell());
    public static final RegistryObject<AbstractSpell> BESTOW_CURSE = registerSpell(new BestowCurseSpell());
    public static final RegistryObject<AbstractSpell> BEACON_OF_HOPE = registerSpell(new BeaconOfHopeSpell());
    public static final RegistryObject<AbstractSpell> SACRED_FLAME = registerSpell(new SacredFlameSpell());
    public static final RegistryObject<AbstractSpell> HEALING_WORD = registerSpell(new HealingWordSpell());
    public static final RegistryObject<AbstractSpell> PRAYER_OF_HEALING = registerSpell(new PrayerOfHealingSpell());
    public static final RegistryObject<AbstractSpell> TOLL_THE_DEAD = registerSpell(new TollTheDeadSpell());
    public static final RegistryObject<AbstractSpell> WARDING_BOND = registerSpell(new WardingBondSpell());
    public static final RegistryObject<AbstractSpell> BLESS = registerSpell(new BlessSpell());
    public static final RegistryObject<AbstractSpell> SHIELD_OF_FAITH = registerSpell(new ShieldOfFaithSpell());
    public static final RegistryObject<AbstractSpell> MASS_HEALING_WORD = registerSpell(new MassHealingWordSpell());
    public static final RegistryObject<AbstractSpell> INFLICT_WOUNDS = registerSpell(new InflictWoundsSpell());
    public static final RegistryObject<AbstractSpell> CURE_WOUNDS = registerSpell(new CureWoundsSpell());
    public static final RegistryObject<AbstractSpell> WORD_OF_RECALL = registerSpell(new WordOfRecallSpell());
}
