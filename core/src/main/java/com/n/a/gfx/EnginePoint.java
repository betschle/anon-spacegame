package com.n.a.gfx;


import com.badlogic.gdx.scenes.scene2d.Group;
import com.n.a.Constants;
import com.n.a.gfx.particles.TrailEmitter;

/**
 * Controls animation and velocity management for engine exhausts.
 * Works like a TrailController
 */
public class EnginePoint extends Group {
    /** The current thrust resting on this engine **/
    protected float thrust;
    /** The current index of engine animation*/
    protected int currentIndex = 0;
    /** The trail of this engine */
    protected TrailEmitter trailEmitter;

    public EnginePoint() {}

    public void setTrail(TrailEmitter trailEmitter) {
        this.trailEmitter = trailEmitter;
    }

    public TrailEmitter getTrail() {
        return trailEmitter;
    }

    /**
     * Updates the exhaust index value. Determines which animation to use
     */
    @Override
    public void act( float delta) {
        super.act(delta);

        if( thrust > Constants.ENGINE_MEDIUM_THRUST) {
            currentIndex = 0;
        } else
        if( thrust < Constants.ENGINE_LOW_THRUST) {
            currentIndex = 2;
        } else {
            currentIndex = 1;
        }
        trailEmitter.getVelocity().setMax(thrust/100f  * TrailEmitter.MAX_VELOCITY + TrailEmitter.MIN_VELOCITY);
        trailEmitter.getAngularVelocity().setMax(thrust/100f  * TrailEmitter.MAX_ANGULAR_VELOCITY);
        /*
        for( StandardParticle particle : trail.getParticles() ) {
            // particle.maxDelay = (1 - thrust / 100f) * Trail.MAX_DELAY;
            particle.maxAngularVelocity = thrust/100f  * Trail.MAX_ANGULAR_VELOCITY;
        }
        */
    }
    public int getCurrentIndex() {
        return currentIndex;
    }
    public void setThrust(float thrust) {
        this.thrust = Math.min(thrust, 100);
    }
}
