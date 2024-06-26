package aster.amo.erosianmagic.divine.witch.eidolon.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import elucent.eidolon.api.altar.AltarInfo;
import elucent.eidolon.api.deity.Deity;
import elucent.eidolon.api.spells.Sign;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.capability.ISoul;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CastingSpell extends StaticSpell {
    final Deity deity;
    RegistryObject<AbstractSpell> spell;
    int castTime;
    float costModifier;
    public CastingSpell(ResourceLocation name, Deity deity, RegistryObject<AbstractSpell> spell, int castTime, Sign... signs) {
        super(name, signs);
        this.deity = deity;
        this.spell = spell;
        this.castTime = castTime;
        this.costModifier = 1.0f;
    }

    public CastingSpell(ResourceLocation name, Deity deity, RegistryObject<AbstractSpell> spell, int castTime, float costModifier, Sign... signs) {
        super(name, signs);
        this.deity = deity;
        this.spell = spell;
        this.castTime = castTime;
        this.costModifier = costModifier;
    }

    @Override
    public boolean canCast(Level level, BlockPos blockPos, Player player) {
        return !this.reputationCheck(level, player, 0.0D);
    }

    @Override
    public int getDelay() {
        return 5;
    }

    @Override
    public void cast(Level level, BlockPos blockPos, Player player) {
        if(spell != null) {
            if(!level.isClientSide) {
                player.getCapability(ISoul.INSTANCE, (Direction)null).ifPresent((soul) -> {
                    float cost = (float) (1.0f/spell.get().getMaxLevel()) * 10.0f * signs.toArray().length * costModifier;
                    if(!soul.hasMagic() || soul.getMagic() < cost) return;
                    int spellLevel = getLevel(level, player, 1, this.spell.get());
                    player.sendSystemMessage(Component.literal("Attempting to cast:" + this.spell.getId().toString()));
                    this.spell.get().attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, level, (ServerPlayer) player, CastSource.SPELLBOOK, false, "custom");
                    level.getCapability(IReputation.INSTANCE, (Direction)null).ifPresent((rep) -> {
                        rep.pray(player, this.getRegistryName(), level.getGameTime());
                        rep.addReputation(player, this.deity.getId(), 0.1 + 0.25 * spellLevel);
                    });
                    if(cost <= 0.0) return;
                    soul.takeMagic(cost);
                    Networking.sendTo((ServerPlayer) player, new SoulUpdatePacket(player));
                });
            }
        }
    }

    protected int getLevel(Level level, Player player, int minLevel, AbstractSpell spell) {
        AttributeInstance songPower = player.getAttribute(AttributeRegistry.CHANT_POWER.get());
        if(songPower != null){
            int spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
            return spellLevel;
        }
        return (int)Math.ceil(getDevotion(level, player) % spell.getMaxLevel()) + minLevel;
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
