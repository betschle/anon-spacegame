package com.n.a.ui.commons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.n.a.sfx.ButtonSounds;
import com.n.a.sfx.SoundManager;

/**
 * A standard Button Click Listener. Invokes sounds when a button is clicked.
 * Sounds can be configured via {@link ButtonSounds}.
 */
public class ButtonClickListener extends ClickListener {

    private ButtonSounds buttonSounds;
    private SoundManager soundManager;

    public ButtonClickListener(SoundManager manager, ButtonSounds buttonSounds) {
        this.buttonSounds = buttonSounds;
        this.soundManager = manager;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if( buttonSounds.getDown() != null) {
            this.soundManager.playSoundOnce(buttonSounds.getDown());
        }
        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if( buttonSounds.getUncheck() != null) {
            this.soundManager.playSoundOnce(buttonSounds.getUncheck());
        }
        super.touchUp(event, x, y, pointer, button);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        if( fromActor != null && buttonSounds.getHover() != null) {
            // pointer must be < 0 (no click event!) and must be fired from a valid actor (a button etc)
            if( buttonSounds.getHover() != null && pointer < 0 && isValidActor(fromActor)) {
                this.soundManager.playSoundOnce(buttonSounds.getHover());
            }
        }
    }

    /**
     * Valid actors that trigger a sound
     * are Button, TextButton, ImageButton, ImageTextButton
     * @param actor
     * @return
     */
    private boolean isValidActor(Actor actor) {
        return actor instanceof TextButton ||
               actor instanceof Button ||
               actor instanceof ImageButton ||
               actor instanceof ImageTextButton;
    }
}
