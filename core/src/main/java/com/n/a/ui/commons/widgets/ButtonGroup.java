package com.n.a.ui.commons.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.n.a.XYZException;
import com.n.a.ui.commons.ButtonClickListener;

/**
 * A button group primarily intended to be used in tabbed panels.
 */
public class ButtonGroup extends Table {

    public enum Orientation {
        VERTICAL,
        HORIZONTAL;
    }

    private Skin skin;
    private Orientation orientation = Orientation.HORIZONTAL;
    private com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup<Button> buttonControl;
    private ButtonClickListener buttonClickListener;
    private ClickListener buttonChangeListener;

    public ButtonGroup ( Skin skin, Orientation orientation) {
        this.skin = skin;
        this.orientation = orientation;
        this.buttonControl = new com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup<>();
        this.buttonControl.setMaxCheckCount(1);
        this.buttonControl.setMinCheckCount(0);
        this.buttonControl.setUncheckLast(true);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets a button click listener that's valid for all buttons. Only one for all buttons.
     * @param buttonClickListener
     */
    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        if( buttonClickListener != null) {
            for( Button button : this.buttonControl.getButtons() ) {
                if( this.buttonClickListener != null) {
                    button.removeListener(this.buttonClickListener);
                }
                button.addListener(buttonClickListener);
            }
        }
        this.buttonClickListener = buttonClickListener;
    }
    /**
     * Sets a click listener that's valid for all buttons. Only one for all buttons.
     * @param clickListener
     */
    public void setButtonClickListener(ClickListener clickListener) {
        if( this.buttonChangeListener != null) {
            for( Button button : this.buttonControl.getButtons() ) {
                if( this.buttonChangeListener != null) {
                    button.removeListener(this.buttonChangeListener);
                }
                button.addListener(clickListener);
            }
        }
        this.buttonChangeListener = clickListener;
    }

    /**
     * Adds a button. Also adds the buttonChangeListener to the button if it was specified.
     * @param stylename
     * @return
     */
    public Button addButton(String stylename, String tooltip) {
        Button button = new Button(skin, stylename);
        button.setDisabled(false);
        if( tooltip != null) {
            button.addListener(new TextTooltip(tooltip, this.skin));
        }
        this.buttonControl.add(button);
        if( this.buttonChangeListener != null) {
            button.addListener(this.buttonChangeListener);
        }
        if( this.buttonClickListener != null) {
            button.addListener(this.buttonClickListener);
        }
        switch( orientation ) {
            case VERTICAL:
                this.add(button).size(64,64).pad(8).row();
                break;
            case HORIZONTAL:
                this.add(button).size(64,64).pad(8);
                break;
            default:
                throw new XYZException(XYZException.ErrorCode.E0004, "Enum case not defined: " + orientation);
        }
        return button;
    }
}
