package com.n.a.game.settings;

import com.badlogic.gdx.audio.Music;

/**
 * Game Settings such as music volume and difficulty (soon)
 */
public class GameSettings {

    /** Music range from 0 to 1*/
    private float musicVolume = 0.5f;
    /** Sound Effects volume from 0 to 1 */
    private float sfxVolume = 0.7f;
    /** UI Sound Effects volume from 0 to 1 */
    private float uiVolume = 0.7f;
    private boolean godmode;

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public float getUiVolume() {
        return uiVolume;
    }

    public void setUiVolume(float uiVolume) {
        this.uiVolume = uiVolume;
    }

    public boolean isGodmode() {
        return godmode;
    }

    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
    }
}
