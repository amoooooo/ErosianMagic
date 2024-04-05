package aster.amo.erosianmagic.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractSpell.class)
public class AbstractSpellMixin {

    @Inject(method = "needsLearning", at = @At("HEAD"), cancellable = true, remap = false)
    private void needsLearningMixin(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private void getDisplayNameMixin(Player player, CallbackInfoReturnable<MutableComponent> cir) {
        AbstractSpell spell = (AbstractSpell) (Object) this;
        if(spell instanceof AbstractEldritchSpell) return;
        boolean obfuscated = player != null && !spell.isLearned(player);
        cir.setReturnValue(Component.translatable(spell.getComponentId()).withStyle(obfuscated ? AbstractEldritchSpell.ELDRITCH_OBFUSCATED_STYLE : Style.EMPTY));
    }

    @Inject(method = "canBeCraftedBy", at = @At("HEAD"), cancellable = true, remap = false)
    private void canBeCraftedByMixin(Player player, CallbackInfoReturnable<Boolean> cir) {
        AbstractSpell spell = (AbstractSpell) (Object) this;
        cir.setReturnValue(spell.isLearned(player));
    }

    @Inject(method = "isLearned", at = @At("HEAD"), cancellable = true, remap = false)
    private void isLearnedMixin(Player player, CallbackInfoReturnable<Boolean> cir) {
        AbstractSpell spell = (AbstractSpell) (Object) this;
        if (player == null) {
            cir.setReturnValue(false);
        } else if (player.level().isClientSide) {
            cir.setReturnValue(ClientMagicData.getSyncedSpellData(player).isSpellLearned(spell));
        } else {
            cir.setReturnValue(MagicData.getPlayerMagicData(player).getSyncedData().isSpellLearned(spell));
        }
    }

    @Inject(method = "canBeCastedBy", at = @At("HEAD"), cancellable = true, remap = false)
    private void canBeCastedByMixin(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player, CallbackInfoReturnable<CastResult> cir) {
        AbstractSpell spell = (AbstractSpell) (Object) this;
        if(!spell.isLearned(player)) {
            cir.setReturnValue(new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.irons_spellbooks.cast_error_unlearned").withStyle(ChatFormatting.RED)));
        }
    }
}
