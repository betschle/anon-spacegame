package com.n.a.game.space;

import com.n.a.game.settings.generator.PlanetGraphicsSettings;

import java.io.Serializable;

/**
 * A helper class that describes features planets can have.
 * These affect textures and scannability of the planet. This
 * class is used to read JSON files and complements the
 * {@link Planet} class.
 */
public class PlanetFeature implements Serializable {

    /** The internal id of this feature */
    private String id;
    /** The display name of this feature */
    private String name;
    /** A description of this scan that shows up at first. */
    private String scanDescription;
    /** A description of this scan after performing it. */
    private String discoveryDescription;
    /** The texture that is applied to the planet graphics (optional) */
    private PlanetGraphicsSettings textureSetting;
    /** The category of this scan */
    private ScanCategory category;
    /** discovery points */
    private int discoveryPoints;
    /** discovery level or tier */
    private int discoveryLevel;

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

    public String getScanDescription() {
        return scanDescription;
    }

    public void setScanDescription(String scanDescription) {
        this.scanDescription = scanDescription;
    }

    public String getDiscoveryDescription() {
        return discoveryDescription;
    }

    public void setDiscoveryDescription(String discoveryDescription) {
        this.discoveryDescription = discoveryDescription;
    }

    public PlanetGraphicsSettings getPlanetGraphicsSettings() {
        return this.textureSetting;
    }

    public void setPlanetGraphicsSettings(PlanetGraphicsSettings textureSetting) {
        this.textureSetting = textureSetting;
    }

    public ScanCategory getCategory() {
        return category;
    }

    public void setCategory(ScanCategory category) {
        this.category = category;
    }

    public int getDiscoveryPoints() {
        return discoveryPoints;
    }

    public void setDiscoveryPoints(int discoveryPoints) {
        this.discoveryPoints = discoveryPoints;
    }

    public int getDiscoveryLevel() {
        return discoveryLevel;
    }

    public void setDiscoveryLevel(int discoveryLevel) {
        this.discoveryLevel = discoveryLevel;
    }

    /*
    // archetype, determines color of landmass and oceans
    ROCKY, // no ocean
    OCEANIC, // little land, lots of water
    VOLCANIC, // rock and traces of magma
    CONTINENTAL, // earth-like

    // geological
    CRATERS,
    RAVINES,


    // biological
    LIFE,
        MICROORGANISMS,
        PLANTS,
        REPTILIAN,
        AMPHIBIAN,
        MAMMALIAN,
        HUMANOID,

    // climate, determines presence of clouds
    ATMOSPHERE,
        TEMPERATURE,
        RAINFALL,
        PRESSURE,
        WINDSPEED,
        STORMS; // lethal storms

    */
}
