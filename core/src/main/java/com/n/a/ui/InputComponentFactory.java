package com.n.a.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.n.a.game.planet.PlanetTrait;

/**
 * Creates standardized UI components to obtain user input
 * such as CheckBoxes, Radioboxes, Dropdowns, Buttons,
 * Text Fields.
 * Components created here must be simple and contain
 * little logic or dependencies.
 */
class InputComponentFactory {
    /** The UI Skin. */
    private Skin skin;

    public InputComponentFactory( Skin skin) {
        this.skin = skin;
    }

    /**
     * Gets a standard 32x32 px button, used as control component.
     * @param styleName
     * @return
     */
    public Button getButton(String styleName) {
        Button button = new Button(skin, styleName);
        button.setSize(32, 32);
        button.setDisabled(false);
        return button;
    }

    /**
     * Gets a TextButton for a Menu
     * @param buttonText
     * @return
     */
    public TextButton getMenuTextButton(String buttonText) {
        return new TextButton(buttonText, skin, "mainMenu");
    }


    /**
     * Gets an interactive TextButton for panels and dialogues.
     * @param buttonText
     * @return
     */
    public TextButton getTextButton(String buttonText, boolean canCheck) {
        return new TextButton(buttonText, skin, canCheck ? "interactive-checked" : "interactive");
    }


    public ImageTextButton getTraitButton(PlanetTrait trait) {
        ImageTextButton textButton = new ImageTextButton( trait.getName(), this.skin, "trait");

        // needs one button style per planet type, for now create each one anew based on the one in the skin
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle( textButton.getStyle() );
        style.imageUp = this.skin.getDrawable( trait.getIcon() );
        style.imageDown = style.imageUp;
        style.imageChecked = style.imageUp;
        style.imageCheckedDown = style.imageUp;

        textButton.setStyle(style);
        textButton.setUserObject( trait );
        textButton.getImageCell().right();
        return textButton;
    }

}
