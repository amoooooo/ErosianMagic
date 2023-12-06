package aster.amo.erosianmagic.witch.eidolon;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.api.spells.SignSequence;
import elucent.eidolon.client.ClientRegistry;
import elucent.eidolon.event.ClientEvents;
import elucent.eidolon.network.AttemptCastPacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.registries.Spells;
import elucent.eidolon.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static elucent.eidolon.codex.CodexGui.blit;

public class QuickChant {
    static final List<Sign> chant = new ArrayList<>();

    public static int tickTime = 0;
    public static void add(Sign sign) {
        chant.add(sign);
        tickTime = 0;
    }

    public static void validateChant() {
        if(chant.isEmpty()) {
            return;
        }
        if(tickTime++ < 10) {
            return;
        }
        tickTime = 0;
        if(Spells.find(new SignSequence(chant)) != null){
            Networking.sendToServer(new AttemptCastPacket(Minecraft.getInstance().player, QuickChant.getChant()));
            clear();
        }
    }

    public static void clear() {
        chant.clear();
    }

    public static List<Sign> getChant() {
        return chant;
    }

    public List<Sign> signs() {
        return chant;
    }

    public static void renderChant(@NotNull GuiGraphics mStack, int x, int y, int mouseX, int mouseY, float pticks) {
        if(chant.isEmpty()) {
            return;
        }
        Font font = Minecraft.getInstance().font;
        int chantWidth = 32 + 24 * chant.size();
        int baseX = x + 156 - chantWidth / 2;
        int baseY = y + 180;
        RenderSystem.enableBlend();
        blit(mStack, baseX, baseY, 256, 208, 16, 32, 512, 512);
        int bgx = baseX + 16;

        for(int i = 0; i < chant.size(); ++i) {
            blit(mStack, bgx, baseY, 272, 208, 24, 32, 512, 512);
            blit(mStack, bgx, baseY, 312, 208, 24, 24, 512, 512);
            bgx += 24;
        }

        blit(mStack, bgx, baseY, 296, 208, 16, 32, 512, 512);
        bgx += 24;
        blit(mStack, bgx, baseY - 4, 336, 208, 32, 32, 512, 512);
        bgx += 36;
        blit(mStack, bgx, baseY - 4, 368, 208, 32, 32, 512, 512);

        RenderSystem.enableBlend();
        RenderSystem.setShader(ClientRegistry::getGlowingSpriteShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        bgx = baseX + 16;
        MultiBufferSource.BufferSource buffersource = Minecraft.getInstance().renderBuffers().bufferSource();

        for(Iterator<Sign> var14 = chant.iterator(); var14.hasNext(); bgx += 24) {
            Sign sign = (Sign)var14.next();
            RenderUtil.litQuad(mStack.pose(), buffersource, (double)(bgx + 4), (double)(baseY + 4), 16.0, 16.0, sign.getRed(), sign.getGreen(), sign.getBlue(), (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(sign.getSprite()));
            buffersource.endBatch();
        }

        bgx = baseX + 16;
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        for(int i = 0; i < chant.size(); ++i) {
            float flicker = 0.75F + 0.25F * (float)Math.sin(Math.toRadians((double)(12.0F * ClientEvents.getClientTicks() - 360.0F * (float)i / (float)chant.size())));
            Sign sign = (Sign)chant.get(i);
            RenderUtil.litQuad(mStack.pose(), buffersource, (double)(bgx + 4), (double)(baseY + 4), 16.0, 16.0, sign.getRed() * flicker, sign.getGreen() * flicker, sign.getBlue() * flicker, (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(sign.getSprite()));
            buffersource.endBatch();
            bgx += 24;
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
