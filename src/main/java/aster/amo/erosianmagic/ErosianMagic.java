package aster.amo.erosianmagic;

import aster.amo.erosianmagic.divine.IDivine;
import aster.amo.erosianmagic.divine.witch.coven.IDedicant;
import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.fighter.barbarian.IBarbarian;
import aster.amo.erosianmagic.fighter.champion.IChampion;
import aster.amo.erosianmagic.fighter.paladin.IPaladin;
import aster.amo.erosianmagic.mage.IMage;
import aster.amo.erosianmagic.mage.bard.IBard;
import aster.amo.erosianmagic.divine.cleric.ICleric;
import aster.amo.erosianmagic.divine.cleric.PrayerRegistry;
import aster.amo.erosianmagic.divine.cleric.chapel.IWorshipper;
import aster.amo.erosianmagic.mage.bard.song.SongRegistry;
import aster.amo.erosianmagic.mage.machinist.IMachinist;
import aster.amo.erosianmagic.mage.wizard.IWizard;
import aster.amo.erosianmagic.net.ClientboundClassPacket;
import aster.amo.erosianmagic.net.CombatTimerPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.particle.ParticleRegistry;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import aster.amo.erosianmagic.registry.EntityRegistry;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.rogue.IRogue;
import aster.amo.erosianmagic.rogue.charlatan.ICharlatan;
import aster.amo.erosianmagic.rogue.monk.IMonk;
import aster.amo.erosianmagic.rogue.ranger.IRanger;
import aster.amo.erosianmagic.rolls.IRoller;
import aster.amo.erosianmagic.spellsnspellbooks.ClassSpells;
import aster.amo.erosianmagic.util.BossUtil;
import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.DelayHandler;
import aster.amo.erosianmagic.util.IClass;
import aster.amo.erosianmagic.divine.witch.IWitch;
import aster.amo.erosianmagic.divine.witch.eidolon.ChantRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.item.GunItem;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.common.item.CodexItem;
import elucent.eidolon.common.spell.StaticSpell;
import elucent.eidolon.recipe.ChantRecipe;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.registries.Spells;
import elucent.eidolon.util.KnowledgeUtil;
import forge.net.mca.entity.VillagerEntityMCA;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import jackiecrazy.footwork.event.EntityAwarenessEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import smartin.miapi.datapack.ReloadEvents;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Mod(ErosianMagic.MODID)
public class ErosianMagic {

