package com.n.a.gfx.particles;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.n.a.gfx.ShipGraphics;
import com.n.a.util.MathUtil;
import com.n.a.util.Ranged;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * For animated particles. Offers some basic functionality for a basic particle emitter.
 * Particles are initialized on start() using the values stored inside this particle emitter.
 * Implementations how these values are applied and interpreted onto each particle may vary.
 * @param <T>
 */
public abstract class AbstractParticleEmitter<T extends AbstractParticle>
        extends Actor
        implements ParticleEmitter<T> {

    protected boolean active;
    protected int particleAmount = 50;
    protected List<T> particles = new ArrayList<T>();
    protected Animation<TextureRegion> animation;
    protected String regionName;
    // general particle properties
    /** the maximum angular velocity for each particle */
    protected Ranged<Float> angularVelocity = new Ranged<Float>(30f, 20f);
    /** the velocity variance for each particle */
    protected Ranged<Float> velocity = new Ranged<Float>(30f, 20f);
    /** the spread variance for each particle. */
    protected Ranged<Float> spread = new Ranged<Float>(0.3f, 0.1f);
    /** the delay variance for each particle. */
    protected Ranged<Float> delay = new Ranged<Float>(0.3f, 0.1f);
    /** the life variance for each particle.*/
    protected Ranged<Float> life = new Ranged<Float>(30f, 20f);

    public int getParticleAmount() {
        return particleAmount;
    }

    public void setParticleAmount(int particleAmount) {
        this.particleAmount = particleAmount;
    }

    public Ranged<Float> getAngularVelocity() {
        return angularVelocity;
    }

    public Ranged<Float> getVelocity() {
        return velocity;
    }

    public Ranged<Float> getSpread() {
        return spread;
    }

    public Ranged<Float> getDelay() {
        return delay;
    }

    public Ranged<Float> getLife() {
        return life;
    }

    /**
     * Sets the intitial particle metadata such as lifetime, angular velocity, delay and spread.
     * @param particle
     */
    protected void setInitialParticleMetaData(AbstractParticle particle) {
        particle.maxLifetime = MathUtil.getRandom(getLife());
        particle.angularVelocity = MathUtil.getRandom(getAngularVelocity());
        particle.delay = MathUtil.getRandom(getDelay());
        particle.spread = MathUtil.getRandom(getSpread());
    }

    /**
     * Sets the initial particle position. This should ideally never change from subclasses
     * @param particle
     */
    protected void setInitialParticlePosition(AbstractParticle particle) {
        Vector2 localPosition = this.localToStageCoordinates(new Vector2(this.getOriginX(),this.getOriginY()));
        particle.x = localPosition.x;
        particle.y = localPosition.y;
    }

    /**
     * Sets the initial particle velocity
     * @param particle
     */
    protected void setInitialParticleVelocity(AbstractParticle particle) {

    }

    /**
     * Optional particle reset if a particle is not alive anymore.
     * Handy if a particle is supposed to be reused right away, e.g.
     * for continuous emitters such as engine trails and similar.
     * @param particle
     */
    protected void resetParticle(AbstractParticle particle) {

    }

    public void start() {
        active = true;
        this.updateActive();

        for( AbstractParticle particle : particles) {
            particle.time = 0;
            this.setInitialParticleMetaData(particle);
            this.setInitialParticlePosition(particle);
            this.setInitialParticleVelocity(particle);
        }
    }

    public void stop() {
        active = false;
        this.updateActive();
    }

    private void updateActive() {
        for( T particle : particles) {
            particle.setActive(active);
        }
    }

    public List<T> getParticles() {
        return particles;
    }

    public void setParticles( List<T> particles) {
        this.particles = particles;
        updateActive();
    }

    public void setAnimation(String regionName, Animation<TextureRegion> animation) {
        this.animation = animation;
        this.regionName = regionName;
        for( T particle : particles) {
            particle.setAnimation(animation);
        }
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for( T particle : particles) {
            particle.update(delta);
            if ( !particle.isAlive() ) {
                // reset particle
                this.resetParticle(particle);
                particle.setRotation(this.getGlobalRotation() + particle.getRotation());
                Vector2 local = this.localToStageCoordinates(new Vector2(this.getOriginX(),this.getOriginY()));
                particle.setX( local.x );
                particle.setY( local.y);
            }
        }
    }

    public float getGlobalRotation() {
        float rotation = 0;
        Actor current = this;
        while( !(current instanceof ShipGraphics) && current != null ) {
            rotation += current.getRotation();
            current = current.getParent();
        }
        return rotation;
    }

    protected void parseAndSet(String value, Ranged<Float> ranged) {
        String[] values = value.split(Pattern.quote(" "));
        float val1 = Float.valueOf(values[0]);
        float val2 = Float.valueOf(values[1]);
        if( val1 < val2 ) {
            ranged.set(val2, val1);
        } else {
            ranged.set(val1, val2);
        }
    }

    /**
     * Loads the Particle Emitter properties from a file. The emitter graphics needs to be initialized
     * after calling this method.
     * @param file
     */
    public void load(FileHandle file){
        String s = file.readString();
        JSONObject jo = new JSONObject(s);
        regionName = jo.getString("regionName");
        particleAmount = Integer.valueOf( jo.get("particleAmount").toString() );
        this.parseAndSet((String) jo.get("life"), life);
        this.parseAndSet((String) jo.get("velocity"), velocity);
        this.parseAndSet((String) jo.get("angularVelocity"), angularVelocity);
        this.parseAndSet((String) jo.get("spread"), spread);
        this.parseAndSet((String) jo.get("delay"), delay);
    }

    /**
     * Saves a particle emitter.
     * @param file
     */
    public void save(FileHandle file){
        LinkedHashMap<String, Object> map = new LinkedHashMap<String,Object>();
        map.put("regionName", regionName);
        map.put("particleAmount", particleAmount);
        map.put("life", life.getMax() + " " + life.getMin() );
        map.put("velocity", velocity.getMax() + " " + velocity.getMin() );
        map.put("angularVelocity", angularVelocity.getMax() + " " + angularVelocity.getMin() );
        map.put("spread", spread.getMax() + " " + spread.getMin() );
        map.put("delay", delay.getMax() + " " + delay.getMin() );

        JSONObject jo = new JSONObject(map);
        file.writeString(jo.toString(3), false);
    }
}
