package aster.amo.erosianmagic.client.pages;

import aster.amo.erosianmagic.util.ClientClassUtils;
import elucent.eidolon.Eidolon;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.IndexPage;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SignClassSpellLockedEntry extends IndexPage.SignLockedEntry {
    AbstractSpell spell;
    String clazz;
    public SignClassSpellLockedEntry(Chapter chapter, String clazz, AbstractSpell spell, ItemStack icon, Sign... signs) {
        super(chapter, icon, signs);
        this.spell = spell;
        this.clazz = clazz;
    }

    @Override
    public boolean isUnlocked() {
        Player player = Eidolon.proxy.getPlayer();
        boolean hasSpell = true;
        boolean isClass = ClientClassUtils.isOneOfClasses(clazz);
        return super.isUnlocked() && hasSpell && isClass;
    }
}
