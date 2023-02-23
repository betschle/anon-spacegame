package com.n.a.gfx.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface ParticleEmitter<T extends Particle> {

    public void start();

    public void stop();

}
