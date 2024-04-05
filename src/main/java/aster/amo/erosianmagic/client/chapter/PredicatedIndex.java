package aster.amo.erosianmagic.client.chapter;

import aster.amo.erosianmagic.util.ClientClassUtils;
import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.Index;
import elucent.eidolon.codex.Page;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Predicate;

public class PredicatedIndex extends Index {
    // This index contains 2 different sets of pages and 2 different titles depending on the predicate
    List<Page> pages1;
    List<Page> pages2;
    public PredicatedIndex(String titleKey, Page[] pages1, Page[] pages2) {
        super(titleKey, pages1);
        this.pages1 = List.of(pages1);
        this.pages2 = List.of(pages2);
    }

    @Override
    public Page get(int i) {
        boolean isCleric = ClientClassUtils.isOneOfClasses("Cleric");
        if (isCleric) {
            return i < this.size() && i >= 0 ? (Page)this.pages1.get(i) : null;
        } else {
            return i < this.size() && i >= 0 ? (Page)this.pages2.get(i) : null;
        }
    }

    @Override
    public int size() {
        boolean isCleric = ClientClassUtils.isOneOfClasses("Cleric");
        if (isCleric) {
            return pages1.size();
        } else {
            return pages2.size();
        }
    }
}
