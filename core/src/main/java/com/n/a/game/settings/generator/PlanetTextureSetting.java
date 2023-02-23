package com.n.a.game.settings.generator;


import com.badlogic.gdx.graphics.Color;

import java.sql.Array;
import java.util.List;

/**
 * Groups texture and configurable color together.
 * Used for loading/saving graphic settings.
 */
public class PlanetTextureSetting {

    /** The texture region name. */
    protected String texture = "env/planet_mask";
    /** The possible tints or color of the texture. */
    protected Color[] color = new Color[0];
    protected float minRotation = 0;
    protected float maxRotation = 0;

    public PlanetTextureSetting() {
        // for serialization
    }

    public PlanetTextureSetting(String textureRegionName, Color... color) {
        this.texture = textureRegionName;
        this.color = color;
    }

    public float getMinRotation() {
        return minRotation;
    }

    public void setMinRotation(float minRotation) {
        this.minRotation = minRotation;
    }

    public float getMaxRotation() {
        return maxRotation;
    }

    public void setMaxRotation(float maxRotation) {
        this.maxRotation = maxRotation;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public Color[] getColor() {
        return color;
    }

    public void setColor(Color[] color) {
        this.color = color;
    }
}
