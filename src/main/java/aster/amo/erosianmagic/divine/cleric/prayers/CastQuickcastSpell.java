package aster.amo.erosianmagic.divine.cleric.prayers;

import elucent.eidolon.api.deity.Deity;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.common.spell.StaticSpell;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SoulUpdatePacket;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import static io.redspace.ironsspellbooks.api.util.Utils.getPlayerSpellbookStack;

public class CastQuickcastSpell extends StaticSpell {
    final Deity deity;

    public CastQuickcastSpell(ResourceLocation name, Deity deity, Sign... signs) {
        super(name, signs);
        this.deity = deity;
    }


    @Override
    public boolean canCast(Level level, BlockPos blockPos, Player player) {
        return !this.reputationCheck(level, player, 0.0D);
    }

    @Override
    public void cast(Level level, BlockPos blockPos, Player player) {
        if (!level.isClientSide) {
            player.getCapability(ISoul.INSTANCE, (Direction)null).ifPresent((soul) -> {
                ItemStack spellbookStack = getPlayerSpellbookStack(player);
                SpellSelectionManager sbd = new SpellSelectionManager(player);
                if (sbd.getSpellCount() > 0 && sbd.getSelection() != null && !sbd.getSelection().equals(SpellData.EMPTY)) {
                    SpellData spellData = sbd.getSelection().spellData;
                    if (spellData != null) {
                        MagicData playerMagicData = MagicData.getPlayerMagicData(player);
                        float cost = (float) (1.0f/spellData.getSpell().getMaxLevel()) * 10.0f * signs.toArray().length;
                        if(!soul.hasMagic() || soul.getMagic() < cost) return;
                        if (playerMagicData.isCasting() && !playerMagicData.getCastingSpellId().equals(spellData.getSpell().getSpellId())) {
                            ServerboundCancelCast.cancelCast((ServerPlayer) player, playerMagicData.getCastType() != CastType.LONG);
                        }
                        spellData.getSpell().attemptInitiateCast(spellbookStack, spellData.getLevel(), player.level(), player, CastSource.SPELLBOOK, true, "custom");
                        level.getCapability(IReputation.INSTANCE, (Direction)null).ifPresent((rep) -> {
                            rep.pray(player, this.getRegistryName(), level.getGameTime());
                            rep.addReputation(player, this.deity.getId(), 0.1 + 0.25 * spellData.getLevel());
                        });
                        if(cost <= 0.0) return;
                        soul.takeMagic(cost);
                        Networking.sendTo((ServerPlayer) player, new SoulUpdatePacket(player));
                    }
                }

            });

        }

    }

    protected double getDevotion(Level level, Player player) {
        LazyOptional<IReputation> iReputationLazyOptional = level.getCapability(IReputation.INSTANCE);
        if (iReputationLazyOptional.resolve().isEmpty()) {
            return 0.0D;
        } else {
            IReputation iReputation = iReputationLazyOptional.resolve().get();
            return iReputation.getReputation(player.getUUID(), this.deity.getId());
        }
    }

    protected boolean reputationCheck(Level world, Player player, double minDevotion) {
        LazyOptional<IReputation> iReputationLazyOptional = world.getCapability(IReputation.INSTANCE);
        if (iReputationLazyOptional.resolve().isEmpty()) {
            return true;
        } else {
            IReputation iReputation = (IReputation)iReputationLazyOptional.resolve().get();
            return iReputation.getReputation(player.getUUID(), this.deity.getId()) < minDevotion;
        }
    }
}
