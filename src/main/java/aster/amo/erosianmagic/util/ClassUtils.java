package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.divine.IDivine;
import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.fighter.barbarian.IBarbarian;
import aster.amo.erosianmagic.fighter.champion.IChampion;
import aster.amo.erosianmagic.fighter.paladin.IPaladin;
import aster.amo.erosianmagic.mage.IMage;
import aster.amo.erosianmagic.mage.bard.IBard;
import aster.amo.erosianmagic.divine.cleric.ICleric;
import aster.amo.erosianmagic.mage.machinist.IMachinist;
import aster.amo.erosianmagic.divine.witch.IWitch;
import aster.amo.erosianmagic.mage.wizard.IWizard;
import aster.amo.erosianmagic.rogue.IRogue;
import aster.amo.erosianmagic.rogue.charlatan.ICharlatan;
import aster.amo.erosianmagic.rogue.monk.IMonk;
import aster.amo.erosianmagic.rogue.ranger.IRanger;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.info.Limitations;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClassUtils {
    public static Map<String, Capability<? extends IClass>> CLASSES = new HashMap<>();

    static {
        CLASSES.put("Fighter", IFighter.INSTANCE);
        CLASSES.put("Barbarian", IBarbarian.INSTANCE);
        CLASSES.put("Champion", IChampion.INSTANCE);
        CLASSES.put("Paladin", IPaladin.INSTANCE);
        CLASSES.put("Divine", IDivine.INSTANCE);
        CLASSES.put("Rogue", IRogue.INSTANCE);
        CLASSES.put("Mage", IMage.INSTANCE);
        CLASSES.put("Witch", IWitch.INSTANCE);
        CLASSES.put("Machinist", IMachinist.INSTANCE);
        CLASSES.put("Bard", IBard.INSTANCE);
        CLASSES.put("Cleric", ICleric.INSTANCE);
        CLASSES.put("Wizard", IWizard.INSTANCE);
        CLASSES.put("Charlatan", ICharlatan.INSTANCE);
        CLASSES.put("Monk", IMonk.INSTANCE);
        CLASSES.put("Ranger", IRanger.INSTANCE);
    }

    public static List<Class<? extends IClass>> BASE_CLASSES = List.of(
            IFighter.class,
            IMage.class,
            IDivine.class,
            IRogue.class
    );

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

    public static String getChosenClassName(Player player){
        if(player.level().isClientSide) return ClientClassUtils.getChosenClass();
        IClass clazz = getChosenClass(player);
        if(clazz == null) return "";
        return clazz.getClassName();
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
