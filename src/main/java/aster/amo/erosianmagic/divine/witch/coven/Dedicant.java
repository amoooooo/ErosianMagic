package aster.amo.erosianmagic.divine.witch.coven;

import aster.amo.erosianmagic.divine.witch.Witch;
import aster.amo.erosianmagic.util.ClassUtils;
import aster.amo.erosianmagic.util.IClass;
import elucent.eidolon.capability.ISoul;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class Dedicant implements IDedicant, INBTSerializable<CompoundTag> {
    private UUID leader;
    private boolean canListen = true;
    @Override
    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    @Override
    public UUID getLeader() {
        return leader;
    }

    @Override
    public boolean canListen() {
        return canListen;
    }

    @Override
    public void setCanListen(boolean canListen) {
        this.canListen = canListen;
    }

    @Override
    public void tick(ServerPlayer player) {
        if(player.level().random.nextFloat() > 0.01) return;
        player.getCapability(ISoul.INSTANCE).ifPresent(soul -> {
            IClass clazz = ClassUtils.getChosenClass(player);
            if(clazz == null) return;
            if(clazz.getClassName().equals("Witch")) {
                Witch witch = (Witch) clazz;
                int covenLevel = witch.getCoven().getCovenLevel();
                soul.giveMagic(soul.getMagic() + (int) (soul.getMaxMagic() * 0.02 * covenLevel));
            }
        });
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if(leader != null) nbt.putUUID("leader", leader);
        nbt.putBoolean("canListen", canListen);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.hasUUID("leader")) leader = nbt.getUUID("leader");
        canListen = nbt.getBoolean("canListen");
    }
}
