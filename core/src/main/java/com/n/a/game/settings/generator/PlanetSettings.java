package com.n.a.game.settings.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * Planet Settings for the proc gen. Defines the probability
 * of a planet to occur, its archetype, subtype, orbit and
 * any additional satellites.
 */
public class PlanetSettings {

    /** The absolute probability ranging from 0 - 100 this particular planet is spawned with. */
    private float probability;
    /** An ID of the archetype of this planet: solid, gaseous, star */
    private String archetype;
    /** An ID of the subtype of this planet, e.g. an earth- or jupiter-like planet.
     * If this is left null, the subtype is randomly chosen */
    private String subtype;
    /** The orbit settings to use for this planet*/
    private OrbitSettings orbitSettings;
    /**Any satellites (moons or similar) that may orbit this planet. */
    private List<PlanetSettings> satellites = new ArrayList<>();
    // private List<> spaceStations = new ArrayList<>();


    public List<PlanetSettings> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<PlanetSettings> satellites) {
        this.satellites = satellites;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public OrbitSettings getOrbitSettings() {
        return orbitSettings;
    }

    public void setOrbitSettings(OrbitSettings orbitSettings) {
        this.orbitSettings = orbitSettings;
    }

    @Override
    public String toString() {
        return "PlanetSettings{" +
                "probability=" + probability +
                ", archetype='" + archetype + '\'' +
                ", subtype='" + subtype + '\'' +
                '}';
    }
}
