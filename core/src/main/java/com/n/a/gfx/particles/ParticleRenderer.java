package com.n.a.gfx.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A particle node that collects all particles
 * so they can be rendered. There should only be
 * one particle renderer per stage/root. Particles are rendered in
 * global space instead of local because of this constellation.
 * Can contain particles from multiple sources.
 *
 * TODO particles are not culled
 */
public class ParticleRenderer extends Group {
    List<Particle> particles = new ArrayList<Particle>();

    public void unregisterParticles(List< ? extends Particle> particles) {
        for ( Particle particle : particles) {
            this.particles.remove(particle);
        }
    }

    public void registerParticles(List< ? extends Particle> particles) {
        for ( Particle particle : particles) {
            if( !this.particles.contains(particle) ) {
                this.particles.add(particle);
            }
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        super.drawChildren(batch, parentAlpha);
        for( Particle particle : particles) {
            if( particle.isActive() ) {
                TextureRegion texture = particle.getTexture();
                batch.draw(texture,
                        this.getX() + particle.getX() - texture.getRegionWidth()/2f,
                        this.getY() + particle.getY() - texture.getRegionHeight()/2f,
                        texture.getRegionWidth()/2f,
                        texture.getRegionHeight()/2f,
                        texture.getRegionWidth(),
                        texture.getRegionHeight(),
                        this.getScaleX(), this.getScaleY(), particle.getRotation()
                );
            }
        }
    }
}
