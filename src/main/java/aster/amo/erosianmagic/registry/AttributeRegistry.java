package aster.amo.erosianmagic.registry;

import aster.amo.erosianmagic.ErosianMagic;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ErosianMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class AttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ErosianMagic.MODID);
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> SONG_POWER = ATTRIBUTES.register("song_power", () -> new RangedAttribute("attribute.erosianmagic.song_power", 0.0, 0.0, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> CHANT_POWER = ATTRIBUTES.register("chant_power", () -> new RangedAttribute("attribute.erosianmagic.chant_power", 0.0, 0.0, 10.0D).setSyncable(true));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> {
            event.add(entity, SONG_POWER.get());
            event.add(entity, CHANT_POWER.get());
        });
    }

}
