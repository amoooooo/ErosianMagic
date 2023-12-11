package aster.amo.erosianmagic.cleric.chapel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class Worshipper implements IWorshipper, INBTSerializable<CompoundTag> {
    private UUID leader;
    private boolean canListen = true;
    Vec3 beforePosition;
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
    public void tick(Level level) {
        if (level.getGameTime() % 20 == 0) {
            if(!canListen) {
                if(level.random.nextFloat() < 0.05) {
                    canListen = true;
                }
            }
        }
    }

    @Override
    public Vec3 getBeforePosition() {
        return beforePosition;
    }

    @Override
    public void setBeforePosition(Vec3 beforePosition) {
        this.beforePosition = beforePosition;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if(leader != null ) nbt.putUUID("leader", leader);
        nbt.putBoolean("canListen", canListen);
        if(beforePosition != null){
            nbt.putFloat("beforeX", (float) beforePosition.x);
            nbt.putFloat("beforeY", (float) beforePosition.y);
            nbt.putFloat("beforeZ", (float) beforePosition.z);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.hasUUID("leader")) leader = nbt.getUUID("leader");
        canListen = nbt.getBoolean("canListen");
        if(nbt.contains("beforeX"))
            beforePosition = new Vec3(nbt.getFloat("beforeX"), nbt.getFloat("beforeY"), nbt.getFloat("beforeZ"));
    }
}
