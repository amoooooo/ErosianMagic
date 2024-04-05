package aster.amo.erosianmagic.divine.witch.eidolon.spells;

import aster.amo.erosianmagic.divine.witch.IWitch;
import aster.amo.erosianmagic.divine.witch.coven.Coven;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.IClass;
import elucent.eidolon.api.deity.Deity;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
import elucent.eidolon.common.deity.Deities;
import elucent.eidolon.common.spell.StaticSpell;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SoulUpdatePacket;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;

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
        List<Player> witches = players.stream().filter(p -> !Objects.equals(ClassUtils.getChosenClass(p).getClassName(), ((IClass) IWitch.INSTANCE).getClassName())).toList();
        if(!witches.isEmpty()) {
            boolean recruiting = witches.stream().noneMatch(p -> Objects.requireNonNull(p.getCapability(IWitch.INSTANCE).resolve().get()).getCoven().isRecruiting());
            if(recruiting) {
                Coven coven = Objects.requireNonNull(player.getCapability(IWitch.INSTANCE).resolve().get()).getCoven();
                coven.setRecruiting(false);
                coven.addMember(player.getUUID());
                return;
            }
        }
        IWitch witch = Objects.requireNonNull(player.getCapability(IWitch.INSTANCE).resolve().get());
        Coven coven = witch.getCoven();
        coven.setRecruiting(true);
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
