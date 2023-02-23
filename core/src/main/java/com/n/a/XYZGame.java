package com.n.a;

import com.badlogic.gdx.Gdx;
import com.n.a.game.screen.MainMenuScreen;
import com.n.a.game.screen.SpaceScreen;
import com.n.a.game.settings.GameSettings;
import com.n.a.sfx.MusicPlayer;
import com.n.a.sfx.SoundManager;
import com.n.a.ui.ComponentFactory;

/**
 * A Representation of the XYZ Game.
 * Handles basic infrastruture for one game (Stage setup, player setup...).
 * This is a singleton.
 */
public class XYZGame extends com.badlogic.gdx.Game {

    public static String DEFAULT_DATAPACK = "XYZ-core";
    public static String VERSION = "0.1";
    public static XYZGame xyzGame;

    private GameSettings gameSettings;
    private SoundManager soundManager;
    private MusicPlayer musicPlayer;
    private ComponentFactory componentFactory;
    private MainMenuScreen mainMenuScreen;
    private SpaceScreen spaceScreen;

    public XYZGame() {
        xyzGame = this;
    }

    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @Override
    public void create() {
        // TODO later read out settings from a file
        this.gameSettings = new GameSettings();
        // TODO later read out available sounds from a directory or similar
        this.loadSounds();
        this.componentFactory = new ComponentFactory(this);
        this.mainMenuScreen = new MainMenuScreen(this);
        this.spaceScreen = new SpaceScreen( this);
        this.musicPlayer = new MusicPlayer();
        this.musicPlayer.setMaxVolume(this.gameSettings.getMusicVolume());
        this.musicPlayer.play();
        this.setScreen(mainMenuScreen);
    }

    public void loadSounds() {
        this.soundManager = new SoundManager();
        this.soundManager.setMaxVolume(this.gameSettings.getUiVolume());
        this.soundManager.addSound("beep1", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/beep1.wav")));
        this.soundManager.addSound("beep2", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/beep2.wav")));
        this.soundManager.addSound("beep3", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/beep3.wav")));
        this.soundManager.addSound("click1", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/click1.wav")));
        this.soundManager.addSound("click2", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/click2.wav")));
        this.soundManager.addSound("click3", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/click3.wav")));
        this.soundManager.addSound("tap1", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/tap1.wav")));
        this.soundManager.addSound("tap2", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/tap2.wav")));
        this.soundManager.addSound("click_soft1", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/click_soft1.wav")));
        this.soundManager.addSound("click_soft2", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/click_soft2.wav")));
        this.soundManager.addSound("switch1", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/switch1.wav")));
        this.soundManager.addSound("switch2", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/switch2.wav")));
        this.soundManager.addSound("switch3", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/switch3.wav")));
        this.soundManager.addSound("switch4", Gdx.audio.newSound(Gdx.files.internal("sfx/ui/switch4.wav")));
    }

    public void applyGameSettings(GameSettings gameSettings){
        this.soundManager.setMaxVolume( gameSettings.getUiVolume() );
        this.musicPlayer.setMaxVolume( gameSettings.getMusicVolume() );
        this.spaceScreen.updateGodmode( gameSettings.isGodmode() );
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public SpaceScreen getSpaceScreen() {
        return spaceScreen;
    }
}
