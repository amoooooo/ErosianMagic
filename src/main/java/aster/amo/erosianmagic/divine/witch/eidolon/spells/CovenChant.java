package aster.amo.erosianmagic.divine.witch.eidolon.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.divine.witch.IWitch;
import aster.amo.erosianmagic.divine.witch.coven.Coven;
import aster.amo.erosianmagic.divine.witch.coven.Dedicant;
import aster.amo.erosianmagic.divine.witch.coven.IDedicant;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.IClass;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import elucent.eidolon.api.deity.Deity;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.common.spell.StaticSpell;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SoulUpdatePacket;
import elucent.eidolon.recipe.ChantRecipe;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CovenChant extends StaticSpell {
    public CovenChant(ResourceLocation name, Sign... signs) {
        super(name, signs);
    }

    @Override
    public boolean canCast(Level level, BlockPos blockPos, Player player) {
        return !this.reputationCheck(level, player, 0.0D);
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void cast(Level level, BlockPos blockPos, Player player) {
        if(level.isClientSide) return;
        List<Player> players = level.getNearbyPlayers(TargetingConditions.forNonCombat(), player, player.getBoundingBox().inflate(12.0D));
        List<Player> playerWitches = players.stream().filter(p -> !Objects.equals(ClassUtils.getChosenClassName(p), ((IClass) IWitch.INSTANCE).getClassName())).toList();
        if(!playerWitches.isEmpty()) {
            boolean recruiting = playerWitches.stream().noneMatch(p -> Objects.requireNonNull(p.getCapability(IWitch.INSTANCE).resolve().get()).getCoven().isRecruiting());
            if(recruiting) {
                Coven coven = Objects.requireNonNull(player.getCapability(IWitch.INSTANCE).resolve().get()).getCoven();
                coven.setRecruiting(false);
                coven.addMember(player.getUUID());
                return;
            } else {
                // add the casting player to the first coven that is recruiting
                playerWitches.stream().filter(p -> Objects.requireNonNull(p.getCapability(IWitch.INSTANCE).resolve().get()).getCoven().isRecruiting()).findFirst().ifPresent(p -> {
                    Coven otherCoven = Objects.requireNonNull(p.getCapability(IWitch.INSTANCE).resolve().get()).getCoven();
                    otherCoven.addMember(player.getUUID());
                    player.getCapability(IWitch.INSTANCE).resolve().get().setCoven(otherCoven);
                });
            }
        }
        IWitch witch = Objects.requireNonNull(player.getCapability(IWitch.INSTANCE).resolve().get());
        Coven coven = witch.getCoven();
        coven.setRecruiting(true);
        double devotion = getDevotion(level, player);
        if(devotion > 0.0D) {
            List<Witch> witches = level.getEntities(null, player.getBoundingBox().inflate(12.0D)).stream()
                    .filter(entity -> entity instanceof Witch)
                    .map(entity -> (Witch) entity)
                    .filter((w) -> {
                        AtomicReference<Boolean> isWitch = new AtomicReference<>(false);
                        w.getCapability(IDedicant.INSTANCE).ifPresent(dedicant -> {
                            if(dedicant.canListen()) {
                                isWitch.set(true);
                            } else {
                                isWitch.set(false);
                            }
                        });
                        return isWitch.get();
                    }).toList();

            witches.forEach(w -> {
                if(level.random.nextFloat() < 0.25) {
                    w.getCapability(IDedicant.INSTANCE).ifPresent(dedicant -> {
                        dedicant.setLeader(player.getUUID());
                        dedicant.setCanListen(false);
                        player.sendSystemMessage(Component.literal("Your coven has a new member."));
                        coven.addDedicant((Dedicant) dedicant);
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
            return iReputation.getReputation(player.getUUID(), Deities.DARK_DEITY.getId());
        }
    }
    protected boolean reputationCheck(Level world, Player player, double minDevotion) {
        LazyOptional<IReputation> iReputationLazyOptional = world.getCapability(IReputation.INSTANCE);
        if (iReputationLazyOptional.resolve().isEmpty()) {
            return true;
        } else {
            IReputation iReputation = (IReputation)iReputationLazyOptional.resolve().get();
            return iReputation.getReputation(player.getUUID(), Deities.DARK_DEITY.getId()) < minDevotion;
        }
    }

}
