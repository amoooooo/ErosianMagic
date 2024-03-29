package aster.amo.erosianmagic.divine.witch.eidolon;

import elucent.eidolon.codex.*;
import elucent.eidolon.registries.Registry;
import elucent.eidolon.registries.Signs;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BookRegistry {
    public static Category MYSTIC_ARTS;
    public static Index MYSTIC_ARTS_INDEX;
    public static Chapter MAGIC_MISSILE_CHAPTER;
    public static Chapter HEXBLOOD_CHAPTER;
    public static Chapter BLACK_HOLE_CHAPTER;
    public static void init() {
        MAGIC_MISSILE_CHAPTER = new Chapter("erosianmagic.codex.chapter.magic_missile",
                new ChantPage("erosianmagic.codex.page.magic_missile.0", ChantRegistry.MAGIC_MISSILE),
                new TextPage("erosianmagic.codex.page.magic_missile.1"));
        HEXBLOOD_CHAPTER = new Chapter("erosianmagic.codex.chapter.hexblood",
                new ChantPage("erosianmagic.codex.page.hexblood.0", ChantRegistry.HEXBLOOD),
                new TextPage("erosianmagic.codex.page.hexblood.1"));
        BLACK_HOLE_CHAPTER = new Chapter("erosianmagic.codex.chapter.black_hole",
                new ChantPage("erosianmagic.codex.page.black_hole.0", ChantRegistry.BLACK_HOLE),
                new TextPage("erosianmagic.codex.page.black_hole.1"));
        MYSTIC_ARTS_INDEX = new Index("erosianmagic.codex.chapter.mystic_arts", new IndexPage(
                new IndexPage.SignLockedEntry(MAGIC_MISSILE_CHAPTER, new ItemStack(Items.AMETHYST_SHARD), Signs.WICKED_SIGN),
                new IndexPage.SignLockedEntry(HEXBLOOD_CHAPTER, new ItemStack(Items.POTION), Signs.WICKED_SIGN, Signs.BLOOD_SIGN),
                new IndexPage.IndexEntry(BLACK_HOLE_CHAPTER, new ItemStack(Items.ENDER_PEARL))
        ));
        CodexChapters.categories.add(MYSTIC_ARTS = new Category(
                "mystic_arts",
                new ItemStack(Registry.ANGELS_SIGHT.get()),
                ColorUtil.packColor(255, 134, 67, 255),
                MYSTIC_ARTS_INDEX
        ));
    }
}
