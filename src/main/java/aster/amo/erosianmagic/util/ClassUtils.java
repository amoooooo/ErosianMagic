package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.fighter.barbarian.IBarbarian;
import aster.amo.erosianmagic.mage.IMage;
import aster.amo.erosianmagic.mage.bard.IBard;
import aster.amo.erosianmagic.divine.cleric.ICleric;
import aster.amo.erosianmagic.mage.machinist.IMachinist;
import aster.amo.erosianmagic.divine.witch.IWitch;
import aster.amo.erosianmagic.rogue.IRogue;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.info.Limitations;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClassUtils {
    public static Map<String, Capability<? extends IClass>> CLASSES = Map.of(
            "Machinist", IMachinist.INSTANCE,
            "Bard", IBard.INSTANCE,
            "Cleric", ICleric.INSTANCE,
            "Witch", IWitch.INSTANCE,
            "Mage", IMage.INSTANCE,
            "Rogue", IRogue.INSTANCE,
            "Barbarian", IBarbarian.INSTANCE,
            "Fighter", IFighter.INSTANCE
    );
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

    public static IClass getChosenClass(Player player) {
        for(Capability<? extends IClass> capability : CLASSES.values()) {
            AtomicBoolean isChosenClass = new AtomicBoolean(false);
            player.getCapability(capability).ifPresent(clazz -> {
                isChosenClass.set(clazz.isChosenClass());
            });
            if(isChosenClass.get()) {
                return player.getCapability(capability).orElse(null);
            }
        }
        return null;
    }

    public static void disableParcool(ServerPlayer player) {
        Limitations.Changer.get(player).setEnabled(true);
        ActionList.ACTIONS.forEach(action -> {
            Limitations.Changer.get(player).setPossibilityOf(action, false);
        });
        Limitations.Changer.get(player).sync();
    }

    public static void enableParcool(ServerPlayer player) {
        Limitations.Changer.get(player).setEnabled(false);
        ActionList.ACTIONS.forEach(action -> {
            Limitations.Changer.get(player).setPossibilityOf(action, true);
        });
        Limitations.Changer.get(player).sync();
    }
}
