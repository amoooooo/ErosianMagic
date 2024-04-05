package aster.amo.erosianmagic.client.pages;

import aster.amo.erosianmagic.util.ClientClassUtils;
import elucent.eidolon.Eidolon;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.codex.ChantPage;
import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.IndexPage;
import elucent.eidolon.codex.Page;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class OpenChapter extends Chapter {
    private List<Page> pages;

    public OpenChapter(String titleKey, Page... pages) {
        super(titleKey, pages);
        this.pages = List.of(pages);
    }

    public List<Page> getPages() {
        return this.pages;
    }

    public Sign[] getSigns() {
        if(pages.stream().anyMatch(it -> it instanceof ChantPage)){
            OpenChantPage page = (OpenChantPage) pages.stream().filter(it -> it instanceof OpenChantPage).findFirst().get();
            return page.getSigns();
        }
        return null;
    }
}
