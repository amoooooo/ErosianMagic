package aster.amo.erosianmagic.util;

import net.minecraft.world.entity.player.Player;

public interface IClass {
    void setChosenClass(boolean isClass);
    boolean isChosenClass();
    String getClassName();
    void sync(Player player);
}
