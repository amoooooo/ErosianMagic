package aster.amo.erosianmagic;

import aster.amo.erosianmagic.bard.IBard;
import aster.amo.erosianmagic.cleric.ICleric;
import aster.amo.erosianmagic.cleric.PrayerRegistry;
import aster.amo.erosianmagic.cleric.chapel.IWorshipper;
import aster.amo.erosianmagic.net.ClientboundClassPacket;
import aster.amo.erosianmagic.net.CombatTimerPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.particle.ParticleRegistry;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import aster.amo.erosianmagic.registry.EntityRegistry;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.witch.eidolon.ChantRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod(ErosianMagic.MODID)
public class ErosianMagic {

    public static final String MODID = "erosianmagic";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static long lastDamagedTime = 0;
    public static LootCategory INSTRUMENT = LootCategory.register(LootCategory.SWORD, "instrument", s -> s.getItem() instanceof InstrumentItem, arr(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

    public ErosianMagic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ChantRegistry.init();
        PrayerRegistry.init();
        SpellRegistry.init(modEventBus);
        MobEffectRegistry.MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        Networking.init();
        EntityRegistry.register(modEventBus);
        AttributeRegistry.register(modEventBus);
    }

    private static EquipmentSlot[] arr(EquipmentSlot... s) {
        return s;
    }

    public static ResourceLocation getId(String name) {
        return new ResourceLocation(MODID, name);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    static class ModEvents {


    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    static class ForgeEvents {
        @SubscribeEvent
        public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(MODID, "bard"), new IBard.Provider());
                event.addCapability(new ResourceLocation(MODID, "cleric"), new ICleric.Provider());
            }
            if(event.getObject() instanceof Villager) {
                event.addCapability(new ResourceLocation(MODID, "worshipper"), new IWorshipper.Provider());
            }
        }

        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            event.getOriginal().getCapability(ICleric.INSTANCE).ifPresent(original -> {
                event.getEntity().getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                    ((INBTSerializable<CompoundTag>) cleric).deserializeNBT(((INBTSerializable<CompoundTag>) original).serializeNBT());
                    cleric.sync(event.getEntity());
                });
            });
            event.getOriginal().getCapability(ICleric.INSTANCE).invalidate();
            event.getOriginal().getCapability(IBard.INSTANCE).ifPresent(original -> {
                event.getEntity().getCapability(IBard.INSTANCE).ifPresent(bard -> {
                    ((INBTSerializable<CompoundTag>) bard).deserializeNBT(((INBTSerializable<CompoundTag>) original).serializeNBT());
                    bard.sync(event.getEntity());
                });
            });
            event.getOriginal().getCapability(IBard.INSTANCE).invalidate();
        }

        @SubscribeEvent
        public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric ->{
                if(cleric.isChosenClass() && cleric.hasTemple()) {
                    player.sendSystemMessage(Component.literal("You have " + cleric.getWorshippers().size() + " worshippers."));
                }
                if(cleric.isChosenClass()) {
                    Networking.sendTo(player, new ClientboundClassPacket(cleric.getClassName()));
                }
            });
            player.getCapability(IBard.INSTANCE).ifPresent(bard -> {
                if(bard.isChosenClass()) {
                    Networking.sendTo(player, new ClientboundClassPacket(bard.getClassName()));
                }
            });
        }
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
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (!event.player.level().isClientSide) {
                Player player = event.player;
                player.getCapability(IBard.INSTANCE).ifPresent((bard) -> {
                    if (bard.isChosenClass()) {
                        bard.tick(player);
                        if(MagicData.getPlayerMagicData(player).isCasting()) {
                            bard.setInspirationTime(120);
                        }
                    }
                });
                player.getCapability(ICleric.INSTANCE).ifPresent((cleric) -> {
                    if (cleric.isChosenClass()) {
                        if(cleric.hasTemple())
                            cleric.getTemple().tick(player.level(), player);
                    }
                });
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void randomizeHurt(LivingHurtEvent event) {
            // roll a random int between 1 and 20
            // if the int is 1, damage is halved
            // if the int is 20, damage is doubled
            // otherwise, damage is multiplied by the int/20
            if(event.getAmount() < 2.0f) return;
            if (event.getSource().getDirectEntity() instanceof Player player) {
                damageRoll(event, player);
            } else if (event.getSource().getDirectEntity() instanceof LivingEntity) {
                LivingEntity le = event.getEntity();
                damageRoll(event, le);
            } else if (event.getEntity() instanceof Player pl) {
                damageRoll(event, event.getAmount() * 2, pl.getDisplayName(), event.getAmount() / 2, pl);
            } else {
                float roll = rollWithLuck(event.getEntity(), event.getEntity().getAttributeValue(Attributes.LUCK));
                if (roll == 1) {
                    event.setAmount(event.getAmount() * 2);
                } else if (roll == 20) {
                    event.setAmount(event.getAmount() / 2);
                } else {
                    event.setAmount(event.getAmount() * ((float) roll / 20));
                }
            }
        }

        private static void damageRoll(LivingHurtEvent event, float v, Component displayName, float v2, LivingEntity pl) {
            float roll = rollWithLuck(pl, pl.getAttributeValue(Attributes.LUCK));
            if (roll == 1) {
                event.setAmount(v);
                List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                players.forEach(p -> {
                    p.sendSystemMessage(displayName.copy().append(Component.literal(" rolled a natural 1!")).withStyle(ChatFormatting.RED));
                });
            } else if (roll == 20) {
                event.setAmount(v2);
                List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                players.forEach(p -> {
                    p.sendSystemMessage(displayName.copy().append(Component.literal(" rolled a natural 20!")).withStyle(ChatFormatting.GREEN));
                });
            } else {
                event.setAmount(event.getAmount() * ((float) roll / 20));
            }
        }

        private static void damageRoll(LivingHurtEvent event, LivingEntity le) {
            damageRoll(event, event.getAmount() / 2, le.getDisplayName(), event.getAmount() * 2, le);
        }

        @SubscribeEvent
        public static void entityTick(LivingEvent.LivingTickEvent event) {
            if(event.getEntity().level().isClientSide) return;
            if(event.getEntity() instanceof Villager ve) {
                ve.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                    UUID leader = worshipper.getLeader();
                    if(leader != null) {
                        long timeSinceLastSermon = event.getEntity().level().getGameTime() - worshipper.getLastSermon();
                        worshipper.setLoseFaithChance((float) (timeSinceLastSermon / 600000));
                        if(event.getEntity().getRandom().nextFloat() < worshipper.getLoseFaithChance()) {
                            Player player = event.getEntity().level().getPlayerByUUID(leader);
                            if(player != null) {
                                player.sendSystemMessage(Component.literal("One of your worshippers has lost faith.").withStyle(ChatFormatting.RED));
                            }
                            worshipper.setLeader(null);
                            worshipper.setLoseFaithChance(0.0f);
                            worshipper.setLastSermon(0);
                            worshipper.setSeatPosition(null);
                            worshipper.setBeforePosition(null);
                        }
                    }
                });
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void randomizeHeals(LivingHealEvent event) {
            // roll a random int between 1 and 20
            // if the int is 1, heal is halved
            // if the int is 20, heal is doubled
            // otherwise, heal is multiplied by the int/20
            if(event.getAmount() < 2) return;
            if (event.getEntity() instanceof Player player) {
                float roll = rollWithLuck(player, player.getAttributeValue(Attributes.LUCK));
                if(event.getEntity().hasEffect(MobEffectRegistry.HOPEFUL.get())){
                    roll = 19;
                    player.sendSystemMessage(Component.literal("The Beacon of Hope shines upon you!").withStyle(ChatFormatting.GREEN));
                }
                if (roll == 1) {
                    event.setAmount(event.getAmount() / 2);
                    List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                    players.forEach(p -> {
                        p.sendSystemMessage(player.getDisplayName().copy().append(Component.literal(" rolled a natural 1!")).withStyle(ChatFormatting.RED));
                    });
                } else if (roll == 20) {
                    event.setAmount(event.getAmount() * 2);
                    List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                    players.forEach(p -> {
                        p.sendSystemMessage(player.getDisplayName().copy().append(Component.literal(" rolled a natural 20!")).withStyle(ChatFormatting.GREEN));
                    });
                } else {
                    event.setAmount(event.getAmount() * ((float) roll / 20));
                }
            } else {
                float roll = rollWithLuck(event.getEntity(), event.getEntity().getAttributeValue(Attributes.LUCK));
                if (roll == 1) {
                    event.setAmount(event.getAmount() / 2);
                } else if (roll == 20) {
                    event.setAmount(event.getAmount() * 2);
                } else {
                    event.setAmount(event.getAmount() * ((float) roll / 20));
                }
            }
        }

        public static float rollWithLuck(LivingEntity entity, double luck){
            float baseChance = 1.0f / 20.0f;
            double adjustedLuck = luck / 1024.0D;
            float randomNumber = entity.getRandom().nextFloat();
            float cumulativeProbability = 0.0f;
            for (int i = 1; i <= 20; i++) {
                cumulativeProbability += baseChance + (adjustedLuck * i / 20.0f);
                if (randomNumber <= cumulativeProbability) {
                    return i;
                }
            }
            return 10;
        }

        @SubscribeEvent
        public static void onCommandRegister(RegisterCommandsEvent event) {
            LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.literal("erosianmagic");
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
                                    })))
                    .then(Commands.literal("set")
                            .then(Commands.literal("bard")
                                    .then(Commands.argument("player", EntityArgument.player())
                                            .executes((commandSource) -> {
                                                Player player = EntityArgument.getPlayer(commandSource, "player");
                                                player.getCapability(IBard.INSTANCE).ifPresent((bard) -> {
                                                    bard.setChosenClass(true);
                                                    bard.sync(player);
                                                    Networking.sendTo(player, new ClientboundClassPacket(bard.getClassName()));
                                                });
                                                player.getCapability(ICleric.INSTANCE).ifPresent((cleric) -> {
                                                    cleric.setChosenClass(false);
                                                    cleric.sync(player);
                                                });
                                                return 1;
                                            })
                                    )
                            )
                            .then(Commands.literal("cleric")
                                    .then(Commands.argument("player", EntityArgument.player())
                                            .executes((commandSource) -> {
                                                Player player = EntityArgument.getPlayer(commandSource, "player");
                                                player.getCapability(IBard.INSTANCE).ifPresent((bard) -> {
                                                    bard.setChosenClass(false);
                                                    bard.sync(player);
                                                });
                                                player.getCapability(ICleric.INSTANCE).ifPresent((cleric) -> {
                                                    cleric.setChosenClass(true);
                                                    cleric.sync(player);
                                                    Networking.sendTo(player, new ClientboundClassPacket(cleric.getClassName()));
                                                });
                                                return 1;
                                            })
                                    )
                            )
                    )
            ;
            event.getDispatcher().register(builder);
        }
    }
}
