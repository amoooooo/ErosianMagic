package aster.amo.erosianmagic.mixin.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE, ordinal = 1))
    private void renderTitleBar(GuiGraphics pGuiGraphics, float pPartialTick, CallbackInfo ci) {
        Window window = Minecraft.getInstance().getWindow();
        int barHeight = 200;
        int yCenter = window.getGuiScaledHeight() / 2;
        int black = 0x88000000;
        pGuiGraphics.fill(0, yCenter + barHeight / 2, window.getGuiScaledWidth(), yCenter - barHeight / 2, 0, black);
    }
}
