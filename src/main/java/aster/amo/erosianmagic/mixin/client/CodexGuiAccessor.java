package aster.amo.erosianmagic.mixin.client;

import elucent.eidolon.codex.Chapter;
import elucent.eidolon.codex.CodexGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CodexGui.class)
public interface CodexGuiAccessor {
    @Invoker("changeChapter")
    void invokeChangeChapter(Chapter chapter);
}
