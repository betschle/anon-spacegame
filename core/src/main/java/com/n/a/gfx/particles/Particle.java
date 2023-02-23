package com.n.a.gfx.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public interface Particle {

    public float getX();
    public float getY();
    public float getScaleX();
    public float getScaleY();
    public float getRotation();

    /**
     * Defines whether a particle was released and ready to render
     * @return true if the particle is active/rendered
     */
    public boolean isActive();

    /**
     * Sets the state whether a particle was released and ready to render
     */
    void setActive(boolean active);

    /**
     * Direct checks if Particle exceeded its lifetime
     * @return
     */
    public boolean isAlive();

    /**
     * Gets the texture of this particle
     * @return
     */
    public TextureRegion getTexture();

    /**
     * Updates this particle
     * @param delta
     */
    public void update(float delta);


    void setAnimation(Animation<TextureRegion> animation);
}
