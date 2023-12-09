package aster.amo.erosianmagic.bard;

import aster.amo.erosianmagic.ErosianMagic;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BardSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "erosianmagic");
    public static RegistryObject<SoundEvent> register(SoundEvent soundEvent) {
        return SOUNDS.register(soundEvent.getLocation().getPath(), () -> soundEvent);
    }
    public static final RegistryObject<SoundEvent> SONG_OF_STORMS = register(SoundEvent.createVariableRangeEvent(ErosianMagic.getId("song_of_storms")));
}
