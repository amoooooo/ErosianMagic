package aster.amo.erosianmagic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ErosianMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue AUTO_THIRD_PERSON = BUILDER
            .comment("If true, automatically switches to third person when casting a spell")
            .define("autoThirdPerson", false);
    private static final ForgeConfigSpec.BooleanValue DEBUG = BUILDER
            .comment("If true, enables debug mode")
            .define("debug", false);
    private static final ForgeConfigSpec.BooleanValue FORCE_RENDER_OWN_NAMETAG = BUILDER
            .comment("If true, forces rendering of your own nametag")
            .define("forceRenderOwnNametag", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean autoThirdPerson;
    public static boolean debug;
    public static boolean forceRenderOwnNametag;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        autoThirdPerson = AUTO_THIRD_PERSON.get();
        debug = DEBUG.get();
        forceRenderOwnNametag = FORCE_RENDER_OWN_NAMETAG.get();
    }
}
