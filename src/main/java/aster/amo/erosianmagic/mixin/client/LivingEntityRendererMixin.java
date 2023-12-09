package aster.amo.erosianmagic.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void renderInvisPlayer(T entity, boolean p_115323_, boolean p_115324_, boolean p_115325_, CallbackInfoReturnable<RenderType> cir){
        if (entity == Minecraft.getInstance().player && entity.hasEffect(MobEffects.INVISIBILITY)) {
            ResourceLocation resourcelocation = ((LivingEntityRenderer<T, M>)(Object)this).getTextureLocation(entity);
            cir.setReturnValue(RenderType.itemEntityTranslucentCull(resourcelocation));
        }
    }

    @Inject(method = "isBodyVisible", at = @At("HEAD"), cancellable = true)
    private void isBodyVisible(T entity, CallbackInfoReturnable<Boolean> cir){
        if (entity == Minecraft.getInstance().player && entity.hasEffect(MobEffects.INVISIBILITY)) {
            cir.setReturnValue(false);
        }
    }
}
