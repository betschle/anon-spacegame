package com.n.a.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Class for multiple Key - Float value pairings stored
 * internally in a hashmap, where float value is within its defined range.
 * Float values of the same keys are added and subtracted.
 */
public class Modifiers {
    // TODO Move to utility later
    /** Min allowed value */
    private float min = 0f;
    /** Max allowed value */
    private float max = 1f;
    private Map<String, Float> modifiers = new HashMap<>();

    public Modifiers() {

    }

    public Modifiers(float max, float min) {
        this.max = max;
        this.min = min;
    }
    /**
     *
     * @param key the key
     * @param value value will be clamped to fit min/max
     */
    public void putModifier(String key, float value) {
        float clamped = Math.min(Math.max(this.min, value), this.max);
        this.modifiers.put(key, clamped);
    }

    public Float get(String key) {
        return modifiers.get(key);
    }

    /**
     *
     * @param key the key to use
     * @return if a float assigned to key exists
     */
    public boolean containsKey(String key) {
        return modifiers.containsKey(key);
    }

    /**
     * Sets the defined range. Must be set before
     * adding modifiers.
     * @param max
     * @param min
     */
    public void setRange( float max, float min) {
        if( max > min) {
            this.max = max;
            this.min = min;
        } // else throw new XYZException() // TODO
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void reset() {
        this.modifiers.clear();
    }
    /**
     * Gets the entries of the map.
     * @return
     */
    public Set<Map.Entry<String, Float>> getEntries() {
        return modifiers.entrySet();
    }

    @Override
    public String toString() {
        return "Modifiers{" +
                "modifiers=" + modifiers +
                '}';
    }
}
