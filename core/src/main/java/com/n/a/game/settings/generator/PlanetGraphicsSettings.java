package com.n.a.game.settings.generator;

import com.badlogic.gdx.graphics.Color;
import com.n.a.gfx.PlanetGraphics;

import java.util.ArrayList;
import java.util.List;

/**
 * A Settings object meant to assemble {@link PlanetGraphics} with.
 * Meant to be Serialized as JSON or similar
 *
 * TODO these classes are meant to shuffle data for the generator but are not necessarily
 * suited for serialization of existing objects !!!
 */
public class PlanetGraphicsSettings {

    // TODO in PlanetArcheType add a base PlanetGraphic Setting
    // On Trait Level there are still TextureSettings
    // that extend the PlanetGraphicSetting from the archetype
    /** The name of these settings */
    public String name = "EMPTY";
    /** textures sorted from bottom to top */
    public ArrayList<PlanetTextureSetting> textures = new ArrayList<>();
    /** Texture assigned for the clouds. These will move */
    @Deprecated
    public String clouds = "env/planet_clouds";;
    @Deprecated
    public int cloudsIndex = 1;
    /** Texture assigned for the outer atmosphere, if available */
    public String atmosphere = "env/planet_atmosphere";
    @Deprecated
    public int atmosphereIndex = 1;
    public String ambientOcclusion = "env/planet_ao";
    /** Minimum axis tilt in degrees. Belongs to Planet BO*/
    public float minAxisTilt = 10;
    /** Maximum axis tilt in degrees. Belongs to Planet BO*/
    public float maxAxisTilt = 27;
    /** The minimum scale variance. 1 is considered earth-like scale */
    public float minScale = 0.9f;
    /** The maximum scale variance. 1 is considered earth-like scale*/
    public float maxScale = 1.2f;

    public List<PlanetTextureSetting> getTextures() {
        return textures;
    }

    /**
     * Utility Method to easily add texture settings programmatically.
     * @param textureRegion
     * @param tints
     */
    public void addTextureSetting( String textureRegion, Color... tints) {
        textures.add( new PlanetTextureSetting( textureRegion, tints) );
    }

    /**
     * Convenience Method
     * @param textureSetting
     */
    public void addTextureSetting( PlanetTextureSetting textureSetting) {
        textures.add( textureSetting );
    }

    /**
     * Utility Method to easily add texture settings programmatically.
     * @param textureRegion
     * @param tints
     */
    public void addTextureSetting( float minRotation, float maxRotation, String textureRegion, Color... tints) {
        PlanetTextureSetting planetTextureSetting = new PlanetTextureSetting(textureRegion, tints);
        planetTextureSetting.setMaxRotation(maxRotation);
        planetTextureSetting.setMinRotation(minRotation);
        textures.add( planetTextureSetting );
    }
}