    public static final String MODID = "erosianmagic";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static long lastDamagedTime = 0;
    public static LootCategory INSTRUMENT = LootCategory.register(LootCategory.SWORD, "instrument", s -> s.getItem() instanceof InstrumentItem, arr(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    public static LootCategory CODEX = LootCategory.register(LootCategory.SWORD, "codex", s -> s.getItem() instanceof CodexItem, arr(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    public static LootCategory FIREARM = LootCategory.register(LootCategory.SWORD, "firearm", s -> s.getItem() instanceof GunItem, arr(EquipmentSlot.MAINHAND));
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
        ClassSpells.init();
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
    public static class ForgeEvents {

        @SubscribeEvent
        public static void serverTick(TickEvent.LevelTickEvent event) {
            if(event.side.isServer()){
                if (event.phase == TickEvent.Phase.END){
                    DelayHandler.tick();
                }
            }
        }

        @SubscribeEvent
        public static void gunFire(GunFireEvent.Pre event) {
            Player player = event.getEntity();
            if(!ClassUtils.getChosenClassName(player).equals("Machinist")) {
                event.setCanceled(true);
            }
        }


        @SubscribeEvent
        public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(MODID, "fighter"), new IFighter.Provider());
                event.addCapability(new ResourceLocation(MODID, "barbarian"), new IBarbarian.Provider());
                event.addCapability(new ResourceLocation(MODID, "champion"), new IChampion.Provider());
                event.addCapability(new ResourceLocation(MODID, "paladin"), new IPaladin.Provider());
                event.addCapability(new ResourceLocation(MODID, "divine"), new IDivine.Provider());
                event.addCapability(new ResourceLocation(MODID, "rogue"), new IRogue.Provider());
                event.addCapability(new ResourceLocation(MODID, "mage"), new IMage.Provider());
                event.addCapability(new ResourceLocation(MODID, "witch"), new IWitch.Provider());
                event.addCapability(new ResourceLocation(MODID, "machinist"), new IMachinist.Provider());
                event.addCapability(new ResourceLocation(MODID, "bard"), new IBard.Provider());
                event.addCapability(new ResourceLocation(MODID, "cleric"), new ICleric.Provider());
                event.addCapability(new ResourceLocation(MODID, "wizard"), new IWizard.Provider());
                event.addCapability(new ResourceLocation(MODID, "charlatan"), new ICharlatan.Provider());
                event.addCapability(new ResourceLocation(MODID, "monk"), new IMonk.Provider());
                event.addCapability(new ResourceLocation(MODID, "ranger"), new IRanger.Provider());

            }
            if(event.getObject() instanceof VillagerEntityMCA) {
                event.addCapability(new ResourceLocation(MODID, "worshipper"), new IWorshipper.Provider());
            }
            if(event.getObject() instanceof Witch) {
                event.addCapability(new ResourceLocation(MODID, "dedicant"), new IDedicant.Provider());
            }
            if(event.getObject() instanceof LivingEntity){
                event.addCapability(new ResourceLocation(MODID, "roller"), new IRoller.Provider());
            }
        }

        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            ClassUtils.CLASSES.values().forEach(capability -> {
                event.getOriginal().getCapability(capability).ifPresent(original -> {
                    event.getEntity().getCapability(capability).ifPresent(clazz -> {
                        ((INBTSerializable<CompoundTag>) clazz).deserializeNBT(((INBTSerializable<CompoundTag>) original).serializeNBT());
                        clazz.sync(event.getEntity());
                    });
                });
                event.getOriginal().getCapability(capability).invalidate();
            });
        }

        @SubscribeEvent
        public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            IClass chosenClass = ClassUtils.getChosenClass(player);
            if(chosenClass != null) {
                Networking.sendTo(player, new ClientboundClassPacket(chosenClass.getClassName()));
                if(chosenClass instanceof ICleric cleric && cleric.hasTemple()) {
                    player.sendSystemMessage(Component.literal("You have " + cleric.getWorshippers().size() + " worshippers."));
                }
            }
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
        public static void cancelMana(ChangeManaEvent event){
            Player player = event.getEntity();
            IClass chosenClass = ClassUtils.getChosenClass(player);
            if(chosenClass instanceof IWitch || chosenClass instanceof ICleric) {
                event.setCanceled(true);
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
                player.getCapability(IWitch.INSTANCE).ifPresent((witch) -> {
                    if(witch.isChosenClass())
                        witch.getCoven().tick((ServerPlayer)player);
                });
                IClass chosenClass = ClassUtils.getChosenClass(player);
                if(chosenClass != null) {
                    chosenClass.sync(player);
                }
            }
        }

        @SubscribeEvent
        public static void onBossDeath(LivingDeathEvent event) {
            if(event.getEntity().level().isClientSide) return;
            LivingEntity entity = event.getEntity();
            if(entity.getType().is(Tags.EntityTypes.BOSSES)) {
                List<ServerPlayer> players = entity.level().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(32));
                players.forEach(p -> {
                    BossUtil.sendBossDefeatedTitle(p, entity.blockPosition());
                });
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void randomizeHurt(LivingHurtEvent event) {
            if(event.getAmount() < 2.0f) return;
            if (event.getSource().getDirectEntity() instanceof Player player) {
                damageRoll(event, player);
            } else if (event.getSource().getDirectEntity() instanceof LivingEntity) {
                LivingEntity le = event.getEntity();
                damageRoll(event, le);
            } else if(event.getSource().getDirectEntity() instanceof Projectile proj){
                if(proj.getOwner() instanceof Player player){
                    damageRoll(event, player);
                } else if(proj.getOwner() instanceof LivingEntity le){
                    damageRoll(event, le);
                }
            } else if(event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player) && !(event.getSource().getDirectEntity() instanceof Player) && !(event.getSource().getDirectEntity() instanceof LivingEntity)) {
                float roll = rollWithLuck(event.getEntity(), event.getEntity().getAttributeValue(Attributes.LUCK));
                double modifier = 1.0 + 0.25 * Math.sin((Math.PI / 18) * (roll - 10));
                event.setAmount((float) (event.getAmount() * modifier));
                event.getEntity().getCapability(IRoller.INSTANCE).ifPresent(roller -> {
                    roller.setRoll((int) roll);
                    roller.sync(event.getEntity());
                });
            }
        }

        private static void damageRoll(LivingHurtEvent event, Component displayName, LivingEntity pl) {
            int roll = rollWithLuck(pl, pl.getAttributeValue(Attributes.LUCK));
            if (roll == 1) {
                List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                players.forEach(p -> {
                    p.sendSystemMessage(displayName.copy().append(Component.literal(" rolled a natural 1!")).withStyle(ChatFormatting.RED));
                });
            } else if (roll == 20) {
                List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                players.forEach(p -> {
                    p.sendSystemMessage(displayName.copy().append(Component.literal(" rolled a natural 20!")).withStyle(ChatFormatting.GREEN));
                });
            }

            double modifier = 1.0 + 0.25 * Math.sin((Math.PI / 18) * (roll - 10));
            event.setAmount((float) (event.getAmount() * modifier));
            pl.getCapability(IRoller.INSTANCE).ifPresent(roller -> {
                roller.setRoll(roll);
                roller.sync(pl);
            });
        }

        private static void damageRoll(LivingHurtEvent event, LivingEntity le) {
            damageRoll(event, le.getDisplayName(), le);
        }
//        public static List<StaticSpell> SPELLS = new ArrayList<>();
//        @SubscribeEvent
//        public static void temp(PlayerEvent.PlayerLoggedInEvent event) {
//            if(event.getEntity().level().isClientSide) return;
//            SPELLS.forEach(spell -> {
//                ResourceLocation name = spell.getRegistryName();
//                Sign[] signs = spell.signs.toArray();
//                ChantRecipe r = new ChantRecipe(name, Arrays.stream(signs).toList());
//                JsonObject jo = r.toJson();
//                // write to file located at c:/chant recipes/
//                File file = new File("./Chant Recipes/");
//                if(!file.exists()) file.mkdir();
//                Path writer = Paths.get("./Chant Recipes/" + name.getPath() + ".json");
//
//                Gson gson = new Gson();
//                String sj = gson.toJson(jo);
//                try {
//                    java.nio.file.Files.write(writer, sj.getBytes());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//        }

        @SubscribeEvent
        public static void entityTick(LivingEvent.LivingTickEvent event) {
            if(event.getEntity().level().isClientSide) {
                return;
            }
            event.getEntity().getCapability(IRoller.INSTANCE).ifPresent(roller -> {
                if(roller.shouldDisplayRoll()) {
                    roller.tickRoll();
                    roller.setFakeRoll(event.getEntity().getRandom().nextInt(20) + 1);
                    roller.sync(event.getEntity());
                }
            });
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
            if(event.getAmount() < 2) return;
            if(event.getEntity().tickCount < 10) return;
            if (event.getEntity() instanceof Player player) {
                int roll = rollWithLuck(player, player.getAttributeValue(Attributes.LUCK));
                if(event.getEntity().hasEffect(MobEffectRegistry.HOPEFUL.get())){
                    roll = 19;
                    player.sendSystemMessage(Component.literal("The Beacon of Hope shines upon you!").withStyle(ChatFormatting.GREEN));
                }
                if (roll == 1) {
                    List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                    players.forEach(p -> {
                        p.sendSystemMessage(player.getDisplayName().copy().append(Component.literal(" rolled a natural 1!")).withStyle(ChatFormatting.RED));
                    });
                } else if (roll == 20) {
                    List<Player> players = event.getEntity().level().getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(32));
                    players.forEach(p -> {
                        p.sendSystemMessage(player.getDisplayName().copy().append(Component.literal(" rolled a natural 20!")).withStyle(ChatFormatting.GREEN));
                    });
                }
                double modifier = 1.0 + 0.25 * Math.sin((Math.PI / 18) * (roll - 10));
                event.setAmount((float) (event.getAmount() * modifier));
                int finalRoll = roll;
                player.getCapability(IRoller.INSTANCE).ifPresent(roller -> {
                    roller.setRoll(finalRoll);
                    roller.sync(player);
                });
            } else {
                int roll = rollWithLuck(event.getEntity(), event.getEntity().getAttributeValue(Attributes.LUCK));
                double modifier = 1.0 + 0.25 * Math.sin((Math.PI / 18) * (roll - 10));
                event.setAmount((float) (event.getAmount() * modifier));
                event.getEntity().getCapability(IRoller.INSTANCE).ifPresent(roller -> {
                    roller.setRoll(roll);
                    roller.sync(event.getEntity());
                });
            }
        }

