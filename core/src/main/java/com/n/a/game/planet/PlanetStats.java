package com.n.a.game.planet;

import com.n.a.XYZException;
import com.n.a.game.space.Planet;
import com.n.a.util.Counter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Table for individual {@link Planet}s. Keeps track
 * of added {@link PlanetTrait} that affect {@link PlanetBaseTrait}, ergo
 * it produces stats derived from PlanetTrait and PlanetBaseTrait.
 */
public class PlanetStats implements Serializable {

    // TODO PlanetStats Listener!!

    /** A list of accepted traits. */
    private List<PlanetTrait> traits = new ArrayList<>();
    /** The current trait about to be discovered. This is an index inside of {@link #traits} */
    private int currentTrait = 0;
    /** A list of discovered traits. */
    private List<PlanetTrait> discoveredTraits = new ArrayList<>();
    /** Keeps track of the counted traits. Directly connects to PlanetRating */
    private Counter<String> traitCounter;
    /** Keeps track of the known traits and the currently known PlanetRating */
    private Counter<String> discoveredTraitCounter;

    /**
     * Serialization only
     */
    public PlanetStats() {

    }

    public PlanetStats(PlanetBaseTrait[] baseTraits) { // TODO remove this initializer
        this.initTraitCounter(baseTraits);
    }

    private void initTraitCounter(PlanetBaseTrait[] baseTraits) {
        String[] ids = new String[baseTraits.length];
        int[] counts = new int[baseTraits.length];
        for( int i = 0; i < baseTraits.length; i++) {
            ids[i] = baseTraits[i].getId();
            counts[i] = baseTraits[i].getClassification();
        }
        this.discoveredTraitCounter = new Counter<>(false);
        this.traitCounter = new Counter<>(false);
    }

    /**
     * Adds a planetTrait to this table. This affects the baseTraits of the planet.
     * @param trait
     */
    public void addTrait(PlanetTrait trait) {
        traits.add( trait );
        if( trait.getModifiers() != null) {
            for (Map.Entry<String, Integer> entry : trait.getModifiers().entrySet()) {
                // TODO no validation
                if( entry.getValue() == null) throw new XYZException(XYZException.ErrorCode.E0004, "Integer value of Modifier can't be null");
                traitCounter.addMany(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Applies modifiers in shape of a map.
     * Changes the discovered trait counter!
     * @param modifiers
     */
    public void applyBaseModifiers(Map<String, Integer> modifiers ) {
        for( Map.Entry<String, Integer> entry : modifiers.entrySet() ) {
            // TODO no validation
            this.discoveredTraitCounter.addMany(entry.getKey(), entry.getValue());
            this.traitCounter.addMany(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Removes a trait from this stat object
     * @param trait
     */
    public void removeTrait( PlanetTrait trait) {
        this.traits.remove(trait);
        for( Map.Entry<String, Integer> entry : trait.getModifiers().entrySet() ) {
            // TODO no validation
            traitCounter.subtractMany(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the trait to the list of discovered traits.
     * @param trait
     */
    private void discoverTrait(PlanetTrait trait) {
        if( this.traits.contains(trait) ) {
            this.discoveredTraits.add(trait);
            for( Map.Entry<String, Integer> entry : trait.getModifiers().entrySet() ) {
                // TODO no validation
                this.discoveredTraitCounter.addMany(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     *
     * @return A list with discovered Traits
     */
    public List<PlanetTrait> getDiscoveredTraits() {
        return discoveredTraits;
    }

    /**
     * Discovers the next trait and then returns it for further processing.
     * @return null if no trait can be discovered
     */
    public PlanetTrait discoverNextTrait() {
        if ( currentTrait >= traits.size() ) return null;
        PlanetTrait undiscovered = traits.get(currentTrait);
        this.discoverTrait(undiscovered);
        currentTrait++;
        return undiscovered;
    }

    /**
     * The percentage of discovered traits
     * @return a float ranging from 0 - 100 in %
     */
    public float getPercentageDiscovered() {
        if( this.traits.isEmpty() || this.discoveredTraits.isEmpty() ) return 0f;
        return this.discoveredTraits.size() / (float) this.traits.size() * 100f;
    }

    /**
     * Gets the counted {@link PlanetBaseTrait}s.
     * @return
     */
    public Counter<String> getTraitCounter() {
        return traitCounter;
    }

    /**
     * Gets all traits that were added as a map.
     * @return a copy of the map of traits, with the keys being PlanetBaseTrait IDs.
     */
    public Map<String, Integer> getCalculatedStats() {
        return traitCounter.getCounterMap();
    }

    /**
     * Gets all traits that were added.
     * @return
     */
    public List<PlanetTrait> getTraits() {
        return traits;
    }

    /**
     *
     * @param planetBaseTraitID
     * @return The classification stored for a planetBaseTraitID. Null if none was found
     */
    public Integer getPlanetRating(String planetBaseTraitID) {
        return this.traitCounter.get(planetBaseTraitID);
    }

    /**
     *
     * @param planetBaseTraitID
     * @return The currently known rating stored for a planetBaseTraitID
     */
    public Integer getDiscoveredPlanetRating(String planetBaseTraitID) {
        return this.discoveredTraitCounter.get(planetBaseTraitID);
    }
}
