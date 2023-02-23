package com.n.a.game.space;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.logging.Logger;

/**
 * A Standard Ellipsoid Orbit. Works like a posiitonal Controller
 */
public class EllipsoidOrbit extends Orbit {

    // static to prevent odd behavior during Serialization
    private static transient Logger logger = Logger.getLogger(EllipsoidOrbit.class.getCanonicalName());

    protected float time = 0;
    protected float velocity = 0.003f;

    protected float width = 200f;
    protected float height = 300f;

    public EllipsoidOrbit() {
        // for serialization
    }

    /**
     *
     * @param orbitWidth the travelling width of the orbit
     * @param orbitHeight the travelling height of the orbit
     */
    public EllipsoidOrbit(float orbitWidth, float orbitHeight) {
        // logger.log(Level.FINE, "Creating orbit for \"{0}\", dimensions: {1} x {2}", new Object[]{ planet.getId().get(), orbitWidth, orbitHeight});
        this.width = orbitWidth;
        this.height = orbitHeight;
    }


    /**
     * Gets the current time that determines where on the ellipse
     * the planet is drawn.
     * @return
     */
    public float getTime() {
        return time;
    }

    /**
     * Sets the time. This should never be set after the planet's creation
     * unless for debugging/developing purposes as it would make
     * the planet teleport.
     * @param time
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     *
     * @return the velocity the planet travels along the orbit. This is no physical velocity.
     */
    public float getVelocity() {
        return velocity;
    }

    /**
     * Sets velocity the planet travels along the orbit. This is no physical velocity.
     * @param velocity the velocity the planet travels along the ellipse
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    /**
     * Obtains the width of the ellitpic orbit
     * @return
     */
    public float getWidth() {
        return width;
    }
    /**
     * Sets the width of the elliptic orbit
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Obtains the height of the ellitpic orbit
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of the elliptic orbit
     * @param height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    public float getCurrentX() {
        return (float)(width * Math.cos(time));
    }

    public float getCurrentY() {
        return (float) (height * Math.sin(time));
    }

    @Override
    public void applyPosition(Actor actor, float delta) {
        this.time = this.time + (this.velocity * delta);
        actor.setPosition( getCurrentX(), getCurrentY() );
    }
}
