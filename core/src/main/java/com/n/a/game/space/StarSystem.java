package com.n.a.game.space;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.gfx.StarSystemGraphics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DataModel for StarSystem. A star system composes of multiple orbits
 * and one planet that poses as sun. The planets
 * added to this system orbit the planet at the center.
 */
public class StarSystem implements Serializable {

    private Planet sun;
    /**
     *
     */
    private List<OrbitalBelt> belts = new ArrayList<>();

    /** A list of all actors inside of the star system that can be targeted. This
     * includes PlanetGraphics (including those located in belt) or even ShipGraphics.
     * This list is created by the StarSystemFactory on creation and serialization. */
    private transient List<Actor> targetableActors = new ArrayList<>();

    /** Lists of actors that were already discovered.
     * This list is created by the StarSystemFactory on creation and serialization.*/
    private transient List<Actor> discoveredTargetableActors = new ArrayList<>(); // TODO replace discoveredOrbits with this!

    private transient StarSystemSettings settings;

    private transient StarSystemGraphics graphics;

    // remove
    public StarSystem( Planet sun) {
        if( sun == null) {
            throw new NullPointerException("Sun cannot be null");
        }
        this.sun = sun;
        // sun is always known!
        this.discoveredTargetableActors.add(  this.sun.getGraphics());
    }

    public StarSystemSettings getSettings() {
        return settings;
    }

    public void setSettings(StarSystemSettings settings) {
        this.settings = settings;
    }

    public Planet getSun() {
        return sun;
    }

    public StarSystemGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(StarSystemGraphics graphics) {
        this.graphics = graphics;
    }

    public void addOrbits(List<Orbit> orbits) {
        orbits.addAll( orbits );
    }

    public void addBelt( OrbitalBelt belt) {
        this.belts.add(belt);
    }

    /**
     * Registers an actor to be targetable.
     * @param actor
     * @param <T>
     */
    public <T extends Actor> void registerTarget(T actor) {
        this.targetableActors.add(actor);
    }

    public List<Actor> getTargetableActors() {
        return targetableActors;
    }

    /**
     * Gets the number of total planets in this system, minus the star(s)
     * at the center.
     * @return
     */
    public int getPlanetCount() {
        return targetableActors.size();
    }

    /**
     * Adds one orbit/planet to the discovered List to keep track of it.
     * @param graphics
     */
    public void discoverPlanet( PlanetGraphics graphics ) {
        if( !this.discoveredTargetableActors.contains(graphics)) {
            this.discoveredTargetableActors.add(graphics);
        }
    }

    /**
     * Checks if one orbit/planet was already discovered.
     * @param graphics
     * @return true if the planet orbit was already discovered
     */
    public boolean isDiscovered( PlanetGraphics graphics ) {
        if(Objects.equals(sun.getGraphics().getId(), graphics.getId() ) ) {
            return true;
        }
        return this.discoveredTargetableActors.contains(graphics);
    }

    public List<Actor> getDiscoveredTargets() {
        return discoveredTargetableActors;
    }

    /**
     * Gets the amount of discovered targets as factor
     * @return a factor 0-1
     */
    public float getDiscoveredFactor() {
        if( this.discoveredTargetableActors.size() == 0 || this.getTargetableActors().size() == 0) return 0;
        return this.discoveredTargetableActors.size() / (float) this.getTargetableActors().size();
    }
}
