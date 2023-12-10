package aster.amo.erosianmagic.bard.song;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Song {
    private final ArrayList<Note> notes;
    private final float tempo;
    private final Pitch basePitch;
    private final float fudge;
    private BiConsumer<Player, Boolean> playEffect = null;
    private boolean perfectTime = false;

    public Song(ArrayList<Note> notes, float tempo, Pitch pitch, float fudge, BiConsumer<Player, Boolean> playEffect) {
        this.notes = notes;
        this.tempo = tempo;
        this.fudge = fudge;
        this.basePitch = pitch;
        this.playEffect = playEffect;
    }

    public Song(ArrayList<Note> notes, float tempo, Pitch pitch, float fudge) {
        this.notes = notes;
        this.tempo = tempo;
        this.fudge = fudge;
        this.basePitch = pitch;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public float getTempo() {
        return tempo;
    }

    public float getFudge() {
        return fudge;
    }

    public Pitch getBasePitch() {
        return basePitch;
    }

    public void setPerfectTime(boolean perfectTime){
        this.perfectTime = perfectTime;
    }

    public boolean isInPerfectTime(){
        return perfectTime;
    }

    public boolean compare(Song song){
        if(song.getNotes().size() != notes.size()){
            return false;
        }
        if(song.basePitch != basePitch){
            return false;
        }
        List<Boolean> perfectTime = new ArrayList<>();
        for(int i = 0; i < notes.size(); i++){
            if(!notes.get(i).isInTime(song.getNotes().get(i).interval().time(), fudge) || notes.get(i).index() != song.getNotes().get(i).index() || notes.get(i).pitch() != song.getNotes().get(i).pitch()){
                return false;
            }
            if(notes.get(i).isInPerfectTime(song.getNotes().get(i).interval().time(), fudge)){
                perfectTime.add(true);
            }
        }
        if(perfectTime.size() >= notes.size() / 2){
            song.perfectTime = true;
        }
        return true;
    }

    public boolean looseCompare(Song song){
        // check if song is a subset of this song.
        if(song.getNotes().size() > notes.size()){
            return false;
        }
        int songIndex = 0;
        if(song.basePitch != basePitch){
            return false;
        }
        for (Note note : notes) {
            if (songIndex >= song.getNotes().size()) {
                return true;
            }
            if (note.isInTime(song.getNotes().get(songIndex).interval().time(), fudge) && note.index() == song.getNotes().get(songIndex).index() && note.pitch() == song.getNotes().get(songIndex).pitch()) {
                songIndex++;
            }
        }
        return songIndex >= song.getNotes().size();
    }

    public void play(Player player, boolean perfectTime) {
        playEffect.accept(player, perfectTime);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("tempo", tempo);
        tag.putFloat("fudge", fudge);
        tag.putString("basePitch", basePitch.name());
        ListTag noteTags = new ListTag();
        for (Note note : notes) {
            noteTags.add(note.toNbt());
        }
        tag.put("notes", noteTags);
        return tag;
    }

    public static Song fromNbt(CompoundTag tag) {
        float tempo = tag.getFloat("tempo");
        float fudge = tag.getFloat("fudge");
        Pitch pitch = Pitch.valueOf(tag.getString("basePitch"));
        ListTag noteTags = tag.getList("notes", 10);
        ArrayList<Note> notes = new ArrayList<>();
        for(int i = 0; i < noteTags.size(); i++){
            notes.add(Note.fromNbt(noteTags.getCompound(i)));
        }
        return new Song(notes, tempo, pitch, fudge);
    }
    public enum Pitch {
        C1(0.5f, -12),
        Db1(0.53f, -11),
        D1(0.56f, -10),
        Eb1(0.59f, -9),
        E1(0.63f, -8),
        F1(0.67f, -7),
        Gb1(0.71f, -6),
        G1(0.75f, -5),
        Ab1(0.79f, -4),
        A1(0.84f, -3),
        Bb1(0.89f, -2),
        B1(0.94f, -1),
        C2(1.0f, 0),
        Db(1.06f, 1),
        D2(1.12f, 2),
        Eb2(1.19f, 3),
        E2(1.26f, 4),
        F2(1.33f, 5),
        Gb2(1.41f, 6),
        G2(1.5f, 7),
        Ab2(1.59f, 8),
        A2(1.68f, 9),
        Bb2(1.78f, 10),
        B2(1.89f, 11),
        C3(2.0f, 12);

        private final float pitch;
        private final int step;

        Pitch(float pitch, int step) {
            this.pitch = pitch;
            this.step = step;
        }

        public float getPitch() {
            return pitch;
        }

        public static Pitch fromPitch(float pitch) {
            pitch = (pitch + 12) / 24.0f + 0.5f;
            for (Pitch p : Pitch.values()) {
                if (p.pitch == pitch) {
                    return p;
                }
            }
            return null;
        }

        public static Pitch fromStep(int step) {
            for (Pitch p : Pitch.values()) {
                if (p.step == step) {
                    return p;
                }
            }
            return null;
        }

        public static float pitchOffset(Pitch pitch1, Pitch pitch2){
            return pitch1.pitch - pitch2.pitch;
        }

        public int getStep() {
            return step;
        }
    }
}
