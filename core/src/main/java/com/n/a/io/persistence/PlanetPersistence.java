package com.n.a.io.persistence;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.n.a.game.settings.generator.PlanetTextureSetting;
import com.n.a.game.space.Planet;
import com.n.a.io.SpatialInformation;
import com.n.a.util.MathUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An object that helps to persist {@link Planet}s.
 */
public class PlanetPersistence extends Persistence<Planet> {

    private List<PersistedTexture> textureSettings = new ArrayList<>();
    @Deprecated
    public PersistedTexture clouds;
    public PersistedTexture ambientOcclusion;
    private SpatialInformation spatialInformation;
    private String id;
    private String name;
    private String planetArchetypeSettingsName;
    private String planetSubtypName;

    public class PersistedTexture implements Serializable {
        public String textureName;
        public String colorHex;

        public PersistedTexture() {

        }

        public PersistedTexture(String textureName, String colorHex) {
            this.colorHex = colorHex;
            this.textureName = textureName;
        }
    }

    /**
     * Serialization only
     */
    public PlanetPersistence() {

    }

    public PlanetPersistence( Planet planet ) {
        this.id = planet.getGraphics().getId().get();
        this.name = planet.getName();
        this.planetArchetypeSettingsName = planet.getArchetypeSettings().getId();
        this.planetSubtypName = planet.getSubtypeSettings().getName();
        List<PlanetTextureSetting> textures = planet.getSettings().getTextures();
        List<Sprite> textureSprites = planet.getGraphics().getTextureSprites();
        for( int i =0; i < textures.size(); i++) {
            this.textureSettings.add( new PersistedTexture( textures.get(i).getTexture(), MathUtil.colorToHex( textureSprites.get(i).getColor() )));
        }

        Sprite ambientSprite = planet.getGraphics().getAoSprite();
        if( ambientSprite != null) {
            this.ambientOcclusion = new PersistedTexture(planet.getSettings().ambientOcclusion, MathUtil.colorToHex(ambientSprite.getColor()));
        }

        this.spatialInformation = SpatialInformation.getInformation(planet.getGraphics());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanetSubtypName() {
        return planetSubtypName;
    }

    public PersistedTexture getClouds() {
        return clouds;
    }

    public PersistedTexture getAmbientOcclusion() {
        return ambientOcclusion;
    }

    public List<PersistedTexture> getTextureSettings() {
        return textureSettings;
    }

    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }

    public String getId() {
        return id;
    }

    public String getPlanetArchetypeSettingsName() {
        return planetArchetypeSettingsName;
    }
}
