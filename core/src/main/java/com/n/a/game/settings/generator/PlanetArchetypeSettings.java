package com.n.a.game.settings.generator;

import com.n.a.util.Probability;
import com.n.a.util.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Archetypes for Celestial bodies. There are 3 Archetypes currently:
 * solid planets, gaseous planets and stars.
 *
 * TODO rename to CelestialObjectArchetype?
 * TODO add debris, asteroids, spaceship wrecks?
 */
public class PlanetArchetypeSettings {

    /** The ID of this archetype */
    private String id;
    /** The name of this archetype */
    private String name;
    /** The UI Icon for this archetype */
    private String icon;
    /** The description displayed on discovery. */
    private String discoveryDescription;
    /** Minimum axis tilt in degrees. Belongs to Planet BO*/
    private float minAxisTilt = 10;
    /** Maximum axis tilt in degrees. Belongs to Planet BO*/
    private float maxAxisTilt = 27;
    /** The minimum scale variance. 1 is considered earth-like scale */
    private float minScale = 0.9f;
    /** The maximum scale variance. 1 is considered earth-like scale*/
    private float maxScale = 1.2f;
    /**The texture setting for the planet base texture */
    private Map<String, Integer> modifiers; // planet only
    /** The settings for the subtype, which includes traits, axis tilts and scale variances. */
    private Map<String,PlanetSubtypeSettings> planetSubtypeSettings = new HashMap<>();
    /** A tree of possible base traits grouped by PlanetType.*/
    private Map<String,Tree<Probability>> traitTree;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDiscoveryDescription() {
        return discoveryDescription;
    }

    public void setDiscoveryDescription(String discoveryDescription) {
        this.discoveryDescription = discoveryDescription;
    }

    public Map<String, PlanetSubtypeSettings> getPlanetSubtypeSettings() {
        return planetSubtypeSettings;
    }

    public void setPlanetSubtypeSettings(Map<String, PlanetSubtypeSettings> planetSubtypeSettings) {
        this.planetSubtypeSettings = planetSubtypeSettings;
    }

    public Map<String, Tree<Probability>> getTraitTree() {
        return traitTree;
    }

    public void setTraitTree(Map<String, Tree<Probability>> traitTree) {
        this.traitTree = traitTree;
    }

    public Map<String, Integer> getModifiers() {
        return modifiers;
    }

    public void setModifiers(Map<String, Integer> modifiers) {
        this.modifiers = modifiers;
    }
}
