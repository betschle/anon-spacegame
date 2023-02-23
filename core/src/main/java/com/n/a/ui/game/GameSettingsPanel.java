package com.n.a.ui.game;


import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.n.a.game.settings.GameSettings;
import com.n.a.ui.commons.XYZComponent;
import com.n.a.ui.commons.LabelValuePair;

// TODO Change to AudioSettingsPanel? add AbstractPanel for the pair functionality?
public class GameSettingsPanel extends Table implements XYZComponent {

    private GameSettings model;

    private LabelValuePair<Slider> musicVolumePair = new LabelValuePair<>();
    private LabelValuePair<Slider> sfxVolumePair = new LabelValuePair<>();
    private LabelValuePair<Slider> uiVolumePair = new LabelValuePair<>();
    private LabelValuePair<Button> godModePair = new LabelValuePair<>();

    public GameSettingsPanel(Skin skin) {
        super(skin);
    }
    
    @Override
    public void construct() {
        LabelValuePair<?>[] pairs = new LabelValuePair[] {
                this.musicVolumePair, this.sfxVolumePair, this.uiVolumePair
        };

        for( LabelValuePair<?> pair : pairs) {
            this.add(pair.getLabel()).padRight(10).fill();
            this.add(pair.getValue()).minWidth(120f).pad(3).fill();
            this.row();
        }
        this.add(this.godModePair.getLabel()).padRight(10).fill();
        this.add(this.godModePair.getValue()).minSize(48, 24).pad(3).right();
        this.pack();
    }

    @Override
    public void updateFromModel() {
        if( this.model != null) {
            this.musicVolumePair.getValue().setValue( model.getMusicVolume() );
            this.sfxVolumePair.getValue().setValue( model.getSfxVolume() );
            this.uiVolumePair.getValue().setValue( model.getUiVolume() );
            this.godModePair.getValue().setChecked( model.isGodmode() );
        } else {
            this.musicVolumePair.getValue().setValue( 0 );
            this.sfxVolumePair.getValue().setValue( 0 );
            this.uiVolumePair.getValue().setValue( 0 );
            this.godModePair.getValue().setChecked( false );
        }
    }

    public void setModel(GameSettings gameSettings) {
        this.model = gameSettings;
    }

    /**
     * Applies the currently stored values into the GameSettings object, then returns it.
     * @return
     */
    public GameSettings getModel() {
        this.model.setMusicVolume( this.musicVolumePair.getValue().getValue() );
        this.model.setSfxVolume( this.sfxVolumePair.getValue().getValue() );
        this.model.setUiVolume( this.uiVolumePair.getValue().getValue() );
        this.model.setGodmode( this.godModePair.getValue().isChecked() );
        return model;
    }

    public LabelValuePair<Slider> getMusicVolumePair() {
        return musicVolumePair;
    }

    public void setMusicVolumePair(LabelValuePair<Slider> musicVolumePair) {
        this.musicVolumePair = musicVolumePair;
    }

    public LabelValuePair<Slider> getSfxVolumePair() {
        return sfxVolumePair;
    }

    public void setSfxVolumePair(LabelValuePair<Slider> sfxVolumePair) {
        this.sfxVolumePair = sfxVolumePair;
    }

    public LabelValuePair<Slider> getUiVolumePair() {
        return uiVolumePair;
    }

    public void setUiVolumePair(LabelValuePair<Slider> uiVolumePair) {
        this.uiVolumePair = uiVolumePair;
    }

    public LabelValuePair<Button> getGodModePair() {
        return godModePair;
    }

    public void setGodModePair(LabelValuePair<Button> godModePair) {
        this.godModePair = godModePair;
    }
}
