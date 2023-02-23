package com.n.a.io.persistence;

import com.n.a.game.settings.generator.OrbitSettings;
import com.n.a.game.space.StaticOrbit;
import com.n.a.game.space.EllipsoidOrbit;
import com.n.a.game.space.Orbit;

/**
 * Contains data to persist a single orbit for one planet and the planet itself.
 */
public class OrbitPersistence extends Persistence<EllipsoidOrbit> {

    private OrbitSettings.OrbitType orbitType;

    // static

    private float x;
    private float y;

    // ellipsoid
    private float time = 0;
    private float velocity = 0.003f;
    private float width = 200f;
    private float height = 300f;

    private PlanetPersistence planet; // remove from here, instead make planetPersistence have OrbitPersistence as a child

    public OrbitPersistence( Orbit orbit) {
        if( orbit instanceof EllipsoidOrbit) {
            this.init( (EllipsoidOrbit) orbit);
        } else
        if (orbit instanceof StaticOrbit) {
            this.init( (StaticOrbit) orbit);
        }
    }

    private void init( EllipsoidOrbit orbit) {
        this.orbitType = OrbitSettings.OrbitType.ELLIPSOID;
        this.time = orbit.getTime();
        this.velocity = orbit.getVelocity();
        this.width = orbit.getWidth();
        this.height = orbit.getHeight();
    }

    private void init(StaticOrbit orbit) {
        this.orbitType = OrbitSettings.OrbitType.STATIC;
        this.x = orbit.getX();
        this.y = orbit.getY();
    }

    public OrbitSettings.OrbitType getOrbitType() {
        return orbitType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getTime() {
        return time;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public PlanetPersistence getPlanet() {
        return planet;
    }
}
