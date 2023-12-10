package aster.amo.erosianmagic.bard.song;

import net.minecraft.nbt.CompoundTag;

public record Note(int index, Interval interval, Song.Pitch pitch) {
    public static Note fromNbt(CompoundTag noteTag) {
        return new Note(
                noteTag.getInt("index"),
                new Interval(noteTag.getFloat("interval")),
                Song.Pitch.fromStep(noteTag.getInt("pitch"))
        );
    }

    public boolean isInTime(float time, float fudge){
        return interval.isInTime(time, fudge);
    }

    public boolean isInPerfectTime(float time, float fudge){
        return interval.isInPerfectTime(time, fudge);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("index", index);
        tag.putFloat("interval", interval.time());
        tag.putFloat("pitch", pitch.getStep());
        return tag;
    }
}
