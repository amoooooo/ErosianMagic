package aster.amo.erosianmagic.mixin.client;

import elucent.eidolon.codex.Chapter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chapter.class)
public interface ChapterAccessor {
    @Accessor("titleKey")
    void setTitleKey(String titleKey);

    @Accessor("titleKey")
    String getTitleKey();
}
