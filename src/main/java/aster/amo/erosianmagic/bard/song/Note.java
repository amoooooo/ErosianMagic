package aster.amo.erosianmagic.bard.song;

import net.minecraft.nbt.CompoundTag;

public record Note(int index, Interval interval) {
    public static Note fromNbt(CompoundTag noteTag) {
        return new Note(
                noteTag.getInt("index"),
                new Interval(noteTag.getFloat("interval"))
        );
    }

    public boolean isInTime(float time, float fudge){
        return interval.isInTime(time, fudge);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("index", index);
        tag.putFloat("interval", interval.time());
        return tag;
    }
}
