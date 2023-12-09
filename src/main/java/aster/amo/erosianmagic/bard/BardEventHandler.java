package aster.amo.erosianmagic.bard;

import aster.amo.erosianmagic.bard.song.Interval;
import aster.amo.erosianmagic.bard.song.Note;
import aster.amo.erosianmagic.bard.song.Song;
import aster.amo.erosianmagic.bard.song.SongRegistry;
import aster.amo.erosianmagic.net.Networking;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BardEventHandler {
    public static KeyMapping magicalModifier = new KeyMapping("key.erosianmagic.magicalmodifier", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.erosianmagic");
    static Song currentSong = null;
    static long lastPlayed = 0;
    @SubscribeEvent
    public static void onInstrumentPlayed(InstrumentPlayedEvent.ByPlayer event){
        if(event.isClientSide){
            int noteIndex = event.sound.index;
            long currentTime = System.currentTimeMillis();
            Note note;
            if(currentSong == null){
                note = new Note(noteIndex, new Interval(0.0f));
                ArrayList<Note> notes = new ArrayList<>();
                notes.add(note);
                currentSong = new Song(notes, 1.0f, Song.Pitch.fromPitch(event.pitch), 0.1f);
            } else {
                note = new Note(noteIndex, new Interval((currentTime - lastPlayed) / 1000.0f));
                currentSong.addNote(note);
            }
            lastPlayed = currentTime;
            Song registeredSong = SongRegistry.getClosestMatch(currentSong);
            if(registeredSong != null) {
                ResourceLocation id = SongRegistry.getId(registeredSong);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forMusic(SoundEvent.createVariableRangeEvent(new ResourceLocation("erosianmagic", id.getPath()))));
                Networking.sendToServer(new SongPacket(id));
                currentSong = null;
                Minecraft.getInstance().setScreen(null);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.player.level().isClientSide && currentSong != null){
            if(System.currentTimeMillis() - lastPlayed > 5000){
                currentSong = null;
                event.player.sendSystemMessage(Component.literal("Song reset."));
            }
        }
    }
}
