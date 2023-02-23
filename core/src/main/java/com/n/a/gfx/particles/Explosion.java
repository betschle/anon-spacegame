package com.n.a.gfx.particles;

import com.badlogic.gdx.math.Vector2;
import com.n.a.util.MathUtil;

/**
 * An explosion particle emitter implementation.
 *
 * TODO next step: group multiple explosions together
 */
public class Explosion extends AbstractParticleEmitter<StandardParticle>{

    /*
        Create particles and set their travel direction radially
        Start at 0° degrees keep on spawning until 360° degrees

        TODO for this type of particle emitter the particles must be
        removed from the renderer as soon as all particles have finished playing/being active
     */

    public Explosion () {

    }

    @Override
    protected void setInitialParticleVelocity(AbstractParticle particle) {
        super.setInitialParticleVelocity(particle);
    }

    @Override
    public void start() {
        super.start();
        Vector2 normal = new Vector2(0,1);
        float max = 6.28319f; //360 deg in radians

        // scatter particles across 360 degree circle
        // release them all at once
        float step = particles.size() / max;
        float currentStep = 0;

        for( StandardParticle particle : particles) {
            this.setInitialParticleMetaData(particle);
            this.setInitialParticlePosition(particle);
            // replaces setInitialParticleVelocity
            // scatter all particles around circle
            Vector2 rotate = MathUtil.rotate(normal, MathUtil.getRandomFloat(step, -step) + currentStep);
            particle.dirx = rotate.x * MathUtil.getRandom(getVelocity());
            particle.diry = rotate.y * MathUtil.getRandom(getVelocity());
            currentStep += step;
        }
    }
}
