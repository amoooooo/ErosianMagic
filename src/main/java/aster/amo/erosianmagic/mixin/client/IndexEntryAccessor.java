package aster.amo.erosianmagic.mixin.client;

import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.IndexPage;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(IndexPage.IndexEntry.class)
public interface IndexEntryAccessor {
    @Accessor("chapter")
    void setChapter(Chapter chapter);

    @Accessor("chapter")
    Chapter getChapter();

    @Accessor("icon")
    void setIcon(ItemStack chapter);

    @Accessor("icon")
    ItemStack getIcon();

}
