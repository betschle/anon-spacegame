package com.n.a.input.controller;

import com.badlogic.gdx.scenes.scene2d.EventListener;

/**
 * Common interface for navigational polling. Polling can be done through user input
 * or through an AI that serves as controller.
 */
public interface NavigationPolling extends EventListener {

    public boolean isLeft();

    public boolean isRight();

    public boolean isAccelerate();

    public boolean isBrake();

    public boolean isStrafeRight();

    public boolean isStrafeLeft();
}
