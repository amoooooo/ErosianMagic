package aster.amo.erosianmagic.client;

import aster.amo.erosianmagic.Config;
import aster.amo.erosianmagic.bard.IBard;
import aster.amo.erosianmagic.bard.song.SongPacket;
import aster.amo.erosianmagic.particle.ParticleRegistry;
import aster.amo.erosianmagic.particle.PsychicScreamParticle;
import aster.amo.erosianmagic.registry.EntityRegistry;
import aster.amo.erosianmagic.witch.eidolon.BookRegistry;
import aster.amo.erosianmagic.witch.eidolon.QuickChant;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elucent.eidolon.network.AttemptCastPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.registries.EidolonSounds;
import elucent.eidolon.registries.Registry;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.leawind.mc.thirdpersonperspective.ThirdPersonPerspective;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import yesman.epicfight.world.item.WeaponItem;

import java.util.List;

import static aster.amo.erosianmagic.ErosianMagic.MODID;

public class ErosianMagicClient {
    public static long lastCombatTime = 0;

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static
    class ErosianMagicClientMod {
        @SubscribeEvent
        public static void onCtorClient(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                BookRegistry.init();
                ErosianMagicClientForge.init();
            });
        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ParticleRegistry.PSYCHIC_SCREAM_PARTICLE_TYPE.get(), PsychicScreamParticle.Factory::new);
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityRegistry.AOE_EFFECT.get(), NoopRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FAERIE_FIRE_AOE.get(), NoopRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    static
    class ErosianMagicClientForge {
        @SubscribeEvent
        public static void onHudRender(RenderGuiOverlayEvent.Post event) {
            if (event.getOverlay().id() != VanillaGuiOverlay.HOTBAR.id()) return;
            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();
            int guiLeft = (width - 312) / 2;
            int guiTop = (height - 208) / 2;
            QuickChant.renderChant(event.getGuiGraphics(), guiLeft, guiTop, 0, 0, event.getPartialTick());
        }

        private static final Vector3f BARD_COLOR = new Vector3f(1.0f, 0.666667f, 0.0f);
//        @SubscribeEvent
//        public static void onPlayerRender(RenderPlayerEvent.Pre event) {
//            if(event.getEntity() == null) return;
//            Player player = event.getEntity();
//            player.getCapability(IBard.INSTANCE).ifPresent(bard -> {
//                if(bard.isBard()){
//                    VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.energySwirl(SpellRenderingHelper.SOLID, 0, 0));
//                    PoseStack poseStack = event.getPoseStack();
//                    float pPartialTick = event.getPartialTick();
//                    int light = event.getPackedLight();
//                    var color = BARD_COLOR;
//                    poseStack.pushPose();
//                    PoseStack.Pose pose = poseStack.last();
//                    Matrix4f poseMatrix = pose.pose();
//                    Matrix3f normalMatrix = pose.normal();
//
//                    float radius = 8;
//                    int segments = (int) (5 * radius + 9);
//                    float angle = 2 * Mth.PI / segments;
//                    float entityY = (float) Mth.lerp(pPartialTick, player.yOld, player.getY());
//
//                    for (int i = 0; i < segments; i++) {
//                        float theta = angle * i;
//                        float theta2 = angle * (i + 1);
//                        float x1 = radius * Mth.cos(theta);
//                        float x2 = radius * Mth.cos(theta2);
//                        float z1 = radius * Mth.sin(theta);
//                        float z2 = radius * Mth.sin(theta2);
//
//                        float y1 = Utils.findRelativeGroundLevel(player.level(), player.position().add(x1, player.getBbHeight(), z1), (int) (player.getBbHeight() * 2.5)) - entityY;
//                        float y2 = Utils.findRelativeGroundLevel(player.level(), player.position().add(x2, player.getBbHeight(), z2), (int) (player.getBbHeight() * 2.5)) - entityY;
//                        consumer.vertex(poseMatrix, x2, y2, z2).color(color.x(), color.y(), color.z(), 1).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0f, 1f, 0f).endVertex();
//                        consumer.vertex(poseMatrix, x2, y2 + 0.6f, z2).color(0, 0, 0, 1).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0f, 1f, 0f).endVertex();
//                        consumer.vertex(poseMatrix, x1, y1 + 0.6f, z1).color(0, 0, 0, 1).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0f, 1f, 0f).endVertex();
//                        consumer.vertex(poseMatrix, x1, y1, z1).color(color.x(), color.y(), color.z(), 1).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light * 4).normal(normalMatrix, 0f, 1f, 0f).endVertex();
//                    }
//                    poseStack.popPose();
//                }
//            });
//        }

        public static final KeyMapping KEY_1 = new KeyMapping("key.erosianmagic.1", GLFW.GLFW_KEY_1, "key.erosianmagic.category");
        public static final KeyMapping KEY_2 = new KeyMapping("key.erosianmagic.2", GLFW.GLFW_KEY_2, "key.erosianmagic.category");
        public static final KeyMapping KEY_3 = new KeyMapping("key.erosianmagic.3", GLFW.GLFW_KEY_3, "key.erosianmagic.category");
        public static final KeyMapping KEY_4 = new KeyMapping("key.erosianmagic.4", GLFW.GLFW_KEY_4, "key.erosianmagic.category");
        public static final KeyMapping KEY_5 = new KeyMapping("key.erosianmagic.5", GLFW.GLFW_KEY_5, "key.erosianmagic.category");
        public static final KeyMapping KEY_6 = new KeyMapping("key.erosianmagic.6", GLFW.GLFW_KEY_6, "key.erosianmagic.category");
        public static final KeyMapping KEY_7 = new KeyMapping("key.erosianmagic.7", GLFW.GLFW_KEY_7, "key.erosianmagic.category");
        public static final KeyMapping KEY_8 = new KeyMapping("key.erosianmagic.8", GLFW.GLFW_KEY_8, "key.erosianmagic.category");
        public static final KeyMapping KEY_9 = new KeyMapping("key.erosianmagic.9", GLFW.GLFW_KEY_9, "key.erosianmagic.category");
        public static final KeyMapping KEY_0 = new KeyMapping("key.erosianmagic.0", GLFW.GLFW_KEY_0, "key.erosianmagic.category");
        public static final KeyMapping KEY_MINUS = new KeyMapping("key.erosianmagic.minus", GLFW.GLFW_KEY_MINUS, "key.erosianmagic.category");
        public static final KeyMapping KEY_GRAVE_ACCENT = new KeyMapping("key.erosianmagic.grave_accent", GLFW.GLFW_KEY_GRAVE_ACCENT, "key.erosianmagic.category");
        public static final KeyMapping KEY_Q = new KeyMapping("key.erosianmagic.q", GLFW.GLFW_KEY_Q, "key.erosianmagic.category");
        public static final KeyMapping KEY_LMB = new KeyMapping("key.erosianmagic.lmb", GLFW.GLFW_KEY_LEFT_ALT, "key.erosianmagic.category");

        public static void init() {
            KEY_1.setKeyModifierAndCode(KeyModifier.ALT, KEY_1.getKey());
            KEY_2.setKeyModifierAndCode(KeyModifier.ALT, KEY_2.getKey());
            KEY_3.setKeyModifierAndCode(KeyModifier.ALT, KEY_3.getKey());
            KEY_4.setKeyModifierAndCode(KeyModifier.ALT, KEY_4.getKey());
            KEY_5.setKeyModifierAndCode(KeyModifier.ALT, KEY_5.getKey());
            KEY_6.setKeyModifierAndCode(KeyModifier.ALT, KEY_6.getKey());
            KEY_7.setKeyModifierAndCode(KeyModifier.ALT, KEY_7.getKey());
            KEY_8.setKeyModifierAndCode(KeyModifier.ALT, KEY_8.getKey());
            KEY_9.setKeyModifierAndCode(KeyModifier.ALT, KEY_9.getKey());
            KEY_0.setKeyModifierAndCode(KeyModifier.ALT, KEY_0.getKey());
            KEY_MINUS.setKeyModifierAndCode(KeyModifier.ALT, KEY_MINUS.getKey());
            KEY_Q.setKeyModifierAndCode(KeyModifier.ALT, KEY_Q.getKey());
            KEY_GRAVE_ACCENT.setKeyModifierAndCode(KeyModifier.ALT, KEY_GRAVE_ACCENT.getKey());
        }

        public static int cooldown = 0;
        @SubscribeEvent
        public static void onClickCapture(InputEvent.InteractionKeyMappingTriggered event) {
            if(Minecraft.getInstance().player == null) return;
            Player entity = Minecraft.getInstance().player;
            if(event.isAttack()){
                if(entity.getItemInHand(InteractionHand.MAIN_HAND).is(Registry.CODEX.get())) {
                    if(!KnowledgeUtil.knowsSign(entity, Signs.MAGIC_SIGN)) return;
                    ItemCooldowns cooldowns = entity.getCooldowns();
                    if (!cooldowns.isOnCooldown(Registry.CODEX.get())) {
                        QuickChant.add(Signs.MAGIC_SIGN);
                        Networking.sendToServer(new AttemptCastPacket(Minecraft.getInstance().player, QuickChant.getChant()));
                        AttributeInstance cdr = entity.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION.get());
                        entity.getCooldowns().addCooldown(entity.getItemInHand(InteractionHand.MAIN_HAND).getItem(), (int) (5 * (1 - (cdr.getValue() - 1))));
                        QuickChant.clear();
                    }
                    event.setSwingHand(false);
                    event.setCanceled(true);
                } else if (entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof InstrumentItem iItem) {
                    ItemCooldowns cooldowns = entity.getCooldowns();
                    if (!cooldowns.isOnCooldown(iItem)) {
                        aster.amo.erosianmagic.net.Networking.sendToServer(new SongPacket(new ResourceLocation("erosianmagic", "guiding_bolt"), false));
                        AttributeInstance cdr = entity.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION.get());
                        entity.getCooldowns().addCooldown(iItem, (int) (30 * (1 - (cdr.getValue() - 1))));
                    }
                    event.setSwingHand(false);
                    event.setCanceled(true);
                }

            }
        }

        private static boolean shouldAim = false;
        public static List<RegistryObject<Item>> aimedItems = List.of(
                ModItems.FLORAL_ZITHER,
                ModItems.GLORIOUS_DRUM,
                ModItems.VINTAGE_LYRE,
                ModItems.WINDSONG_LYRE,
                Registry.CODEX
        );
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if(event.phase == TickEvent.Phase.END){
                if(Minecraft.getInstance().player == null) return;
                if(Minecraft.getInstance().level == null) return;
                QuickChant.validateChant();
                ItemStack stack = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND);
                if(Minecraft.getInstance().level.getGameTime() - lastCombatTime < 100 && Config.autoThirdPerson) {
                    Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
                    Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_FRONT);
                }
                if(Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
                    Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_FRONT);
                }
                if((stack.getItem() instanceof WeaponItem || ClientMagicData.isCasting() || aimedItems.stream().map(RegistryObject::get).anyMatch(stack::is)) && !shouldAim) {
                    shouldAim = true;
                    ThirdPersonPerspective.Options.isForceKeepAiming = true;
                } else if(!(stack.getItem() instanceof WeaponItem || ClientMagicData.isCasting() || aimedItems.stream().map(RegistryObject::get).anyMatch(stack::is)) && shouldAim) {
                    shouldAim = false;
                    ThirdPersonPerspective.Options.isForceKeepAiming = false;
                }
            }
            if(cooldown > 0) cooldown--;
        }

        @SubscribeEvent
        public static void onButtonPress(InputEvent.Key event) {
            if(Minecraft.getInstance().player == null) return;
            Player entity = Minecraft.getInstance().player;
            if (KEY_1.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.WICKED_SIGN)) {
                QuickChant.add(Signs.WICKED_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_2.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.SACRED_SIGN)) {
                QuickChant.add(Signs.SACRED_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_3.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.BLOOD_SIGN)) {
                QuickChant.add(Signs.BLOOD_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_4.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.SOUL_SIGN)) {
                QuickChant.add(Signs.SOUL_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_5.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.MIND_SIGN)) {
                QuickChant.add(Signs.MIND_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_6.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.FLAME_SIGN)) {
                QuickChant.add(Signs.FLAME_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_7.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.WINTER_SIGN)) {
                QuickChant.add(Signs.WINTER_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_8.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.HARMONY_SIGN)) {
                QuickChant.add(Signs.HARMONY_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_9.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.DEATH_SIGN)) {
                QuickChant.add(Signs.DEATH_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_0.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.WARDING_SIGN)) {
                QuickChant.add(Signs.WARDING_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_MINUS.consumeClick() && KnowledgeUtil.knowsSign(entity, Signs.MAGIC_SIGN)) {
                QuickChant.add(Signs.MAGIC_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_GRAVE_ACCENT.consumeClick()) {
                QuickChant.clear();
                entity.playNotifySound((SoundEvent) SoundEvents.CANDLE_EXTINGUISH, SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_Q.consumeClick()) {
                Networking.sendToServer(new AttemptCastPacket(Minecraft.getInstance().player, QuickChant.getChant()));
                QuickChant.clear();
            }
        }

    }
}