        public static int rollWithLuck(LivingEntity entity, double luck){
            float baseChance = 1.0f / 20.0f;
            double adjustedLuck = luck / 1024.0D;
            if(entity.hasEffect(MobEffectRegistry.SHIELD_OF_FAITH.get())){
                adjustedLuck += adjustedLuck * 0.05 * entity.getEffect(MobEffectRegistry.SHIELD_OF_FAITH.get()).getAmplifier();
            }
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
            builder.requires((commandSource) -> commandSource.hasPermission(2))
                    .then(Commands.literal("dumpSpells")
                            .executes((commandSource) -> {
                                Path writer = null;
                                try {
                                    File dir = new File("./erosianmagic");
                                    if(!dir.exists()){
                                        dir.mkdir();
                                    }
                                    writer = Path.of("./erosianmagic/spells.txt");
                                    Collection<AbstractSpell> spells = io.redspace.ironsspellbooks.api.registry.SpellRegistry.REGISTRY.get().getValues();
                                    Multimap<SchoolType, AbstractSpell> spellMap = ArrayListMultimap.create();
                                    List<String> lines = new ArrayList<>();
                                    spells.forEach(spell -> {
                                        spellMap.put(spell.getSchoolType(), spell);
                                    });
                                    for(SchoolType type : spellMap.keySet()){
                                        lines.add("###  " + type.getId().getPath() + "  ###");
                                        spellMap.get(type).forEach(spell -> {
                                            lines.add(" - "+io.redspace.ironsspellbooks.api.registry.SpellRegistry.REGISTRY.get().getKey(spell).toString());
                                        });
                                    }

                                    Files.write(writer, lines);
                                } catch (Exception exception){
                                    System.out.println(exception.getMessage());
                                }
                                return 1;
                            }))
                    .then(Commands.literal("grant")
                            .then(Commands.argument("sign", StringArgumentType.string())
                                    .suggests((commandContext, suggestionsBuilder) -> {
                                        return SharedSuggestionProvider.suggest(Signs.getSigns().stream().map(s -> s.getRegistryName().getPath()).collect(Collectors.toList()), suggestionsBuilder);
                                    })
                                    .executes((commandSource) -> {
                                        Player player = commandSource.getSource().getPlayerOrException();
                                        String sign = StringArgumentType.getString(commandSource, "sign");
                                        if(sign.equals("all")){
                                            for (Sign s : Signs.getSigns()) {
                                                KnowledgeUtil.grantSign(player, s);
                                            }
                                            return 1;
                                        }
                                        KnowledgeUtil.grantSign(player, Signs.find(new ResourceLocation("eidolon"+StringArgumentType.getString(commandSource, "sign"))));
                                        return 1;
                                    })))
                    .then(Commands.literal("level")
                            .then(Commands.argument("type", StringArgumentType.word())
                                    .then(Commands.argument("level", IntegerArgumentType.integer(1))
                                            .executes((commandSource) -> {
                                                Player player = commandSource.getSource().getPlayerOrException();
                                                int level = IntegerArgumentType.getInteger(commandSource, "level");
                                                String type = StringArgumentType.getString(commandSource, "type");
                                                IClass chosenClass = ClassUtils.getChosenClass(player);
                                                if(chosenClass != null) {
                                                    if(type.equals("set")) {
                                                        chosenClass.setLevel(level);
                                                        chosenClass.sync(player);
                                                    } else if(type.equals("add")) {
                                                        chosenClass.setLevel(chosenClass.getLevel() + level);
                                                        chosenClass.sync(player);
                                                    } else if(type.equals("subtract")) {
                                                        chosenClass.setLevel(chosenClass.getLevel() - level);
                                                        chosenClass.sync(player);
                                                    } else {
                                                        player.sendSystemMessage(Component.literal("Invalid type: " + type).withStyle(ChatFormatting.RED));
                                                        return 0;
                                                    }
                                                }
                                                return 1;
                                            })))
                            )
                    .then(Commands.literal("set")
                            .then(Commands.argument("class", StringArgumentType.word())
                                    .suggests((commandContext, suggestionsBuilder) -> {
                                        return SharedSuggestionProvider.suggest(ClassUtils.CLASSES.keySet(), suggestionsBuilder);
                                    }).then(Commands.argument("player", EntityArgument.player())
                                            .executes((commandSource) -> {
                                                Player player = EntityArgument.getPlayer(commandSource, "player");
                                                String className = StringArgumentType.getString(commandSource, "class");
                                                Capability<? extends IClass> clazz = ClassUtils.CLASSES.get(className);
                                                player.getCapability(clazz).ifPresent(clazz1 -> {
                                                    clazz1.setChosenClass(true, player);
                                                    clazz1.sync(player);
                                                    clazz1.onSetClass(player);
                                                });
                                                IClass chosenClass = ClassUtils.getChosenClass(player);
                                                if(chosenClass != null) {
                                                    player.sendSystemMessage(Component.literal("Attempting to set class to: " + chosenClass.getClassName()));
                                                    Networking.sendTo(player, new ClientboundClassPacket(chosenClass.getClassName()));
                                                }
                                                for (Capability<? extends IClass> capability : ClassUtils.CLASSES.values()) {
                                                    if (capability != clazz) {
                                                        player.getCapability(capability).ifPresent(clazz1 -> {
                                                            clazz1.setChosenClass(false, player);
                                                            clazz1.sync(player);
                                                            boolean shouldSetOtherClass = true;
                                                            for(Class<? extends IClass> cl : ClassUtils.BASE_CLASSES) {
                                                                if (cl.isInstance(clazz1) && cl.isInstance(chosenClass)) {
                                                                    shouldSetOtherClass = false;
                                                                    break;
                                                                }
                                                            }
                                                            if(shouldSetOtherClass)
                                                                clazz1.onSetOtherClass(player);
                                                        });
                                                    }
                                                }
                                                return 1;
                                            }))))
                    .then(Commands.literal("booktest")
                            .then(Commands.argument("pos", Vec3Argument.vec3())
                                    .executes((commandSource) -> {
                                        Player player = commandSource.getSource().getPlayerOrException();
                                        Vec3 pos = Vec3Argument.getVec3(commandSource, "pos");
                                        SongRegistry.getSongs().forEach((rl, song) -> {
                                            ItemEntity ie = new ItemEntity(player.level(), pos.x, pos.y, pos.z, song.toSignedBook());
                                            player.level().addFreshEntity(ie);
                                        });
                                        return 1;
                                    })))
                    .then(Commands.literal("dumpSigns")
                            .executes((commandSource) -> {
                                Path writer = null;
                                try {
                                    File dir = new File("./erosianmagic");
                                    if(!dir.exists()){
                                        dir.mkdir();
                                    }
                                    writer = Path.of("./erosianmagic/signs.txt");
                                    List<String> lines = new ArrayList<>();
                                    Spells.getSpells().forEach(spell -> {
                                        StringBuilder b = new StringBuilder();
                                        b.append(spell.getRegistryName().toString()).append(": [");
                                        ((StaticSpell)spell).signs.seq.forEach(sign -> {
                                            b.append(sign.getRegistryName().getPath()).append(", ");
                                        });
                                        b.append("]");
                                        lines.add(b.toString());
                                    });
                                    Files.write(writer, lines);
                                } catch (Exception exception){
                                    System.out.println(exception.getMessage());
                                }
                                return 1;
                            }))
            ;
            event.getDispatcher().register(builder);
        }
    }
}
