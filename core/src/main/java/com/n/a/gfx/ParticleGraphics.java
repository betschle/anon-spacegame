package com.n.a.gfx;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * An non-animated, decorative particle in space, which can be
 * a chunk of space debris, small asteroids or
 * even resources chunks. It rotates slowly.
 */
public class ParticleGraphics extends SpriteGraphics {

    /** The currently recorded time */
    private float time = 0f;
    /** The speed at which the sprite rotates */
    private float rotationSpeed = 5f;

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public ParticleGraphics(Sprite sprite ) {
        super(sprite);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.time = this.time + (delta * this.rotationSpeed);
        this.sprite.setPosition(this.getX(), this.getY());
        this.sprite.setRotation(this.getRotation() + this.time );
    }
}
