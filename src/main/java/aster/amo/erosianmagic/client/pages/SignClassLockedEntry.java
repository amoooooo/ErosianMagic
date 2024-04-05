package aster.amo.erosianmagic.client.pages;

import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.ClientClassUtils;
import aster.amo.erosianmagic.util.IClass;
import elucent.eidolon.Eidolon;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.IndexPage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SignClassLockedEntry extends IndexPage.SignLockedEntry {
    String clazz;
    public SignClassLockedEntry(Chapter chapter, String clazz, ItemStack icon, Sign... signs) {
        super(chapter, icon, signs);
        this.clazz = clazz;
    }

    @Override
    public boolean isUnlocked() {
        Player player = Eidolon.proxy.getPlayer();
        boolean isClass = ClientClassUtils.isOneOfClasses(clazz);
        return super.isUnlocked() && isClass;
    }
}
