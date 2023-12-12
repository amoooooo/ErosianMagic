package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.bard.IBard;
import aster.amo.erosianmagic.cleric.ICleric;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClassUtils {
    public static boolean isBard(Player player) {
        AtomicBoolean isBard = new AtomicBoolean(false);
        player.getCapability(IBard.INSTANCE).ifPresent(bard -> {
            player.sendSystemMessage(Component.nullToEmpty("isChosenClass: " + bard.isChosenClass()));
            isBard.set(bard.isChosenClass());
        });
        return isBard.get();
    }

    public static boolean isCleric(Player player) {
        AtomicBoolean isCleric = new AtomicBoolean(false);
        player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
            player.sendSystemMessage(Component.nullToEmpty("isChosenClass: " + cleric.isChosenClass()));
            isCleric.set(cleric.isChosenClass());
        });
        return isCleric.get();
    }
}
