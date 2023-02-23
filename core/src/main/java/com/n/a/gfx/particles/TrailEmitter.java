package com.n.a.gfx.particles;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.n.a.util.MathUtil;

/**
 * A simple Trail that uses animation particles. Manages update logic
 * of particles. Inside the node structure, the Trail is contained in an EnginePoint,
 * and the engine point contained inside an ExhaustController (a group of Engines).
 * The trail is also an actor, and the particles are spawned in a global
 * space in the particle renderer, but do derive the transformation from the Trail actor.
 *
 * At the moment, there is also some logic in engine controller. How to deal with that?
 */
public class TrailEmitter extends AbstractParticleEmitter<StandardParticle> {

    // trail model
    public static float MAX_LIFE = 0.6f;
    public static float MAX_VELOCITY = 30f;
    public static float MIN_VELOCITY = -1.3f;
    public static float MAX_SPREAD = 2f;
    public static float MAX_DELAY = 0.1f;
    public static float MAX_ANGULAR_VELOCITY = 60f;

    float maxLife = MAX_LIFE;

    public TrailEmitter() {
        super();

    }

    @Override
    protected void setInitialParticleVelocity(AbstractParticle particle) {
        super.setInitialParticleVelocity(particle);
        particle.diry = MathUtil.getRandom( getSpread() );
        particle.dirx = -MathUtil.getRandom( getVelocity());
    }

    @Override
    protected void resetParticle(AbstractParticle particle) {
        super.resetParticle(particle);
        particle.time = 0;
        particle.rotation = 0;
        particle.active = false;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
