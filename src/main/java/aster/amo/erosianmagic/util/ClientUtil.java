package aster.amo.erosianmagic.util;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientUtil {
    public static void animatePlayerStart(Player player, ResourceLocation resourceLocation) {
        KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(resourceLocation);
        if (keyframeAnimation != null) {
            ModifierLayer<IAnimation> animation = (ModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer)player).get(SpellAnimations.ANIMATION_RESOURCE);
            if (animation != null) {
                KeyframeAnimationPlayer castingAnimationPlayer = new KeyframeAnimationPlayer(keyframeAnimation);
                ClientMagicData.castingAnimationPlayerLookup.put(player.getUUID(), castingAnimationPlayer);
                Boolean armsFlag = (Boolean) ClientConfigs.SHOW_FIRST_PERSON_ARMS.get();
                Boolean itemsFlag = (Boolean)ClientConfigs.SHOW_FIRST_PERSON_ITEMS.get();
                if (!armsFlag && !itemsFlag) {
                    castingAnimationPlayer.setFirstPersonMode(FirstPersonMode.DISABLED);
                } else {
                    castingAnimationPlayer.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    castingAnimationPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(armsFlag, armsFlag, itemsFlag, itemsFlag));
                }

                animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE), castingAnimationPlayer, true);
            }
        }

    }
}
