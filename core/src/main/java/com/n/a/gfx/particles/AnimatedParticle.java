package com.n.a.gfx.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An animated particle backed by an Animation.
 */
public class AnimatedParticle extends AbstractParticle {

    Animation<TextureRegion> animation;

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    @Override
    public TextureRegion getTexture() {
        float percent = time / maxLifetime * animation.getAnimationDuration();
        return animation.getKeyFrame(percent);
    }

    @Override
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

}
