package com.n.a.gfx.particles;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimatedParticleSettings {

    protected String textureRegion;
    protected float animationSpeed;
    protected Animation.PlayMode playmode;

    public Animation.PlayMode getPlaymode() {
        return playmode;
    }

    public void setPlaymode(Animation.PlayMode playmode) {
        this.playmode = playmode;
    }

    public String getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(String textureRegion) {
        this.textureRegion = textureRegion;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }
}
