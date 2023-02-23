package com.n.a.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.n.a.game.space.Planet;
import com.n.a.game.EntityID;
import com.n.a.game.space.EllipsoidOrbit;
import com.n.a.game.space.Orbit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * <pre>
 * Simple Planet Graphics.
 *
 * Composed of multiple textures that can be varied and randomized.
 * Provides textures in different layers, e.g. for icecaps, atmosphere plus 3 additional texture layers that can be anything from
 * terrestrial terrain to stripe-like textures for gaseous planets.
 *
 * The texture moves along the x axis to create the illusion of rotation.
 * The planet shape is defined via the mask texture.
 *
 * For alternate masking implementation see
 *
 * https://libgdx.com/wiki/graphics/2d/masking
 * TODO atmosphere / cloud textures are not set/not working right now
 * </pre>
 */
public class PlanetGraphics extends AbstractGraphics {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    // TODO add functionality to rotate textures
    /**The display scale of the planet. This is different from the actor's scale!!*/
    private float planetScale = 1;
    private Planet model;
    private float time = 0;
    private float cloudRotationSpeed = 0;
    /** The orbit that serves as position controller */
    private Orbit orbit;
    /** Sprite array that contains planet textures. Does not include textures for mask, cloud and atmosphere. First added
     * sprite is also drawn first */
    private List<Sprite> sprites = new ArrayList<>();
    /** Sprite for ambient occlusion effect*/
    private Sprite aoSprite;

    public PlanetGraphics(EntityID id) {
        super(id);
    }

    public float getPlanetScale() {
        return planetScale;
    }

    public void setPlanetScale(float planetScale) {
        this.planetScale = planetScale;
    }

    /**
     * Gets the DataModel
     * @return
     */
    public Planet getModel() {
        return model;
    }

    /**
     * Sets the DataModel. Establishes a connection between
     * Entity and its Graphics in both directions.
     * @param model
     */
    public void setModel(Planet model) {
        this.model = model;
        this.model.setGraphics(this);
        this.cloudRotationSpeed = model.getPlanetStats().getPlanetRating("weather") * 1.5f;
    }

    public Orbit getOrbit() {
        return orbit;
    }

    /**
     * Sets the orbit this planet has.
     * @param orbit
     */
    public void setOrbit(Orbit orbit) {
        this.orbit = orbit;
    }

    /**
     * Adds a texture to the Texture array.
     * Textures for clouds or atmosphere and similar should not be included here. First added
     * texture is also drawn first, the very first texture is the base texture.
     *
     * @param texture the texture
     * @param tint the tint the texture has (currently unused)
     * @param rotation rotation inside the graphics in degrees
     */
    public void addTexture(TextureRegion texture, Color tint, float rotation) {
        if( texture == null || tint == null) {
            logger.warning("Texture/Color is null! Texture: " + texture + " / Color:" + tint );
        }
        Sprite sprite = new Sprite(texture);
        sprite.setColor(tint);
        sprite.setRotation( this.getRotation() + rotation);
        sprite.setCenter(sprite.getRegionWidth()/2f, sprite.getRegionHeight()/2f);
        sprite.setOrigin(sprite.getRegionWidth()/2f, sprite.getRegionHeight()/2f);
        this.sprites.add(sprite);
    }

    public void removeTexture(int index) {
        this.sprites.remove(index);
    }

    public int getTextureAmount() {
        return this.sprites.size();
    }

    public List<Sprite> getTextureSprites() {
        return Collections.unmodifiableList(sprites);
    }

    /***
     *
     * @return the ambient occlusion Sprite
     */
    public Sprite getAoSprite() {
        return aoSprite;
    }

    /**
     *
     * @param aoSprite the ambient occlusion Sprite
     */
    public void setAoSprite(Sprite aoSprite) {
        this.aoSprite = aoSprite;
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        this.time += delta;
        // this is not a good thing here lol
        if( this.orbit instanceof EllipsoidOrbit) {
            EllipsoidOrbit ellipsoidOrbit = (EllipsoidOrbit) this.orbit;
            ellipsoidOrbit.applyPosition(this, delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for( Sprite sprite : sprites) {
            sprite.setPosition(this.getX() - sprite.getWidth()/2f, this.getY() - sprite.getHeight()/2f);
            sprite.setScale(this.planetScale, this.planetScale);
            //sprite.setRotation(this.getRotation() + (time + cloudRotationSpeed) );
            sprite.draw(batch);
        }
        if( this.aoSprite != null ) {
            this.aoSprite.setPosition(this.getX() - this.aoSprite.getWidth()/2f,
                                        this.getY() - this.aoSprite.getHeight()/2f);
            this.aoSprite.setScale(this.planetScale , this.planetScale);
            this.aoSprite.draw(batch);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public String toString() {
        return this.id.get() + " " + super.toString();
    }
}
