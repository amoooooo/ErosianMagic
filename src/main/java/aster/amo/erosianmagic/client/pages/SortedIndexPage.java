package aster.amo.erosianmagic.client.pages;

import aster.amo.erosianmagic.mixin.client.ChapterAccessor;
import aster.amo.erosianmagic.mixin.client.CodexGuiAccessor;
import aster.amo.erosianmagic.mixin.client.IndexEntryAccessor;
import elucent.eidolon.codex.CodexGui;
import elucent.eidolon.codex.IndexPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SortedIndexPage extends IndexPage {
    public final List<IndexEntry> entries = new ArrayList<>();

    public SortedIndexPage(IndexEntry... pages) {
        super(pages);
        this.entries.addAll(List.of(pages));
    }

    @Override
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        List<IndexEntry> sortedEntries = sortEntries();
        for (int i = 0; i < sortedEntries.size(); i++) {
            IndexEntry entry = sortedEntries.get(i);
            if((entry).isUnlocked() && mouseX >= x + 2 && mouseX <= x + 124 && mouseY >= y + 8 + i * 20 && mouseY <= y + 26 + i * 20) {
                ((CodexGuiAccessor)gui).invokeChangeChapter(((IndexEntryAccessor)entry).getChapter());
                assert Minecraft.getInstance().level != null;
                Minecraft.getInstance().player.playNotifySound(SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(CodexGui gui, @NotNull GuiGraphics mStack, ResourceLocation bg, int x, int y, int mouseX, int mouseY) {
        List<IndexEntry> sortedEntries = sortEntries();
        for (int i = 0; i < sortedEntries.size(); i++) {
            mStack.blit(bg, x+1, y+7+i*20, 128,((IndexEntry)sortedEntries.get(i)).isUnlocked() ? 0 : 96, 122, 18 );
            IndexEntry entry = sortedEntries.get(i);
            if(entry.isUnlocked()) {
                mStack.renderItem(((IndexEntryAccessor)entry).getIcon(), x+2, y+8+i*20);
                String trans = I18n.get(((ChapterAccessor)((IndexEntryAccessor)entry).getChapter()).getTitleKey());
                int xE = x + 24;
                int yE = y + 20 + i * 20;
                assert Minecraft.getInstance().font != null;
                drawText(mStack, trans, x, y - 9);
            }
        }
    }

    public List<IndexEntry> sortEntries(){
        // return a list of entries that are unlocked
        return entries.stream().filter(IndexEntry::isUnlocked).toList();
    }
}
