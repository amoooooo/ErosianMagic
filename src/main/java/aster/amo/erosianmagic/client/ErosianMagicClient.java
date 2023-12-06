package aster.amo.erosianmagic.client;

import aster.amo.erosianmagic.witch.eidolon.BookRegistry;
import aster.amo.erosianmagic.witch.eidolon.QuickChant;
import com.mojang.blaze3d.platform.InputConstants;
import elucent.eidolon.network.AttemptCastPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.registries.EidolonSounds;
import elucent.eidolon.registries.Registry;
import elucent.eidolon.registries.Signs;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import static aster.amo.erosianmagic.ErosianMagic.MODID;

public class ErosianMagicClient {
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

        @SubscribeEvent
        public static void onClickCapture(InputEvent.InteractionKeyMappingTriggered event) {
            if(Minecraft.getInstance().player == null) return;
            Player entity = Minecraft.getInstance().player;
            ItemCooldowns cooldowns = entity.getCooldowns();
            if(event.isAttack() && entity.getItemInHand(InteractionHand.MAIN_HAND).is(Registry.CODEX.get()) && !cooldowns.isOnCooldown(Registry.CODEX.get())) {
                QuickChant.add(Signs.WICKED_SIGN);
                Networking.sendToServer(new AttemptCastPacket(Minecraft.getInstance().player, QuickChant.getChant()));
                AttributeInstance cdr = entity.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION.get());
                entity.getCooldowns().addCooldown(entity.getItemInHand(InteractionHand.MAIN_HAND).getItem(), (int) (20 * (1 - (cdr.getValue() - 1))));
                QuickChant.clear();
                event.setSwingHand(false);
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onButtonPress(InputEvent.Key event) {
            if(Minecraft.getInstance().player == null) return;
            Player entity = Minecraft.getInstance().player;
            if (KEY_1.consumeClick()) {
                QuickChant.add(Signs.WICKED_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_2.consumeClick()) {
                QuickChant.add(Signs.SACRED_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_3.consumeClick()) {
                QuickChant.add(Signs.BLOOD_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_4.consumeClick()) {
                QuickChant.add(Signs.SOUL_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_5.consumeClick()) {
                QuickChant.add(Signs.MIND_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_6.consumeClick()) {
                QuickChant.add(Signs.FLAME_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_7.consumeClick()) {
                QuickChant.add(Signs.WINTER_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_8.consumeClick()) {
                QuickChant.add(Signs.HARMONY_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_9.consumeClick()) {
                QuickChant.add(Signs.DEATH_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_0.consumeClick()) {
                QuickChant.add(Signs.WARDING_SIGN);
                entity.playNotifySound((SoundEvent) EidolonSounds.SELECT_RUNE.get(), SoundSource.NEUTRAL, 0.5F, entity.level().random.nextFloat() * 0.25F + 0.75F);
            } else if (KEY_MINUS.consumeClick()) {
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
