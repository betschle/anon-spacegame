package com.n.a.game.space;

import com.n.a.game.settings.generator.PlanetArchetypeSettings;
import com.n.a.game.planet.PlanetBaseTrait;
import com.n.a.game.planet.PlanetStats;
import com.n.a.game.settings.generator.PlanetSubtypeSettings;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.game.settings.generator.PlanetGraphicsSettings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DataModel for Planet.
 */
public class Planet implements Serializable {

    // TODO needs an ID
    private String name = ""; // serialize
    private PlanetStats planetStats; // serialize
    private String planetType; // serialize
    /** Additional satellites that orbit this planet. Updates of these obrits are managed by starsystem. */
    private List<Planet> satellites = new ArrayList<>();

    private transient PlanetGraphics graphics;
    private transient PlanetGraphicsSettings settings; // should be stored as string
    private transient PlanetArchetypeSettings archetypeSettings; // should be stored as string
    private transient PlanetSubtypeSettings subtypeSettings; // should be stored as string

    public Planet() {
        PlanetBaseTrait temperature = new PlanetBaseTrait("classification_life", "Temperature", 0);
        PlanetBaseTrait weather = new PlanetBaseTrait("classification_weather", "Weather", 0);
        PlanetBaseTrait life = new PlanetBaseTrait("classification_temperature", "Life", 0);

        this.planetStats = new PlanetStats( new PlanetBaseTrait[]{ life, temperature, weather});

    }

    /**
     * Convenience method
     * @param satellite
     */
    public void addSatellite(Planet satellite) {
        satellites.add(satellite);
    }

    public List<Planet> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<Planet> satellites) {
        this.satellites = satellites;
    }

    /**
     *
     * @return true if the planet has at least one discovered trait
     */
    public boolean hasDiscoveredTraits() {
        return !getPlanetStats().getDiscoveredTraits().isEmpty();
    }

    public String getName() {
        return name;
    }

    /**
     * The displayed name. May be ? if the planet was not discovered yet.
     * @return
     */
    public String getDisplayName() {
        return hasDiscoveredTraits() ? getName() : "?";
    }

    /**
     * The displayed name. May be ? if the planet was not discovered yet.
     * @param godmode to override display if true
     * @return
     */
    public String getDisplayName(boolean godmode) {
        return hasDiscoveredTraits() || godmode ? getName() : "?";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanetType() {
        return planetType;
    }

    public void setPlanetType(String planetType) {
        this.planetType = planetType;
    }

    public PlanetStats getPlanetStats() {
        return planetStats;
    }

    public void setPlanetStats(PlanetStats planetStats) {
        this.planetStats = planetStats;
    }

    public PlanetArchetypeSettings getArchetypeSettings() {
        return archetypeSettings;
    }

    public void setArchetypeSettings(PlanetArchetypeSettings archetypeSettings) {
        this.archetypeSettings = archetypeSettings;
    }

    public PlanetSubtypeSettings getSubtypeSettings() {
        return subtypeSettings;
    }

    public void setSubtypeSettings(PlanetSubtypeSettings subtypeSettings) {
        this.subtypeSettings = subtypeSettings;
    }

    public PlanetGraphicsSettings getSettings() {
        return settings;
    }

    public void setSettings(PlanetGraphicsSettings settings) {
        this.settings = settings;
    }

    public PlanetGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(PlanetGraphics graphics) {
        this.graphics = graphics;
    }

    public void update(float delta) {

    }
}
