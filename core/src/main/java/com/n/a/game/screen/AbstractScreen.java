package com.n.a.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.n.a.XYZGame;

/**
 * An Abstract Screen providing basic functionality for XYZ Screens.
 */
public abstract class AbstractScreen implements Screen {

    protected final XYZGame game;

    protected Stage background;
    protected Stage root;
    protected Stage hud;

    protected AbstractScreen ( XYZGame game) {
        this.game = game;
        this.background = new Stage();
        this.root = new Stage();
        this.hud = new Stage();
    }
}
