package com.n.a.game.space;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.Serializable;

/**
 * An Orbit. Defines movement behavior.
 * <pre>
 * Determines shape of orbit, controls
 * the movement of Planets, Asteroids and similar.
 * The orbit's shape is always elipsoid.
 *
 * The orbit's center is always at 0,0 within
 * the group or stage it was attached to. The movement
 * of a planet therefore is relative to the center
 * of its parent object. This class controls the
 * position of an actor on stage.
 * </pre>
 */
public abstract class Orbit implements Serializable {
    /**
     * Updates the orbit and applies an ellipsoid curse
     * @param delta
     */
    public abstract void applyPosition(Actor actor, float delta);

}
