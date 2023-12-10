package aster.amo.erosianmagic.bard;

import aster.amo.erosianmagic.bard.song.*;
import aster.amo.erosianmagic.net.Networking;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

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
            Song.Pitch pitch = Song.Pitch.fromStep(event.pitch);
            if(pitch == null){
                return;
            }
            if(currentSong == null){
                note = new Note(noteIndex, new Interval(0.0f), pitch);
                ArrayList<Note> notes = new ArrayList<>();
                notes.add(note);
                currentSong = new Song(notes, 1.0f, pitch, 0.1f);
            } else {
                note = new Note(noteIndex, new Interval((currentTime - lastPlayed) / 1000.0f), pitch);
                currentSong.addNote(note);
            }
            lastPlayed = currentTime;
            Song registeredSong = SongRegistry.getClosestMatch(currentSong);
            if(registeredSong != null) {
                ResourceLocation id = SongRegistry.getId(registeredSong);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forMusic(SoundEvent.createVariableRangeEvent(new ResourceLocation("erosianmagic", id.getPath()))));
                Networking.sendToServer(new SongPacket(id, currentSong.isInPerfectTime()));
                currentSong = null;
                if(Minecraft.getInstance().screen != null) {
                    Minecraft.getInstance().screen.onClose();
                }
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
