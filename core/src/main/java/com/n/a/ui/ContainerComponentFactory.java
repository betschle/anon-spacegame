package com.n.a.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/*
 * Creates standardized UI components as containers,
 * such as panels or accordions.
 * Components created here must be simple and contain
 * little logic or dependencies.
 */
public class ContainerComponentFactory {
    /** The UI Skin. */
    private Skin skin;

    public ContainerComponentFactory( Skin skin) {
        this.skin = skin;
    }
}
