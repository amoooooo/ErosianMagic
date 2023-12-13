package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.net.ClientboundSyncMultiTargetingData;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.spellsnspellbooks.data.CastMultiTargetingData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TargetingHelper {
    public TargetingHelper() {
    }

    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, float range, float aimAssist, boolean sendFailureMessage, float radius) {
        var target = Utils.raycastForEntity(caster.level(), caster, range, true, aimAssist);
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            // get entities in radius of target
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, livingTarget.getBoundingBox().inflate(radius), entity -> {
                return !Utils.shouldHealEntity(caster, entity);
            });
            entities.remove(livingTarget);
            playerMagicData.setAdditionalCastData(new CastMultiTargetingData(entities, livingTarget));
            if (caster instanceof ServerPlayer serverPlayer) {
                List<UUID> targets = new ArrayList<>(entities.stream().map(LivingEntity::getUUID).toList());
                targets.add(livingTarget.getUUID());
                Networking.sendTo(serverPlayer, new ClientboundSyncMultiTargetingData(targets, spell.getSpellId()));
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", livingTarget.getDisplayName().getString(), spell.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
            }
            if (livingTarget instanceof ServerPlayer serverPlayer) {
                Utils.sendTargetedNotification(serverPlayer, caster, spell);
            }
            return true;
        } else if (sendFailureMessage && caster instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.cast_error_target").withStyle(ChatFormatting.RED)));
        }
        return false;
    }

    public static boolean preCastTargetHelperFriendly(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, float range, float aimAssist, boolean sendFailureMessage, float radius) {
        var target = Utils.raycastForEntity(caster.level(), caster, range, true, aimAssist);
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            if(!Utils.shouldHealEntity(caster, livingTarget)) return false;
                // get entities in radius of target
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, livingTarget.getBoundingBox().inflate(radius), entity -> {
                return Utils.shouldHealEntity(caster, entity);
            });
            entities.remove(livingTarget);
            playerMagicData.setAdditionalCastData(new CastMultiTargetingData(entities, livingTarget));
            if (caster instanceof ServerPlayer serverPlayer) {
                List<UUID> targets = new ArrayList<>(entities.stream().map(LivingEntity::getUUID).toList());
                targets.add(livingTarget.getUUID());
                Networking.sendTo(serverPlayer, new ClientboundSyncMultiTargetingData(targets, spell.getSpellId()));
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", livingTarget.getDisplayName().getString(), spell.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
            }
            if (livingTarget instanceof ServerPlayer serverPlayer) {
                Utils.sendTargetedNotification(serverPlayer, caster, spell);
            }
            return true;
        } else if (target instanceof BlockHitResult blockHitResult) {
            BlockPos pos = blockHitResult.getBlockPos();
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(radius), entity -> Utils.shouldHealEntity(caster, entity));
            if(entities.size() == 0) return false;
            LivingEntity mainTarget = entities.stream().min((entity1, entity2) -> {
                return (int) (entity1.distanceToSqr(pos.getCenter()) - entity2.distanceToSqr(pos.getCenter()));
            }).orElse(null);
            entities.remove(mainTarget);
            playerMagicData.setAdditionalCastData(new CastMultiTargetingData(entities, mainTarget));
            if (caster instanceof ServerPlayer serverPlayer) {
                List<UUID> targets = new ArrayList<>(entities.stream().map(LivingEntity::getUUID).toList());
                Networking.sendTo(serverPlayer, new ClientboundSyncMultiTargetingData(targets, spell.getSpellId()));
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", "Self", spell.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
            }
            return true;
        } else if (sendFailureMessage && caster instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.cast_error_target").withStyle(ChatFormatting.RED)));
        }
        return false;
    }
}