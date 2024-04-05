package aster.amo.erosianmagic.client.pages;

import elucent.eidolon.Eidolon;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.api.spells.Spell;
import elucent.eidolon.codex.ChantPage;
import elucent.eidolon.recipe.ChantRecipe;

public class OpenChantPage extends ChantPage {
    Spell spell;
    public OpenChantPage(String textKey, Spell spell) {
        super(textKey, spell);
        this.spell = spell;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public Sign[] getSigns() {
        if(this.spell != null) {
            Object v = Eidolon.proxy.getWorld().getRecipeManager().byKey(spell.getRegistryName()).orElse(null);
            if(v instanceof ChantRecipe) {
                return ((ChantRecipe) v).signs();
            }
        }
        return null;
    }
}
