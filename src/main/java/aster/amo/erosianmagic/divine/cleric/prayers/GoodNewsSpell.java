package aster.amo.erosianmagic.divine.cleric.prayers;

import aster.amo.erosianmagic.divine.cleric.ICleric;
import aster.amo.erosianmagic.divine.cleric.chapel.Temple;
import aster.amo.erosianmagic.divine.cleric.chapel.IWorshipper;
import elucent.eidolon.api.deity.Deity;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.common.spell.StaticSpell;
import forge.net.mca.entity.VillagerEntityMCA;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

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
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if(!cleric.isChosenClass()) {
                    player.sendSystemMessage(Component.literal("You are not a cleric."));
                    return;
                }
                if(cleric.hasTemple()){
                    if(cleric.getTemple().isSermon()){
                        player.sendSystemMessage(Component.literal("You are ending the sermon."));
                        cleric.getTemple().setTempleProgress(-1);
                        cleric.getTemple().setSermon(false);
                    } else {
                        LazyOptional<IReputation> iReputationLazyOptional = level.getCapability(IReputation.INSTANCE);
                        if (iReputationLazyOptional.resolve().isEmpty()) return;
                        IReputation iReputation = iReputationLazyOptional.resolve().get();
                        if (!iReputation.canPray(player, getRegistryName(), level.getGameTime())) {
                            player.displayClientMessage(Component.translatable("eidolon.message.prayer_cooldown"), true);
                            return;
                        }
                        BlockPos mutableBlockPos = player.blockPosition();
                        for (Direction direction : Direction.values()) {
                            mutableBlockPos = player.blockPosition().relative(direction);
                            boolean isLectern = ForgeRegistries.BLOCKS.getKey(level.getBlockState(mutableBlockPos).getBlock()).getPath().contains("lectern");
                            boolean isSameDimension = level.dimension().equals(cleric.getTemple().getDimension());
                            if (isLectern && isSameDimension && mutableBlockPos.equals(cleric.getTemple().getLecternPos())) {
                                if (level.getBlockState(mutableBlockPos).getBlock() instanceof LecternBlock) {
                                    Temple temple = cleric.getTemple();
                                    if(temple.validateTemple(level, player)){
                                        player.sendSystemMessage(Component.literal("You are starting a sermon."));
                                        temple.setSermon(true);
                                        return;
                                    }
                                }
                            } else {
                                if(!isSameDimension){
                                    player.sendSystemMessage(Component.literal("You are not in the same dimension as your temple."));
                                }
                            }
                        }
                    }
                } else {
                    player.sendSystemMessage(Component.literal("You do not have a temple."));
                    return;
                }
                if (devotion > 0.0D) {
                    List<VillagerEntityMCA> villagers = level.getEntities(null, player.getBoundingBox().inflate(Math.max(5, devotion / 5))).stream()
                            .filter(entity -> entity instanceof VillagerEntityMCA)
                            .map(entity -> (VillagerEntityMCA) entity)
                                    .filter((villager) -> {
                                        AtomicReference<Boolean> isWorshipper = new AtomicReference<>(false);
                                        villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                                            isWorshipper.set(worshipper.getLeader() == null && worshipper.canListen());
                                        });
                                        return isWorshipper.get();
                                    }).toList();

                    villagers.forEach(villager -> {
                        if (level.random.nextFloat() < 0.5) {
                            villager.getCapability(IWorshipper.INSTANCE).ifPresent(worshipper -> {
                                worshipper.setLeader(player.getUUID());
                                worshipper.setLastSermon(level.getGameTime());
                                worshipper.setLoseFaithChance(0.0f);
                                ((ServerLevel)level).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), villager.getX(), villager.getY() + villager.getBbHeight(), villager.getZ(), 10, 0.25, 0.5, 0.25, 0.1);
                                player.sendSystemMessage(Component.literal("Your temple has a new follower."));
                            });
                            cleric.addWorshipper(villager.getUUID());
                        } else {
                            villager.playSound(SoundEvents.VILLAGER_NO, 1.0f, 0.9f + level.random.nextFloat() * 0.2f);
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
