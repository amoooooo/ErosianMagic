package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.bard.IBard;
import aster.amo.erosianmagic.cleric.ICleric;
import aster.amo.erosianmagic.client.ErosianMagicClient;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientClassUtils {
    public static boolean isOneOfClasses(String... classes) {
        return Arrays.stream(classes).anyMatch(r -> r.equals(ErosianMagicClient.CLASS));
    }
}
