package com.n.a.input.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import java.util.Objects;

/**
 * Catches Input Events from the user and saves the current pressed keys as state in itself, so it
 * can be polled in the graphics controller.
 */
public class ShipInputController implements NavigationPolling {

    boolean left;
    boolean right;
    boolean accelerate;
    boolean brake;
    boolean strafeRight;
    boolean strafeLeft;

    public ShipInputController (){
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isAccelerate() {
        return accelerate;
    }

    public boolean isBrake() {
        return brake;
    }

    @Override
    public boolean isStrafeRight() {
        return strafeRight;
    }

    @Override
    public boolean isStrafeLeft() {
        return strafeLeft;
    }

    @Override
    public boolean handle(Event event) {
        if( event instanceof InputEvent) {
            InputEvent keyevent = (InputEvent) event;
            int keycode = keyevent.getKeyCode();
            InputEvent.Type t = keyevent.getType();

            // left / right
            if ( keycode == Input.Keys.LEFT && Objects.equals(t, InputEvent.Type.keyDown)) {
                left = true;
                return true;
            }
            if ( keycode == Input.Keys.LEFT && Objects.equals(t, InputEvent.Type.keyUp)) {
                left = false;
                return true;
            }
            if( keycode == Input.Keys.RIGHT && Objects.equals(t, InputEvent.Type.keyDown)) {
                right = true;
                return true;
            }
            if( keycode == Input.Keys.RIGHT && Objects.equals(t, InputEvent.Type.keyUp)) {
                right = false;
                return true;
            }

            // accelerate/brake
            if( keycode == Input.Keys.UP && Objects.equals(t, InputEvent.Type.keyDown)) {
                accelerate = true;
                return true;
            }
            if( keycode == Input.Keys.UP && Objects.equals(t, InputEvent.Type.keyUp)) {
                accelerate = false;
                return true;
            }
            if( keycode == Input.Keys.DOWN && Objects.equals(t, InputEvent.Type.keyDown)) {
                brake = true;
                return true;
            }
            if( keycode == Input.Keys.DOWN && Objects.equals(t, InputEvent.Type.keyUp)) {
                brake = false;
                return true;
            }

            // Strafing
            if( keycode == Input.Keys.A && Objects.equals(t, InputEvent.Type.keyDown)) {
                strafeLeft = true;
                return true;
            }
            if( keycode == Input.Keys.A && Objects.equals(t, InputEvent.Type.keyUp)) {
                strafeLeft = false;
                return true;
            }
            if( keycode == Input.Keys.D && Objects.equals(t, InputEvent.Type.keyDown)) {
                strafeRight = true;
                return true;
            }
            if( keycode == Input.Keys.D && Objects.equals(t, InputEvent.Type.keyUp)) {
                strafeRight = false;
                return true;
            }

        }
        return false;
    }
}
