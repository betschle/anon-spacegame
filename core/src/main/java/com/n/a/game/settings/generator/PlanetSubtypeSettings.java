package com.n.a.game.settings.generator;

import com.n.a.util.Probability;
import com.n.a.util.Tree;

/**
 * Describes a planet subtype and belongs to planet archetype.
 * This object is merely of structural nature.
 */
public class PlanetSubtypeSettings {
    /**The key this sub type was stored in the archetype settings.*/
    private String name;
    /**A tree of possible traits.*/
    private Tree<Probability> traits;
    /** Minimum axis tilt in degrees. Belongs to Planet BO*/
    private float minAxisTilt = 10;
    /** Maximum axis tilt in degrees. Belongs to Planet BO*/
    private float maxAxisTilt = 27;
    /** The minimum scale variance. 1 is considered earth-like scale */
    private float minScale = 0.9f;
    /** The maximum scale variance. 1 is considered earth-like scale*/
    private float maxScale = 1.2f;

    // serialization only
    public PlanetSubtypeSettings() {

    }

    public Tree<Probability> getTraits() {
        return traits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTraits(Tree<Probability> traits) {
        this.traits = traits;
    }

    public float getMinAxisTilt() {
        return minAxisTilt;
    }

    public void setMinAxisTilt(float minAxisTilt) {
        this.minAxisTilt = minAxisTilt;
    }

    public float getMaxAxisTilt() {
        return maxAxisTilt;
    }

    public void setMaxAxisTilt(float maxAxisTilt) {
        this.maxAxisTilt = maxAxisTilt;
    }

    public float getMinScale() {
        return minScale;
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }
}
