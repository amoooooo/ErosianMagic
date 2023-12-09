package aster.amo.erosianmagic.bard.song;

import aster.amo.erosianmagic.bard.BardSounds;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import software.bernie.example.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        SpellRegistry.HEALING_CIRCLE_SPELL.get().attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), 3, player.level(), player, CastSource.SCROLL, true);
                    }
            )
    );
}
