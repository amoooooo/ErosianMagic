package aster.amo.erosianmagic.bard.song;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Song {
    private final ArrayList<Note> notes;
    private final float tempo;
    private final Pitch pitch;
    private final float fudge;
    private Consumer<Player> playEffect = null;

    public Song(ArrayList<Note> notes, float tempo, Pitch pitch, float fudge, Consumer<Player> playEffect) {
        this.notes = notes;
        this.tempo = tempo;
        this.fudge = fudge;
        this.pitch = pitch;
        this.playEffect = playEffect;
    }

    public Song(ArrayList<Note> notes, float tempo, Pitch pitch, float fudge) {
        this.notes = notes;
        this.tempo = tempo;
        this.fudge = fudge;
        this.pitch = pitch;
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

    public Pitch getPitch() {
        return pitch;
    }

    public boolean compare(Song song){
        if(song.getNotes().size() != notes.size()){
            return false;
        }
        for(int i = 0; i < notes.size(); i++){
            if(!notes.get(i).isInTime(song.getNotes().get(i).interval().time(), fudge)){
                return false;
            }
        }
        return true;
    }

    public boolean looseCompare(Song song){
        // check if song is a subset of this song.
        if(song.getNotes().size() > notes.size()){
            return false;
        }
        int songIndex = 0;
        for(int i = 0; i < notes.size(); i++){
            if(songIndex >= song.getNotes().size()){
                return true;
            }
            if(notes.get(i).isInTime(song.getNotes().get(songIndex).interval().time(), fudge)){
                songIndex++;
            }
        }
        return songIndex >= song.getNotes().size();
    }

    public void play(Player player) {
        playEffect.accept(player);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("tempo", tempo);
        tag.putFloat("fudge", fudge);
        tag.putString("pitch", pitch.name());
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
        Pitch pitch = Pitch.valueOf(tag.getString("pitch"));
        ListTag noteTags = tag.getList("notes", 10);
        ArrayList<Note> notes = new ArrayList<>();
        for(int i = 0; i < noteTags.size(); i++){
            notes.add(Note.fromNbt(noteTags.getCompound(i)));
        }
        return new Song(notes, tempo, pitch, fudge);
    }
    public enum Pitch {
        C1(0.5f),
        Db1(0.53f),
        D1(0.56f),
        Eb1(0.59f),
        E1(0.63f),
        F1(0.67f),
        Gb1(0.71f),
        G1(0.75f),
        Ab1(0.79f),
        A1(0.84f),
        Bb1(0.89f),
        B1(0.94f),
        C2(1.0f),
        Db(1.06f),
        D2(1.12f),
        Eb2(1.19f),
        E2(1.26f),
        F2(1.33f),
        Gb2(1.41f),
        G2(1.5f),
        Ab2(1.59f),
        A2(1.68f),
        Bb2(1.78f),
        B2(1.89f),
        C3(2.0f);

        private final float pitch;

        Pitch(float pitch) {
            this.pitch = pitch;
        }

        public float getPitch() {
            return pitch;
        }

        public static Pitch fromPitch(float pitch) {
            for (Pitch p : Pitch.values()) {
                if (p.pitch == pitch) {
                    return p;
                }
            }
            return null;
        }
    }
}
