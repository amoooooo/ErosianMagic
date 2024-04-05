package aster.amo.erosianmagic.client.pages;

import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.IClass;
import elucent.eidolon.Eidolon;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.IndexPage;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SignSpellLockedEntry extends IndexPage.SignLockedEntry {
    AbstractSpell spell;
    public SignSpellLockedEntry(Chapter chapter, AbstractSpell spell, ItemStack icon, Sign... signs) {
        super(chapter, icon, signs);
        this.spell = spell;
    }

    @Override
    public boolean isUnlocked() {
        Player player = Eidolon.proxy.getPlayer();
        boolean hasSpell = MagicData.getPlayerMagicData(player).getSyncedData().isSpellLearned(spell);
        return super.isUnlocked() && hasSpell;
    }
}
