package aster.amo.erosianmagic.registry;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.mobeffect.BardicInspirationEffect;
import aster.amo.erosianmagic.mobeffect.HexbloodEffect;
import aster.amo.erosianmagic.mobeffect.MantleOfInspirationEffect;
import elucent.eidolon.registries.EidolonAttributes;
import elucent.eidolon.registries.Registry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, ErosianMagic.MODID);

    public static final RegistryObject<MobEffect> HEXBLOOD = MOB_EFFECT_DEFERRED_REGISTER.register("hexblood", () -> new HexbloodEffect(MobEffectCategory.BENEFICIAL, 3311322).addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.1f, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> BARDIC_INSPIRATION = MOB_EFFECT_DEFERRED_REGISTER.register("bardic_inspiration", () -> new BardicInspirationEffect(MobEffectCategory.BENEFICIAL, 3311322)
            .addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.15f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(AttributeRegistry.COOLDOWN_REDUCTION.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, "87733c95-909c-4fc3-9780-e35a89565666", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "87733c95-909c-4fc3-9780-e35a89565666", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "87733c95-909c-4fc3-9780-e35a89565666", 0.05f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EidolonAttributes.CHANTING_SPEED.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(aster.amo.erosianmagic.registry.AttributeRegistry.CHANT_POWER.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(aster.amo.erosianmagic.registry.AttributeRegistry.SONG_POWER.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.10f, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(AttributeRegistry.MANA_REGEN.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.50f, AttributeModifier.Operation.MULTIPLY_BASE)
    );

    public static final RegistryObject<MobEffect> MANTLE_OF_INSPIRATION = MOB_EFFECT_DEFERRED_REGISTER.register("mantle_of_inspiration", () -> new MantleOfInspirationEffect(MobEffectCategory.BENEFICIAL, 3311322));
}
