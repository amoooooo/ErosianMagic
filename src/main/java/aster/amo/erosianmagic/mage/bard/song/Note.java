package aster.amo.erosianmagic.mage.bard.song;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.List;

public record Note(int index, Interval interval, Song.Pitch pitch) {
    public Note {
        if (index > 21) {
            index = index % 21;
        }
    }
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

    public Component toText() {
        return Component.nullToEmpty("Note " + index + ":").copy().append(Component.nullToEmpty("\n    Interval: " + interval.time())).append(Component.nullToEmpty("\n    Pitch: " + pitch.name()));
    }
}
