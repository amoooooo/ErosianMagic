package aster.amo.erosianmagic.mixin;

import dev.shadowsoffire.apotheosis.adventure.AdventureConfig;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.loot.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

@Mixin(AffixConvertLootModifier.class)
public class AffixConvertLootModifierMixin {
    @Inject(method = "doApply", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void cancelRarity(ObjectArrayList<ItemStack> generatedLoot, LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir, Iterator var3, AdventureConfig.LootPatternMatcher m) {
        if(!context.hasParam(LootContextParams.THIS_ENTITY)) return;
        if(!(context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player)) return;
        RarityClamp rarityClamp = (RarityClamp)AdventureConfig.AFFIX_CONVERT_RARITIES.get(context.getLevel().dimension().location());
        if(GameStageHelper.hasStage(player, "common")) {
            rarityClamp = new RarityClamp.Simple(RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:common")), RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:common")));
        }
        if(GameStageHelper.hasStage(player, "uncommon")) {
            rarityClamp = new RarityClamp.Simple(RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:common")), RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:uncommon")));
        }
        if(GameStageHelper.hasStage(player, "rare")) {
            rarityClamp = new RarityClamp.Simple(RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:uncommon")), RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:rare")));
        }
        if(GameStageHelper.hasStage(player, "epic")) {
            rarityClamp = new RarityClamp.Simple(RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:rare")), RarityRegistry.INSTANCE.holder(new ResourceLocation("apotheosis:epic")));
        }
        if(GameStageHelper.hasStage(player, "mythic")) {
            rarityClamp = AdventureConfig.AFFIX_CONVERT_RARITIES.get(context.getLevel().dimension().location());
        }
        RandomSource rand = context.getRandom();
        float luck = context.getLuck();
        for (ItemStack s : generatedLoot) {
            if (!LootCategory.forItem(s).isNone() && AffixHelper.getAffixes(s).isEmpty() && rand.nextFloat() <= m.chance()) {
                LootController.createLootItem(s, LootRarity.random(rand, luck, rarityClamp), rand);
            }
        }
        cir.setReturnValue(generatedLoot);
    }
}
