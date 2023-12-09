package aster.amo.erosianmagic.bard.song;

import aster.amo.erosianmagic.bard.BardSounds;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import aster.amo.erosianmagic.util.ParticleUtil;
import com.cstav.genshinstrument.sound.ModSounds;
import com.cstav.genshinstrument.sound.NoteSound;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import software.bernie.example.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Implement "Song Power" attribute
public class SongRegistry {
    public static final Map<ResourceLocation, Song> SONGS = new HashMap<>();

    public static Song register(ResourceLocation id, Song song) {
        SONGS.put(id, song);
        return song;
    }

    public static Song get(ResourceLocation id) {
        return SONGS.get(id);
    }

    public static ResourceLocation getId(Song song) {
        for (Map.Entry<ResourceLocation, Song> entry : SONGS.entrySet()) {
            if (entry.getValue().equals(song)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Song getClosestMatch(Song song) {
        for (Song s : SONGS.values()) {
            if (s.compare(song)) {
                return s;
            }
        }
        return null;
    }

    public static List<Song> getLooseMatches(Song song) {
        return SONGS.values().stream().filter(s -> s.looseCompare(song)).toList();
    }

    public static final Song SONG_OF_STORMS = register(
            new ResourceLocation("erosianmagic", "storms"),
            new Song(
                    new ArrayList<Note>(List.of(new Note(8, Interval.QUARTER),
                            new Note(12, Interval.WHOLE),
                            new Note(15, Interval.WHOLE),
                            new Note(8, Interval.WHOLE),
                            new Note(12, Interval.WHOLE),
                            new Note(15, Interval.WHOLE)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    player -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        SpellRegistry.BREATHE_EASY.get().attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), 3, player.level(), player, CastSource.SCROLL, true);
                    }
            )
    );

    public static final Song GUIDING_BOLT = register(
            new ResourceLocation("erosianmagic", "guiding_bolt"),
            new Song(
                    new ArrayList<Note>(List.of(new Note(8, Interval.SIXTEENTH),
                            new Note(9, Interval.SIXTEENTH),
                            new Note(12, Interval.SIXTEENTH),
                            new Note(14, Interval.SIXTEENTH),
                            new Note(17, Interval.SIXTEENTH)
                    )),
                    1.0f,
                    Song.Pitch.C1,
                    0.1f,
                    player -> {
                        NoteSound[] sounds = ModSounds.WINDSONG_LYRE_NOTE_SOUNDS;
                        int[] notes = new int[]{8, 9, 12, 14, 17};
                        for (int note : notes) {
                            NoteSound sound = sounds[note];
                            // swap between Pitch.C1, Pitch.C2, Pitch.C3 randomly
                            float[] pitches = new float[]{Song.Pitch.C1.getPitch(), Song.Pitch.C2.getPitch(), Song.Pitch.C3.getPitch()};
                            float pitch = pitches[(int) (Math.random() * pitches.length)];
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), sound.getByPreference(), player.getSoundSource(), 1.0f, pitch);
                        }
                        for(int i = 0; i < 10; i++){
                            Vec3 particlePos = ParticleUtil.getProjectileSpawnPos(player, InteractionHand.MAIN_HAND, 1.0f, 0.25f);
                            ((ServerLevel) player.level()).sendParticles(ParticleTypes.NOTE, particlePos.x(), particlePos.y(), particlePos.z(), 3, 0.25, 0.25, 0.25, 0.0);
                        }
                        io.redspace.ironsspellbooks.api.registry.SpellRegistry.GUIDING_BOLT_SPELL.get().attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), 3, player.level(), player, CastSource.SPELLBOOK, false);
                    }
            )
    );
}
