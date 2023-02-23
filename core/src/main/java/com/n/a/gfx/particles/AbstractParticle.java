package com.n.a.gfx.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Particles get random values for their transformation, life, velocity and other properties
 * from their particle emitter parents.
 */
public abstract class AbstractParticle implements Particle {
    /** max life on this individual particle **/
    protected float maxLifetime;
    /** current frametime, used for animated particles */
    protected float time;
    protected float rotation;
    protected float x; // local
    protected float y; // local
    /** Controls whether this particle is rendered */
    protected boolean active = true;

    protected float delay = 0;
    /** Directional variance */
    protected float spread;
    /** Rotational/angular velocity */
    protected float angularVelocity = 0;
    /** Current traveling direction x */
    protected float dirx;
    /** Current traveling direction y */
    protected float diry;
    /** Inherited from parent */
    protected Animation<TextureRegion> animation;

    public float getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(float maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getSpread() {
        return spread;
    }

    public void setSpread(float spread) {
        this.spread = spread;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getDirx() {
        return dirx;
    }

    public void setDirx(float dirx) {
        this.dirx = dirx;
    }

    public float getDiry() {
        return diry;
    }

    public void setDiry(float diry) {
        this.diry = diry;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    @Override
    public boolean isAlive() {
        return time < maxLifetime;
    }

    @Override
    public TextureRegion getTexture() {
        float percent = time / maxLifetime * animation.getAnimationDuration();
        return animation.getKeyFrame(percent);
    }

    public void update(float delta) {
        time += delta;
        if( active ) {
            // only moves when active
            x += dirx * delta;
            y += diry * delta;
            rotation += angularVelocity * delta;
        } else {
            if( time > delay ) {
                // reset and put on active
                time = 0;
                active = true;
            }
        }
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getScaleX() {
        return 1;
    }

    @Override
    public float getScaleY() {
        return 1;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
