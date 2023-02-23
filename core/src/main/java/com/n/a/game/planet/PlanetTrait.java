package com.n.a.game.planet;

import com.n.a.game.settings.generator.PlanetTextureSetting;
import com.n.a.game.space.ScanCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * TODO adjust this class to be able to load data in planetFeatures.json
 *
 * Multiple planets can have one or more traits.
 * One trait has one category and may affect texture
 * and texture color.
 *
 * - Traits modify the stats of a planet
 * - Traits are organized in a tree structure.
 */
public class PlanetTrait {
    /** The Id for reference */
    private String id;
    /** The Display Name */
    private String name;
    /** The icon name */
    private String icon;
    /** Text shown in the UI on discovery of this trait. */
    private String discoveryDescription;
    /**Base Points awarded for discovery. */
    private int discoveryPoints;
    /** A Texture setting that is connected to this trait. This
     * is optional, so it can be null. */
    private List<PlanetTextureSetting> textureSettings = new ArrayList<>();
    /** The Category of this trait */
    private ScanCategory category; // TODO
    /** Traits to modify base traits of a planet.
     * "This trait affects planet base traits like so:". These
     * are optional, so they can be null. */
    private Map<String, Integer> modifiers = new HashMap<>();
    /** BaseTrait Requirements. Works as Minimum
     * classification to enable this trait */
    private PlanetBaseTrait[] required; // this doesnt have to be an array, a Map<String, int> suffices

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscoveryDescription() {
        return discoveryDescription;
    }

    public void setDiscoveryDescription(String discoveryDescription) {
        this.discoveryDescription = discoveryDescription;
    }

    public int getDiscoveryPoints() {
        return discoveryPoints;
    }

    public void setDiscoveryPoints(int discoveryPoints) {
        this.discoveryPoints = discoveryPoints;
    }

    public void addTextureSetting(PlanetTextureSetting setting) {
        if(setting != null) this.textureSettings.add(setting);
    }

    public List<PlanetTextureSetting> getTextureSettings() {
        return textureSettings;
    }

    public void setTextureSettings(List<PlanetTextureSetting> textureSettings) {
        this.textureSettings = textureSettings;
    }

    public ScanCategory getCategory() {
        return category;
    }

    public void setCategory(ScanCategory category) {
        this.category = category;
    }

    public PlanetBaseTrait[] getRequired() {
        return required;
    }

    public void setRequired(PlanetBaseTrait[] required) {
        this.required = required;
    }

    public Map<String, Integer> getModifiers() {
        return modifiers;
    }

    public void setModifiers(Map<String, Integer> modifiers) {
        this.modifiers = modifiers;
    }


    @Override
    public String toString() {
        return "PlanetTrait{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
