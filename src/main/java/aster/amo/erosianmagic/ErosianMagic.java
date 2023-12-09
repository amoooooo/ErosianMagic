package aster.amo.erosianmagic;

import aster.amo.erosianmagic.net.CombatTimerPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.particle.ParticleRegistry;
import aster.amo.erosianmagic.registry.EntityRegistry;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.witch.eidolon.ChantRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

@Mod(ErosianMagic.MODID)
public class ErosianMagic {

    public static final String MODID = "erosianmagic";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static long lastDamagedTime = 0;
    public ErosianMagic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ChantRegistry.init();
        SpellRegistry.init(modEventBus);
        MobEffectRegistry.MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        Networking.init();
        EntityRegistry.register(modEventBus);
    }

    public static ResourceLocation getId(String name) {
        return new ResourceLocation(MODID, name);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    static class ModEvents {

        @SubscribeEvent
        public static void onHurt(LivingHurtEvent event) {
            // if target is player or source is player
            if (event.getEntity() instanceof Player || event.getSource().getDirectEntity() instanceof Player) {
                Player player = null;
                if (event.getEntity() instanceof Player) {
                    player = (Player) event.getEntity();
                } else if (event.getSource().getDirectEntity() instanceof Player) {
                    player = (Player) event.getSource().getDirectEntity();
                }
                if (player != null) {
                    lastDamagedTime = player.level().getGameTime();
                    Networking.sendTo(player, new CombatTimerPacket(lastDamagedTime));
                }
            }
        }

        @SubscribeEvent
        public static void onCommandRegister(RegisterCommandsEvent event) {
            LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.<CommandSourceStack>literal("erosianmagic");
            builder.requires((commandSource) -> commandSource.hasPermission(3))
                    .then(Commands.literal("grant")
                            .then(Commands.argument("sign", StringArgumentType.string())
                                    .suggests((commandContext, suggestionsBuilder) -> {
                                        return SharedSuggestionProvider.suggest(Signs.getSigns().stream().map(s -> s.getRegistryName().toString()).collect(Collectors.toList()), suggestionsBuilder);
                                    })
                                    .executes((commandSource) -> {
                                        Player player = commandSource.getSource().getPlayerOrException();
                                        KnowledgeUtil.grantSign(player, Signs.find(new ResourceLocation(StringArgumentType.getString(commandSource, "sign"))));
                                        return 1;
                                    })));
            event.getDispatcher().register(builder);
        }
    }
}
