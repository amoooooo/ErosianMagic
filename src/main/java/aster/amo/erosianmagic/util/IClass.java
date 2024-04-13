package aster.amo.erosianmagic.util;

import net.minecraft.world.entity.player.Player;

public interface IClass {
    void setChosenClass(boolean isClass, Player player);
    boolean isChosenClass();
    String getClassName();
    void sync(Player player);
    default void onSetOtherClass(Player player){}
    default void onSetClass(Player player){}
    int getLevel();
    void setLevel(int level);
}
