package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.bard.IBard;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClassUtils {
    public static boolean isBard(Player player) {
        AtomicBoolean isBard = new AtomicBoolean(false);
        player.getCapability(IBard.INSTANCE).ifPresent(bard -> {
            player.sendSystemMessage(Component.literal("Is Bard?: " + bard.isChosenClass()));
            isBard.set(bard.isChosenClass());
        });
        return isBard.get();
    }
}
