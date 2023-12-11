package aster.amo.erosianmagic.bard;

import aster.amo.erosianmagic.util.ClassUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.spellbook.SpellBookData;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientRenderCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class BardSpellBarOverlay implements IGuiOverlay {
    public static final BardSpellBarOverlay instance = new BardSpellBarOverlay();

    public final static ResourceLocation TEXTURE = new ResourceLocation(IronsSpellbooks.MODID, "textures/gui/icons.png");
    static final int IMAGE_HEIGHT = 21;
    static final int IMAGE_WIDTH = 21;
    static final int HOTBAR_HALFWIDTH = 91;
    static final int boxSize = 20;
    static int screenHeight;
    static int screenWidth;

    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;

        if (!ClassUtils.isBard(player))
            return;

        int centerX, centerY;
        centerX = screenWidth / 2 - Math.max(110, screenWidth / 4);
        centerY = screenHeight - Math.max(55, screenHeight / 8);

        //
        //  Render Spells
        //
        MagicData magicData = MagicData.getPlayerMagicData(player);
        var spellBookData = new SpellBookData(magicData.getPlayerCooldowns().getSpellCooldowns().size());
        magicData.getPlayerCooldowns().getSpellCooldowns().forEach((spell, cooldown) -> {
            spellBookData.addSpell(SpellRegistry.getSpell(spell), 1, null);
        });
        ClientRenderCache.generateRelativeLocations(spellBookData, 20, 22);

        var spells = spellBookData.getInscribedSpells();
        var locations = ClientRenderCache.relativeSpellBarSlotLocations;
        int approximateWidth = locations.size() / 3;
        //Move spellbar away from hotbar as it gets bigger
        centerX -= approximateWidth * 5;

        //Slot Border
        //setTranslucentTexture(TEXTURE);
        for (Vec2 location : locations) {
            guiHelper.blit(TEXTURE, centerX + (int) location.x, centerY + (int) location.y, 66, 84, 22, 22);
        }
        //Spell Icons
        for (int i = 0; i < locations.size(); i++) {
            if (spells[i] != null) {
                //setOpaqueTexture(spells[i].getSpellType().getResourceLocation());
                guiHelper.blit(spells[i].getSpell().getSpellIconResource(), centerX + (int) locations.get(i).x + 3, centerY + (int) locations.get(i).y + 3, 0, 0, 16, 16, 16, 16);
            }
        }
        //Border + Cooldowns
        for (int i = 0; i < locations.size(); i++) {
            //setTranslucentTexture(TEXTURE);
            if (i != spellBookData.getActiveSpellIndex())
                guiHelper.blit(TEXTURE, centerX + (int) locations.get(i).x, centerY + (int) locations.get(i).y, 22, 84, 22, 22);

            float f = spells[i] == null ? 0 : ClientMagicData.getCooldownPercent(spells[i].getSpell());
            if (f > 0) {
                int pixels = (int) (16 * f + 1f);
                guiHelper.blit(TEXTURE, centerX + (int) locations.get(i).x + 3, centerY + (int) locations.get(i).y + 19 - pixels, 47, 87, 16, pixels);
            }
        }
        //Selected Outline
        for (int i = 0; i < locations.size(); i++) {
            //setTranslucentTexture(TEXTURE);
            if (i == spellBookData.getActiveSpellIndex())
                guiHelper.blit(TEXTURE, centerX + (int) locations.get(i).x, centerY + (int) locations.get(i).y, 0, 84, 22, 22);
        }
    }

    private static void setOpaqueTexture(ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }

    private static void setTranslucentTexture(ResourceLocation texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeTranslucentShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }


}