package com.n.a.game.space;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * <pre>
 * A static orbit sets the position of a celestial body
 * to static values, resulting in no movement within its
 * stage, group or actor it was added to.
 *
 * Throws an UnsupportedOperationException when invoking
 * unsupported methods.
 * </pre>
 * TODO this is a positional controller and does not alter the position, so its
 * really not necessary. Can be solved by the mere absence of a positional controller instead
 */
public class StaticOrbit extends Orbit {
    /**Static Y Position */
    private float x;
    /**Static Y Position */
    private float y;

    public StaticOrbit() {
        //serialization only
    }

    @Override
    public void applyPosition(Actor actor, float delta) {
        actor.setPosition( this.x, this.y);
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
