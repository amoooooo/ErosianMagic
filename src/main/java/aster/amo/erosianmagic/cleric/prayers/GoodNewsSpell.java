package aster.amo.erosianmagic.cleric.prayers;

import aster.amo.erosianmagic.cleric.ICleric;
import aster.amo.erosianmagic.cleric.chapel.Chapel;
import aster.amo.erosianmagic.cleric.chapel.IWorshipper;
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

public class GoodNewsSpell extends StaticSpell {
    final Deity deity;

    public GoodNewsSpell(ResourceLocation name, Deity deity, Sign... signs) {
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
            double devotion = this.getDevotion(level, player);
            // check if 1 block in each direction is the player's chapel (lectern)
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if(!cleric.isChosenClass()) {
                    player.sendSystemMessage(Component.literal("You are not a cleric."));
                    return;
                }
                if(cleric.hasChapel()){
                    if(cleric.getChapel().isSermon()){
                        player.sendSystemMessage(Component.literal("You are ending the sermon."));
                        cleric.getChapel().setChapelProgress(-1);
                    } else {
                        BlockPos mutableBlockPos = player.blockPosition();
                        for (Direction direction : Direction.values()) {
                            mutableBlockPos = player.blockPosition().relative(direction);
                            boolean isLectern = level.getBlockState(mutableBlockPos).getBlock() instanceof LecternBlock;
                            boolean isSameDimension = level.dimension().equals(cleric.getChapel().getDimension());
                            if (isLectern && isSameDimension && mutableBlockPos.equals(cleric.getChapel().getLecternPos())) {
                                player.sendSystemMessage(Component.literal("You are starting a sermon."));
                                if (level.getBlockState(mutableBlockPos).getBlock() instanceof LecternBlock) {
                                    Chapel chapel = cleric.getChapel();
                                    if(chapel.validateChapel(level)){
                                        player.sendSystemMessage(Component.literal("You are starting a sermon. Validated."));
                                        chapel.setSermon(true);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                    for (Direction direction : Direction.values()) {
                        mutableBlockPos.set(blockPos).move(direction);
                        if (level.getBlockState(mutableBlockPos).getBlock() instanceof LecternBlock) {
                            Chapel chapel = new Chapel(level.dimension(), mutableBlockPos, new ArrayList<>(), 1);
                            if(chapel.validateChapel(level)){
                                cleric.setChapel(chapel);
                            }
                        }
                    }
                }
                if (devotion > 0.0D) {
                    List<Villager> villagers = level.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(Math.max(5, devotion / 5)), villager -> {
                        AtomicReference<Boolean> isWorshipper = new AtomicReference<>(false);
                        villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                            isWorshipper.set(worshipper.getLeader() == null && worshipper.canListen());
                        });
                        return isWorshipper.get();
                    });
                    villagers.forEach(villager -> {
                        if (level.random.nextFloat() < 0.5) {
                            villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                                worshipper.setLeader(player.getUUID());
                                ((ServerLevel)level).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), villager.getX(), villager.getY() + villager.getBbHeight(), villager.getZ(), 10, 0.25, 0.5, 0.25, 0.1);
                                player.sendSystemMessage(Component.literal("Your temple has a new follower."));
                            });
                            cleric.addWorshipper(villager.getUUID());
                        }
                    });
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
