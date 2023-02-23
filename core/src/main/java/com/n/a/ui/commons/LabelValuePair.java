package com.n.a.ui.commons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * A Generic Label/Value Pair used for UI Tables.
 * @param <T> the component type to display the value
 */
public class LabelValuePair<T extends Actor>{

    private Label label;
    private T value;

    public Label getLabel() {
        return label;
    }

    public LabelValuePair<T> setLabel(Label label) {
        this.label = label;
        return this;
    }

    public T getValue() {
        return value;
    }

    public LabelValuePair<T>  setValue(T value) {
        this.value = value;
        return this;
    }
}
