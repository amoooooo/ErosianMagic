package aster.amo.erosianmagic.registry;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.witch.mobeffect.HexbloodEffect;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, ErosianMagic.MODID);

    public static final RegistryObject<MobEffect> HEXBLOOD = MOB_EFFECT_DEFERRED_REGISTER.register("hexblood", () -> new HexbloodEffect(MobEffectCategory.BENEFICIAL, 3311322).addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), "87733c95-909c-4fc3-9780-e35a89565666", 0.1f, AttributeModifier.Operation.MULTIPLY_TOTAL));

}
