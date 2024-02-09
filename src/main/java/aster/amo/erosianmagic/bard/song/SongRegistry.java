package aster.amo.erosianmagic.bard.song;

import aster.amo.erosianmagic.bard.BardSounds;
import aster.amo.erosianmagic.registry.AttributeRegistry;
import aster.amo.erosianmagic.util.DelayHandler;
import aster.amo.erosianmagic.util.ParticleUtil;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.item.ModItems;
import com.cstav.genshinstrument.sound.ModSounds;
import com.cstav.genshinstrument.sound.NoteSound;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import software.bernie.example.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                    new ArrayList<Note>(List.of(new Note(8, Interval.ZERO, Song.Pitch.C2),
                            new Note(12, Interval.QUARTER, Song.Pitch.C2),
                            new Note(15, Interval.QUARTER, Song.Pitch.C2),
                            new Note(8, Interval.HALF, Song.Pitch.C2),
                            new Note(12, Interval.QUARTER, Song.Pitch.C2),
                            new Note(15, Interval.QUARTER, Song.Pitch.C2)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        int spellLevel = 1;
                        AbstractSpell spell = SpellRegistry.FORTIFY_SPELL.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song MANTLE_OF_INSPIRATION = register(
            new ResourceLocation("erosianmagic", "mantle_of_inspiration"),
            new Song(
                    new ArrayList<Note>(List.of(new Note(7, Interval.ZERO, Song.Pitch.C2),
                            new Note(8, Interval.QUARTER, Song.Pitch.C2),
                            new Note(12, Interval.QUARTER, Song.Pitch.C2)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        NoteSound[] sounds = getSoundsForItem(player.getItemInHand(InteractionHand.MAIN_HAND));
                        if(sounds == null) sounds = ModSounds.WINDSONG_LYRE_NOTE_SOUNDS;
//                        int[] notes = new int[]{8, 9, 12, 14, 17};
//                        for (int i = 0; i < notes.length; i++) {
//                            NoteSound[] finalSounds = sounds;
//                            int finalI = i;
//                            float interval = SongRegistry.MANTLE_OF_INSPIRATION.getNotes().get(finalI).interval().time();
//                            DelayHandler.addDelay((int)Math.floor(interval * 20), () -> {
//                                NoteSound sound = finalSounds[notes[finalI]];
//                                List<Float> pitches = SongRegistry.MANTLE_OF_INSPIRATION.getNotes().stream().map(note -> note.pitch().getPitch()).toList();
//                                float pitch = pitches.get(finalI);
//                                ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), sound.getByPreference(), player.getSoundSource(), 1.0f, pitch);
//                            });
//                        }
                        int spellLevel = 1;
                        AbstractSpell spell = aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.MANTLE_OF_INSPIRATION.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song BLAZE_STORM = register(
            new ResourceLocation("erosianmagic", "blaze_storm"),
            new Song(
                    new ArrayList<Note>(List.of(
                            new Note(7, Interval.ZERO, Song.Pitch.C2),
                            new Note(7, Interval.QUARTER, Song.Pitch.Db),
                            new Note(9, Interval.QUARTER, Song.Pitch.C2),
                            new Note(10, Interval.QUARTER, Song.Pitch.C2),
                            new Note(11, Interval.QUARTER, Song.Pitch.C2),
                            new Note(11, Interval.HALF, Song.Pitch.C2),
                            new Note(10, Interval.QUARTER, Song.Pitch.C2),
                            new Note(9, Interval.QUARTER, Song.Pitch.C2),
                            new Note(7, Interval.QUARTER, Song.Pitch.Db),
                            new Note(7, Interval.QUARTER, Song.Pitch.C2)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        int spellLevel = 1;
                        AbstractSpell spell = SpellRegistry.BLAZE_STORM_SPELL.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song CONE_OF_COLD = register(
            new ResourceLocation("erosianmagic", "cone_of_cold"),
            new Song(
                    new ArrayList<Note>(List.of(
                            new Note(0, Interval.ZERO, Song.Pitch.C2),
                            new Note(1, Interval.QUARTER, Song.Pitch.Db),
                            new Note(4, Interval.QUARTER, Song.Pitch.C2),
                            new Note(6, Interval.QUARTER, Song.Pitch.C2)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        int spellLevel = 1;
                        AbstractSpell spell = SpellRegistry.CONE_OF_COLD_SPELL.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song ROOT = register(
            new ResourceLocation("erosianmagic", "root"),
            new Song(
                    new ArrayList<Note>(List.of(
                            new Note(7, Interval.ZERO, Song.Pitch.C2),
                            new Note(7, Interval.QUARTER, Song.Pitch.Db),
                            new Note(10, Interval.QUARTER, Song.Pitch.Db),
                            new Note(12, Interval.QUARTER, Song.Pitch.Db)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        int spellLevel = 1;
                        AbstractSpell spell = SpellRegistry.ROOT_SPELL.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song MAGIC_WEAPON = register(
            new ResourceLocation("erosianmagic", "magic_weapon"),
            new Song(
                    new ArrayList<Note>(List.of(
                            new Note(7, Interval.ZERO, Song.Pitch.C2),
                            new Note(8, Interval.QUARTER, Song.Pitch.C2),
                            new Note(10, Interval.QUARTER, Song.Pitch.Db),
                            new Note(12, Interval.QUARTER, Song.Pitch.Db)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        int spellLevel = 1;
                        AbstractSpell spell = aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.MAGIC_WEAPON.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song HEAT_METAL = register(
            new ResourceLocation("erosianmagic", "heat_metal"),
            new Song(
                    new ArrayList<Note>(List.of(
                            new Note(8, Interval.ZERO, Song.Pitch.C2),
                            new Note(10, Interval.QUARTER, Song.Pitch.Db),
                            new Note(11, Interval.QUARTER, Song.Pitch.C2),
                            new Note(12, Interval.QUARTER, Song.Pitch.Db)
                    )),
                    1.0f,
                    Song.Pitch.C2,
                    0.1f,
                    (player, perfect) -> {
                        ((ServerLevel)player.level()).sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.0, player.getZ(), 10, 0.25, 0.25, 0.25, 0.0);
                        int spellLevel = 1;
                        AbstractSpell spell = aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.HEAT_METAL.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null) {
                            spellLevel = Math.max((int) Math.floor(songPower.getValue()), spell.getMinLevel());
                        }
                        if(perfect){
                            spellLevel = Math.min(spellLevel + 1, spell.getMaxLevel());
                            for(int i = 0; i < 10; i++){
                                Vec3 particlePos = new Vec3(
                                        player.getX() + (Math.random() * 2.0 - 1.0),
                                        player.getY() + (Math.random() * 2.0 - 1.0),
                                        player.getZ() + (Math.random() * 2.0 - 1.0)
                                );
                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.END_ROD, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                            }
                            ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, player.getSoundSource(), 1.0f, 2.0f);
                        }
                        spell.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), spellLevel, player.level(), player, CastSource.SPELLBOOK, true);
                    }
            )
    );

    public static final Song GUIDING_BOLT = register(
            new ResourceLocation("erosianmagic", "guiding_bolt"),
            new Song(
                    new ArrayList<Note>(List.of(new Note(8, Interval.ZERO, Song.Pitch.C2),
                            new Note(9, Interval.SIXTEENTH, Song.Pitch.C2),
                            new Note(12, Interval.SIXTEENTH, Song.Pitch.C2),
                            new Note(14, Interval.SIXTEENTH, Song.Pitch.C2),
                            new Note(17, Interval.SIXTEENTH, Song.Pitch.C2)
                    )),
                    1.0f,
                    Song.Pitch.C1,
                    0.1f,
                    (player, perfect) -> {
                        NoteSound[] sounds = getSoundsForItem(player.getItemInHand(InteractionHand.MAIN_HAND));
                        if(sounds == null) sounds = ModSounds.WINDSONG_LYRE_NOTE_SOUNDS;
//                        int[] notes = new int[]{8, 9, 12, 14, 17};
//                        for (int i = 0; i < notes.length; i++) {
//                            NoteSound[] finalSounds = sounds;
//                            int finalI = i;
//                            float interval = SongRegistry.GUIDING_BOLT.getNotes().get(finalI).interval().time();
//                            DelayHandler.addDelay((int)Math.floor(interval * 20), () -> {
//                                NoteSound sound = finalSounds[notes[finalI]];
//                                List<Float> pitches = SongRegistry.GUIDING_BOLT.getNotes().stream().map(note -> note.pitch().getPitch()).toList();
//                                float pitch = pitches.get(finalI);
//                                ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), sound.getByPreference(), player.getSoundSource(), 1.0f, pitch);
//                            });
//                        }
                        for(int i = 0; i < 10; i++){
                            Vec3 particlePos = ParticleUtil.getProjectileSpawnPos(player, InteractionHand.OFF_HAND, 2.0f, 0.1f);
                            ((ServerLevel) player.level()).sendParticles(ParticleTypes.NOTE, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                        }
                        AbstractSpell guidingBolt = SpellRegistry.GUIDING_BOLT_SPELL.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null){
                            int level = Math.max((int) Math.floor(songPower.getValue()), guidingBolt.getMinLevel());
                            guidingBolt.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), level, player.level(), player, CastSource.SPELLBOOK, false);
                        }
                    }
            )
    );

    public static final Song VICIOUS_MOCKERY = register(
            new ResourceLocation("erosianmagic", "vicious_mockery"),
            new Song(
                    new ArrayList<Note>(List.of(new Note(8, Interval.ZERO, Song.Pitch.C2)
                    )),
                    1.0f,
                    Song.Pitch.C1,
                    0.1f,
                    (player, perfect) -> {
                        NoteSound[] sounds = getSoundsForItem(player.getItemInHand(InteractionHand.MAIN_HAND));
                        if(sounds == null) sounds = ModSounds.WINDSONG_LYRE_NOTE_SOUNDS;
//                        int[] notes = new int[]{8, 9, 12, 14, 17};
//                        for (int i = 0; i < notes.length; i++) {
//                            NoteSound[] finalSounds = sounds;
//                            int finalI = i;
//                            float interval = SongRegistry.VICIOUS_MOCKERY.getNotes().get(finalI).interval().time();
//                            DelayHandler.addDelay((int)Math.floor(interval * 20), () -> {
//                                NoteSound sound = finalSounds[notes[finalI]];
//                                List<Float> pitches = SongRegistry.VICIOUS_MOCKERY.getNotes().stream().map(note -> note.pitch().getPitch()).toList();
//                                float pitch = pitches.get(finalI);
//                                ((ServerLevel) player.level()).playSound(null, player.getX(), player.getY(), player.getZ(), sound.getByPreference(), player.getSoundSource(), 1.0f, pitch);
//                            });
//                        }
                        for(int i = 0; i < 10; i++){
                            Vec3 particlePos = ParticleUtil.getProjectileSpawnPos(player, InteractionHand.OFF_HAND, 2.0f, 0.1f);
                            ((ServerLevel) player.level()).sendParticles(ParticleTypes.NOTE, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.25, 0.25, 0.25, 0.0);
                        }
                        AbstractSpell guidingBolt = aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry.VICIOUS_MOCKERY.get();
                        AttributeInstance songPower = player.getAttribute(AttributeRegistry.SONG_POWER.get());
                        if(songPower != null){
                            int level = Math.max((int) Math.floor(songPower.getValue()), guidingBolt.getMinLevel());
                            guidingBolt.attemptInitiateCast(player.getItemInHand(InteractionHand.MAIN_HAND), level, player.level(), player, CastSource.SPELLBOOK, false);
                        }
                    }
            )
    );




    private static NoteSound[] getSoundsForItem(ItemStack item) {
        if(item.is(ModItems.FLORAL_ZITHER.get())){
            return ModSounds.ZITHER_NEW_NOTE_SOUNDS;
        } else if (item.is(ModItems.VINTAGE_LYRE.get())){
            return ModSounds.VINTAGE_LYRE_NOTE_SOUNDS;
        } else if (item.is(ModItems.WINDSONG_LYRE.get())){
            return ModSounds.WINDSONG_LYRE_NOTE_SOUNDS;
        } else if (item.is(ModItems.GLORIOUS_DRUM.get())){
            return ModSounds.GLORIOUS_DRUM;
        }
        return null;
    }
}
