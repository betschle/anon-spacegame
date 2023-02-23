package com.n.a.game.settings.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings for StarSystems that determine default values such as
 * spacing between generated planets or possible min/max speed of each planet.
 */
public class StarSystemSettings implements Serializable {

    /** A name for this star system setting */
    private String name = "";
    /** Default spacing between planets. */
    private float spacing = 600f;
    private PlanetSettings starSettings;
    /** The minimum speed a planet has travelling its orbit */
    private float minPlanetSpeed = 0.0001f;
    /** The maximum speed a planet has travelling its orbit */
    private float maxPlanetSpeed = 0.01f;

    private PlanetSettings star;
    private List<PlanetSettings> planetSettings = new ArrayList<>();
    private List<OrbitalBeltSettings> beltSettings = new ArrayList<>();


    /**
     * Gets system settings with sampleSize planets of the specified type, each type
     * with a probability of 100%
     * @param sampleSize the amount of planets
     * @param subtype the subtype of planets to generate
     * @return
     */
    public static StarSystemSettings getTestSettings(int sampleSize, String archetype, String subtype) {
        StarSystemSettings settings = new StarSystemSettings();
        String subtypename = subtype == null ? "" : "-" + subtype;
        settings.setName("StarSystem_TypeTest_" + archetype + subtypename);

        OrbitSettings orbitSettings = new OrbitSettings();

        PlanetSettings starSettings = new PlanetSettings();
        starSettings.setArchetype("archetype_star");
        starSettings.setOrbitSettings( OrbitSettings.getStaticOrbitSettings(0,0));
        settings.setStarSettings( starSettings );

        List<PlanetSettings> planetSettingsList = new ArrayList<>();
        for(int i =0; i < sampleSize; i++) {
            PlanetSettings planetSettings = new PlanetSettings();
            planetSettings.setArchetype(archetype);
            planetSettings.setSubtype(subtypename);
            planetSettings.setProbability(100);
            planetSettings.setOrbitSettings( orbitSettings );
            planetSettingsList.add(planetSettings);
            // settings.addProbability(archetype, subtype == null ? null : subtype.name(), 100f);
        }
        settings.setPlanetSettings(planetSettingsList);
        settings.setSpacing(100);
        return settings;
    }

    public PlanetSettings getStarSettings() {
        return starSettings;
    }

    public void setStarSettings(PlanetSettings starSettings) {
        this.starSettings = starSettings;
    }

    public StarSystemSettings() {
        // for serialization
    }

    public StarSystemSettings(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlanetSettings> getPlanetProbability() {
        return this.planetSettings;
    }

    public void setPlanetSettings(List<PlanetSettings> planetProbability) {
        this.planetSettings = planetProbability;
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public float getMinPlanetSpeed() {
        return minPlanetSpeed;
    }

    public void setMinPlanetSpeed(float minPlanetSpeed) {
        this.minPlanetSpeed = minPlanetSpeed;
    }

    public float getMaxPlanetSpeed() {
        return maxPlanetSpeed;
    }

    public void setMaxPlanetSpeed(float maxPlanetSpeed) {
        this.maxPlanetSpeed = maxPlanetSpeed;
    }

    public List<OrbitalBeltSettings> getBeltSettings() {
        return beltSettings;
    }

    public void setBeltSettings(List<OrbitalBeltSettings> beltSettings) {
        this.beltSettings = beltSettings;
    }
}
