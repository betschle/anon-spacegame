package com.n.a.ui.commons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.n.a.sfx.ButtonSounds;
import com.n.a.sfx.SoundManager;

/**
 * Plays sound for checkbox style buttons on uncheck and check operations only.
 */
public class CheckBoxSounds extends ClickListener {

    private ButtonSounds buttonSounds;
    private SoundManager soundManager;

    public CheckBoxSounds(SoundManager manager, ButtonSounds buttonSounds) {
        this.buttonSounds = buttonSounds;
        this.soundManager = manager;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if( event.getTarget() instanceof Button) {
            Button buttonActor = (Button) event.getTarget();
            if (buttonSounds.getDown() != null && buttonActor.isChecked()) {
                this.soundManager.playSoundOnce(buttonSounds.getDown());
            }

            if (buttonSounds.getUncheck() != null && !buttonActor.isChecked()) {
                this.soundManager.playSoundOnce(buttonSounds.getUncheck());
            }
        }
    }
}
