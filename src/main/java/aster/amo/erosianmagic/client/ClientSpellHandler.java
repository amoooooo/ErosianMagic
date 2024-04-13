package aster.amo.erosianmagic.client;

import elucent.eidolon.api.deity.Deity;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3dc;

public class ClientSpellHandler {
    public static void spiritGuardians(LivingEntity entity, Deity deity) {
    }

    public static Player getPlayer(SpellSelectionManager.SpellSelectionEvent event){
        if(event.getEntity() == null || event.getEntity().level().isClientSide()){
            return Minecraft.getInstance().player;
        }
        return event.getEntity();
    }
}
