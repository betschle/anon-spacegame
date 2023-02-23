package com.n.a.game.settings.generator;

import com.n.a.game.space.OrbitalBelt;

import java.util.List;

/**
 * Calls the generator to generate planetary objects in
 * groups. The objects will be spawned as static orbit,
 * so they can be rotated with the whole belt
 * (to reduce position calculations).
 * @see OrbitalBelt
 */
public class OrbitalBeltSettings {

    /* TODO use probability for allowedSubtypes
       TODO define a range of allowed colors or add a color shuffle
     */
    /** The absolute probability ranging from 0 - 100 this particular planet is spawned with. */
    private float probability;
    /** The density of decorative sprites. */
    private float chunkDensity;
    /** The density of scannable objects in this belt. */
    private float density;
    /** A set of allowed objects to be spawned in this belt */
    private List<PlanetSettings> allowedSubtypes;
    /** Minimum belt radius */
    private float orbitMinRadius;
    /** Maximum belt radius */
    private float orbitMaxRadius;
    /** The texture region names to use.
     * These are of undetectable chunks or particles that only serve visual purpose.
     * Expected are names of texture regions without any numbering. The
     * result texture is shuffled from any available texture regions registered under
     * this name. */
    private List<String> chunkNames;
    /** A list of possible colors, as names (must be contained in colors.json) */
    private List<String> colors;

    public float getChunkDensity() {
        return chunkDensity;
    }

    public void setChunkDensity(float chunkDensity) {
        this.chunkDensity = chunkDensity;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public List<PlanetSettings> getAllowedSubtypes() {
        return allowedSubtypes;
    }

    public void setAllowedSubtypes(List<PlanetSettings> allowedSubtypes) {
        this.allowedSubtypes = allowedSubtypes;
    }

    public float getOrbitMinRadius() {
        return orbitMinRadius;
    }

    public void setOrbitMinRadius(float orbitMinRadius) {
        this.orbitMinRadius = orbitMinRadius;
    }

    public float getOrbitMaxRadius() {
        return orbitMaxRadius;
    }

    public void setOrbitMaxRadius(float orbitMaxRadius) {
        this.orbitMaxRadius = orbitMaxRadius;
    }

    public List<String> getChunkNames() {
        return chunkNames;
    }

    public void setChunkNames(List<String> chunkNames) {
        this.chunkNames = chunkNames;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
}
