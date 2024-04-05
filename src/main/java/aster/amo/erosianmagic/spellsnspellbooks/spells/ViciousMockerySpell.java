package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.net.ClientboundInsultPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.spellsnspellbooks.spells.entity.FaerieFireAoe;
import aster.amo.erosianmagic.util.TargetingHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.CastTargetingData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

import static io.redspace.ironsspellbooks.api.util.Utils.preCastTargetHelper;

@AutoSpellConfig
public class ViciousMockerySpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "vicious_mockery");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)));
    }
    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) * 2f;
    }
    public ViciousMockerySpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 2;
        this.castTime = 2;
        this.baseManaCost = 5;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.HOLY_CAST.get());
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f);
    }
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof CastTargetingData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null) {
                DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(entity));
                for(int i = 0; i < spellLevel * 5; i++) {
                    ((ServerLevel) level).sendParticles(ParticleRegistry.DRAGON_FIRE_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 3, 0.1, 0.25, 0.1, 0.1);
                    ((ServerLevel) level).sendParticles(ParticleRegistry.UNSTABLE_ENDER_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 1, 0.1, 0.25, 0.1, 0.1);
                }
                if(level.getRandom().nextFloat() <= 0.25){
                    if(entity instanceof Player pl) {
                        Networking.sendTo(pl, new ClientboundInsultPacket(INSULTS.get(level.getRandom().nextInt(INSULTS.size()))));
                    }
                }
            }
        }
        super.onCast(level, spellLevel, entity, source, playerMagicData);
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(2)
            .build();
    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }


    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_INSTANT_CAST;
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

    private static List<String> INSULTS = List.of(
            "Darling, your charisma is as lackluster as a rusty dagger in a dragon's den.",
            "Oh sweetheart, you possess the charm of a kobold on a rainy day, utterly drenched in dreariness.",
            "My dear, your company is as enchanting as a cursed scroll: draining every drop of delight from the room.",
            "Bless your heart, but your personality is about as captivating as a rusted chain, shackling all who dare engage.",
            "Oh darling, your presence is as charming as a mimic's disguise, fooling none but the most naive.",
            "Sweetie, your aura is as radiant as a troglodyte's lair at midnight, cloaked in darkness.",
            "My dear, your wit is as sharp as a gelatinous cube's edges, slicing through conversation with transparent ease.",
            "Oh darling, your sense of humor is as lively as a skeleton's dance, bones rattling with forced mirth.",
            "Honey, your conversation skills are as riveting as a goblin's lullaby, putting even the most alert to sleep.",
            "Oh sweetie, your company is like a cursed artifact: a black hole of joy, sucking all merriment into oblivion.",
            "Darling, your aura is as vibrant as a banshee's wail, piercing the ears and rending the soul.",
            "My dear, your presence is as captivating as a zombie's moan, sending shivers down the spine.",
            "Bless your heart, but your personality is about as thrilling as a rusted trapdoor, creaking with every step.",
            "Oh darling, your wit is about as engaging as a rusted lock, stubbornly resisting every attempt at charm.",
            "Sweetie, your laughter is as infectious as a ghoul's cough, spreading discomfort far and wide.",
            "My dear, your sense of style is as fashionable as a zombie's wardrobe, draped in tattered rags.",
            "Oh darling, your scent is about as inviting as a troll's den, reeking of decay and despair.",
            "Honey, your manners are as refined as a troll's table etiquette, lacking even the slightest hint of grace.",
            "Sweetie, your presence is as captivating as a blank tome in a wizard's library, utterly devoid of intrigue.",
            "Oh darling, your charm is as elusive as a rust monster in a blacksmith's forge, leaving naught but disappointment in its wake.",
            "My dear, your conversation skills are as enthralling as a kobold's cave painting, leaving much to be desired.",
            "Bless your heart, but your company is as appealing as a trek through the Abyss, fraught with danger and despair.",
            "Oh sweetie, your personality is as enchanting as a cursed amulet, casting a shadow over all who come near.",
            "Darling, your aura is as vibrant as a troglodyte's hide, exuding a pungent odor of mediocrity.",
            "My dear, your company is as alluring as a dragon's hoard, filled with treasures of tedium and banality.",
            "Oh darling, your wit is as sharp as a spoon in a bard's kit, dulling even the most vibrant of conversations.",
            "Sweetie, your laughter is as dull as a dagger in a rogue's pocket, lacking the sparkle of genuine amusement.",
            "My dear, your charm is about as captivating as a kobold's nest, cluttered with disappointment and despair.",
            "Bless your heart, but your presence is as thrilling as a rusted sword, barely capable of drawing a yawn.",
            "Oh darling, your personality is as lively as a tavern without ale, devoid of the intoxicating spirit of adventure."
    );
}
