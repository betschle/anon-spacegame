package com.n.a.game.space;

import com.n.a.game.settings.generator.OrbitalBeltSettings;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @see OrbitalBeltSettings
 */
public class OrbitalBelt {

    private String name = ""; // serialize
    private OrbitalBeltSettings settings;
    private List<Planet> satellites = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrbitalBeltSettings getSettings() {
        return settings;
    }

    public void setSettings(OrbitalBeltSettings settings) {
        this.settings = settings;
    }

    public List<Planet> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<Planet> satellites) {
        this.satellites = satellites;
    }

    public void addBeltObject( Planet planet) {
        this.satellites.add(planet);
    }
}
