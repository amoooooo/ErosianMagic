package aster.amo.erosianmagic.cleric.prayers;

import aster.amo.erosianmagic.cleric.ICleric;
import aster.amo.erosianmagic.cleric.chapel.IWorshipper;
import aster.amo.erosianmagic.cleric.chapel.Temple;
import elucent.eidolon.api.deity.Deity;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.common.spell.StaticSpell;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class HallowSpell extends StaticSpell {
    final Deity deity;

    public HallowSpell(ResourceLocation name, Deity deity, Sign... signs) {
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
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if(!cleric.isChosenClass()) {
                    player.sendSystemMessage(Component.literal("You are not a cleric."));
                    return;
                }
                BlockPos mutableBlockPos = player.blockPosition();
                for (Direction direction : Direction.values()) {
                    mutableBlockPos = player.blockPosition().relative(direction);
                    if (level.getBlockState(mutableBlockPos).getBlock() instanceof LecternBlock) {
                        Temple temple = new Temple(level.dimension(), mutableBlockPos, new ArrayList<>(), 1);
                        if(cleric.hasTemple()) {
                            temple.setWorshippers(cleric.getTemple().getWorshippers());
                            temple.setTempleLevel(cleric.getTemple().getTempleLevel());
                        }
                        if(temple.validateTemple(level, player)){
                            cleric.setTemple(temple);
                        }
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
