package aster.amo.erosianmagic.mixin;

import aster.amo.erosianmagic.mage.wizard.IWizard;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import dev.shadowsoffire.apotheosis.ench.table.ApothEnchantmentMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ApothEnchantmentMenu.class)
public class ApothEnchantmentMenuMixin {

    @Inject(method = "gatherStats(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)Ldev/shadowsoffire/apotheosis/ench/table/ApothEnchantmentMenu$TableStats;", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void gatherStats(Level level, BlockPos pos, int itemEnch, CallbackInfoReturnable<ApothEnchantmentMenu.TableStats> cir, ApothEnchantmentMenu.TableStats.Builder builder){
        if(!level.isClientSide) {
            List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(10));
            for(Player player : players) {
                player.getCapability(IWizard.INSTANCE).ifPresent(wiz -> {
                    if(wiz.isChosenClass()) {
                        double enchPower = player.getAttributeValue(AttributeRegistry.ENCHANTMENT_LEVEL.get());
                        builder.addArcana((int) enchPower);
                        builder.addClues((int) Math.ceil(enchPower/10));
                    }
                });
            }
            cir.setReturnValue(builder.build());
        }
    }
}
