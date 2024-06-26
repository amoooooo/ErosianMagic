package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.mobeffect.RageEffect;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.CastTargetingData;
import io.redspace.ironsspellbooks.network.spell.ClientboundSyncTargetingData;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.Log;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RageSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "rage");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage_reduction", Utils.stringTruncation(getSpellPower(spellLevel, caster), 0))
        );
    }

    public RageSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 5;
        this.baseManaCost = 0;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.RAVAGER_ROAR);
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f);
    }

    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist) {
        var target = Utils.raycastForEntity(caster.level(), caster, range, true, aimAssist);
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            playerMagicData.setAdditionalCastData(new CastTargetingData(livingTarget));
            if (caster instanceof ServerPlayer serverPlayer) {
                Messages.sendToPlayer(new ClientboundSyncTargetingData(livingTarget, spell), serverPlayer);
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", livingTarget.getDisplayName().getString(), spell.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
            }
            if (livingTarget instanceof ServerPlayer serverPlayer) {
                Utils.sendTargetedNotification(serverPlayer, caster, spell);
            }
            return true;
        } else {
            if (caster instanceof ServerPlayer serverPlayer) {
                playerMagicData.setAdditionalCastData(new CastTargetingData(serverPlayer));
                Messages.sendToPlayer(new ClientboundSyncTargetingData(serverPlayer, spell), serverPlayer);
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", serverPlayer.getDisplayName().getString(), spell.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
            }
            return caster instanceof ServerPlayer;
        }
    }
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.RAGE.get(), RageEffect.RAGE_DURATION[Math.min(RageEffect.RAGE_DURATION.length, spellLevel)], spellLevel));
        super.onCast(level, spellLevel, entity, source, playerMagicData);
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(60)
            .build();
    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }


    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }

    @Nullable
    private LivingEntity findTarget(LivingEntity caster) {
        var target = Utils.raycastForEntity(caster.level(), caster, 32, true, 0.35f);
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            return livingTarget;
        } else {
            return null;
        }
    }
}
