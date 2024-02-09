package aster.amo.erosianmagic.registry;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.spellsnspellbooks.spells.entity.FaerieFireAoe;
import aster.amo.erosianmagic.spellsnspellbooks.spells.entity.PsychicLanceProjectile;
import aster.amo.erosianmagic.util.AoeEffectEntity;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ErosianMagic.MODID);
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<AoeEffectEntity>> AOE_EFFECT = ENTITIES.register("aoe_effect", () -> EntityType.Builder.<AoeEffectEntity>of(AoeEffectEntity::new, MobCategory.MISC)
            .sized(4f, .8f)
            .clientTrackingRange(64)
            .build(new ResourceLocation(ErosianMagic.MODID, "aoe_effect").toString()));

    public static final RegistryObject<EntityType<FaerieFireAoe>> FAERIE_FIRE_AOE = ENTITIES.register("faerie_fire_aoe", () -> EntityType.Builder.<FaerieFireAoe>of(FaerieFireAoe::new, MobCategory.MISC)
            .sized(4f, .8f)
            .clientTrackingRange(64)
            .build(new ResourceLocation(ErosianMagic.MODID, "faerie_fire_aoe").toString()));

    public static final RegistryObject<EntityType<PsychicLanceProjectile>> PSYCHIC_LANCE = ENTITIES.register("psychic_lance", () -> EntityType.Builder.<PsychicLanceProjectile>of(PsychicLanceProjectile::new, MobCategory.MISC)
            .sized(4f, .8f)
            .clientTrackingRange(64)
            .build(new ResourceLocation(ErosianMagic.MODID, "psychic_lance").toString()));
}
