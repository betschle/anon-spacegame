package com.n.a.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conveniently meant to play sounds
 * TODO use two SoundManagers, one for UI sounds, the other for game SFX
 */
public class SoundManager {
    /** Cached sounds */
    private Map<String, Sound> sounds = new HashMap<>();
    /** Sound instances currently being played */
    private List<SoundInstance> soundInstances = new ArrayList<>();
    private float maxVolume = 1f;

    private class SoundInstance {
        long id;
        Sound sound;

        public SoundInstance(long id, Sound sound) {
            this.id = id;
            this.sound = sound;
        }
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    /**
     * Checks if sound registered with soundID exists
     * @param soundID
     * @return
     */
    public boolean hasSound( String soundID ) {
        return this.sounds.containsKey(soundID);
    }

    /**
     * Adds a sound, ready to play
     * @param soundID
     * @param sound
     */
    public void addSound( String soundID, Sound sound) {
        if( soundID != null && sound != null) {
            this.sounds.put(soundID, sound);
        } else {
            Gdx.app.debug(this.getClass().getCanonicalName(), "Could not add sound!");
        }
    }

    /**
     * Sets the maximum volume to be used for this manager
     * @param maxVolume
     */
    public void setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
    }

    /**
     * Plays a sound once
     * @param id the id of the sound to play
     */
    public void playSoundOnce(String id) {
        Sound sound = this.sounds.get(id);
        if( sound != null) {
            long playedID = sound.play();
            sound.setVolume(playedID, this.maxVolume);
            Gdx.app.debug(this.getClass().getCanonicalName(), "Playing sound " + id);
        } else {
            Gdx.app.debug(this.getClass().getCanonicalName(), "Could not find sound of ID " + id);
        }
    }

    /**
     * Loops a sound. Sound must be stopped
     * @param id an id referring to the registered sound
     * @return the sound id of the sound instance
     */
    public long playSoundAsLoop(String id) {
        Sound sound = this.sounds.get(id);
        if( sound != null) {
            long loopingSound = sound.loop(this.maxVolume);
            this.soundInstances.add(new SoundInstance(loopingSound, sound));
            return loopingSound;
        }
        return 0;
    }

    /**
     * Stops a previously looped sound.
     * @param soundId the sound ID
     */
    public void stopSoundLoop(long soundId) {
        SoundInstance looped = null;
        for( SoundInstance sound : soundInstances) {
            if( sound.id == soundId) {
                sound.sound.stop(soundId);
                looped = sound;
            }
        }
        if( looped != null) {
            soundInstances.remove(looped);
        }
    }

    /**
     * Updates the volume on all looping sound instances
     */
    public void updateVolume() {
        for( SoundInstance sounds : soundInstances) {
            sounds.sound.setVolume( sounds.id, this.maxVolume );
        }
    }
}
