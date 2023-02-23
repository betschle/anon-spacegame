package com.n.a.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.n.a.game.EntityIDFactory;
import com.n.a.game.settings.generator.PlanetArchetypeSettings;
import com.n.a.game.settings.generator.PlanetGraphicsSettings;
import com.n.a.game.settings.generator.PlanetTextureSetting;
import com.n.a.game.settings.generator.ShipGraphicsSettings;
import com.n.a.gfx.particles.AbstractParticleEmitter;
import com.n.a.gfx.particles.AnimatedParticleSettings;
import com.n.a.gfx.particles.Explosion;
import com.n.a.gfx.particles.StandardParticle;
import com.n.a.gfx.particles.TrailEmitter;
import com.n.a.io.SpatialInformation;
import com.n.a.io.persistence.PlanetPersistence;
import com.n.a.util.MathUtil;
import com.n.a.util.sequences.NumberGenerator;
import com.n.a.XYZ.gfx.particles.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides some utility methods to create animations, particle emitters etc.
 * Operates on a Texture Atlas.
 */
public class GraphicsAssembler {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    public TextureAtlas atlas;

    private EntityIDFactory entityIDFactory;
    private NumberGenerator numberGenerator;

    public GraphicsAssembler ( TextureAtlas atlas, NumberGenerator generator, EntityIDFactory entityIDFactory) {
        this.atlas = atlas;
        this.entityIDFactory = entityIDFactory;
        this.numberGenerator = generator;
    }

    public Animation createAnimation(TextureRegion... regions) {
        Array<TextureRegion> keyframes = new Array<>();
        for( TextureRegion region : regions ) {
            keyframes.add(region);
        }
        Animation<TextureRegion> exhaust = new Animation<>(1f, keyframes);
        return exhaust;
    }

    public Animation createAnimation(Array<? extends TextureRegion> regions) {
        Animation exhaust = new Animation(1f, regions);
        return exhaust;
    }

    /**
     * Creates a standard trail emitter.
     * @return
     */
    public TrailEmitter getTrail() {
        logger.log(Level.FINE, "Assembling Trail...");
        String region = "prt/smoke";
        Array<TextureAtlas.AtlasRegion> smoke = atlas.findRegions(region);
        Animation particleAnim = this.createAnimation(smoke.toArray());
        particleAnim.setFrameDuration(1);
        particleAnim.setPlayMode(Animation.PlayMode.NORMAL);

        TrailEmitter trailEmitter = new TrailEmitter();
        trailEmitter.getAngularVelocity().set(TrailEmitter.MAX_ANGULAR_VELOCITY, TrailEmitter.MAX_ANGULAR_VELOCITY * 0.7f );
        trailEmitter.getSpread().set(TrailEmitter.MAX_SPREAD,  TrailEmitter.MAX_SPREAD * 0.7f );
        trailEmitter.getLife().set(TrailEmitter.MAX_LIFE, TrailEmitter.MAX_LIFE * 0.7f);
        trailEmitter.getVelocity().set(TrailEmitter.MAX_VELOCITY, TrailEmitter.MIN_VELOCITY);
        trailEmitter.getDelay().set(TrailEmitter.MAX_DELAY, TrailEmitter.MAX_DELAY * 0.4f);

        List<StandardParticle> particles = new ArrayList<StandardParticle>();
        for(int i = 0; i < trailEmitter.getParticleAmount(); i++) {
            particles.add(new StandardParticle());
        }
        trailEmitter.setParticles(particles);
        trailEmitter.setAnimation(region,particleAnim);
        return trailEmitter;
    }

    /**
     * Creates an explosion emitter.
     * @param particleRegion
     * @param particleAmount
     * @return
     */
    public Explosion getExplosion(String particleRegion, int particleAmount) {
        logger.log(Level.FINE, "Assembling Explosion...");
        Explosion explosion = new Explosion();
        explosion.setParticleAmount(particleAmount);
        explosion.getVelocity().set(43f, 30f);

        List<StandardParticle> particles = new ArrayList<StandardParticle>();
        for( int i =0; i < explosion.getParticleAmount(); i++) {
            particles.add(new StandardParticle());
        }
        explosion.setParticles( particles );
        Animation animation = this.createAnimation(atlas.findRegions(particleRegion));
        explosion.setAnimation(particleRegion, animation );
        explosion.getAngularVelocity().set(190f, 40f);
        return explosion;
    }

    /**
     * Creates any particle emitter that inherits from AbstractParticleEmitter. Works with reflection.
     * @param type
     * @param settings
     * @param <T>
     * @return
     */
    public <T extends AbstractParticleEmitter> T assembleParticleEmitter(Class<T> type, AnimatedParticleSettings settings) {
        logger.log(Level.FINE, "Assembling Abstract Particle Emitter...");
        AbstractParticleEmitter<?> emitter = null;
        try {
            emitter = type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            logger.log(Level.SEVERE, "Could not instantiate particle emitter", e);
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, "Could not instantiate particle emitter", e);
        } catch (InvocationTargetException e) {
            logger.log(Level.SEVERE, "Could not instantiate particle emitter", e);
        } catch (NoSuchMethodException e) {
            logger.log(Level.SEVERE, "Could not instantiate particle emitter", e);
        }
        if ( emitter != null) {
            Array<TextureAtlas.AtlasRegion> textures = atlas.findRegions(settings.getTextureRegion());
            Animation particleAnim = this.createAnimation(textures.toArray());
            // is always 1 (in 100%)
            particleAnim.setFrameDuration(1);
            particleAnim.setPlayMode(settings.getPlaymode());
            emitter.setAnimation(settings.getTextureRegion(),particleAnim);
        }
        return (T) emitter;
    }

    /**
     * Assembles a Planet based on its Persistence Object
     * @param planetPersistence
     * @return
     */
    public PlanetGraphics assemblePlanet(PlanetPersistence planetPersistence) {
        PlanetGraphics planetGraphics = new PlanetGraphics( this.entityIDFactory.get(planetPersistence.getId()) );

        // setup special textures
        PlanetPersistence.PersistedTexture clouds = planetPersistence.getClouds();

        PlanetPersistence.PersistedTexture ambientOcclusion = planetPersistence.getAmbientOcclusion();
        if( ambientOcclusion != null) {
            Sprite ambientSprite = new Sprite(atlas.findRegion(ambientOcclusion.textureName));
            planetGraphics.setAoSprite(ambientSprite);
        }

        // setup planet type textures
        for( PlanetPersistence.PersistedTexture texture : planetPersistence.getTextureSettings() ) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(texture.textureName);
            planetGraphics.addTexture(region, MathUtil.hexToColor(texture.colorHex), 0f);
        }

        // setup transformation
        SpatialInformation.applyToActor(planetGraphics, planetPersistence.getSpatialInformation() );

        return planetGraphics;
    }

    public PlanetGraphics assemblePlanet(String id, PlanetArchetypeSettings settings) {
        PlanetGraphics planet = new PlanetGraphics( entityIDFactory.createID(id) );
        return planet;
    }

    /**
     * Assembles a {@link PlanetGraphics} object
     * @param settings
     * @param id the prefix for the id to use
     * @return
     */
    public PlanetGraphics assemblePlanet(String id, PlanetGraphicsSettings settings) {
        logger.log(Level.INFO, "Assembling Planet of ID \"{0}\" using settings \"{1}\"...",
                new Object[]{ id +"", settings.name + ""});

        PlanetGraphics planet = new PlanetGraphics( entityIDFactory.createID(id) );
        if( settings.ambientOcclusion != null ) {
            planet.setAoSprite( new Sprite( atlas.findRegion(settings.ambientOcclusion)));
        }
        planet.setSize(256, 256); //default size
        planet.setPlanetScale( this.numberGenerator.getRandomFloat( settings.maxScale, settings.minScale) );

        logger.log(Level.FINE, "... Rotation: {0}", planet.getRotation());
        logger.log(Level.FINE, "... Scale: {0}", planet.getPlanetScale());
        for (PlanetTextureSetting texture : settings.textures) {

            logger.log(Level.FINE, "... Randomize Texture: {0}", texture.getTexture());
            // randomize regions per region name
            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(texture.getTexture());
            if( regions.isEmpty() ) {
                logger.log(Level.WARNING, "Could not find texture regions of name {0}, creation of planet id {1} skipped",
                        new Object[]{ texture.getTexture(), id } );
                continue;
            }
            if( texture.getColor().length == 0) {
                logger.log(Level.WARNING, "Could not find colors for texture {0}, creation of planet id {1} skipped",
                        new Object[]{ texture.getTexture(), id } );
                continue;
            }
            planet.addTexture(
                    this.numberGenerator.<TextureAtlas.AtlasRegion>getRandomEntry(regions) ,
                    this.numberGenerator.<Color>getRandomEntry(texture.getColor() ),
                    this.numberGenerator.getRandomFloat(texture.getMaxRotation(), texture.getMinRotation())
            );
        }
        return planet;
    }

    /**
     * Assembles a {@link ShipGraphics} object
     * @deprecated
     * @return
     */
    @Deprecated
    public ShipGraphics assemblePlayer() {

        logger.log(Level.FINE, "Assembling Player...");
        ShipGraphics player = new ShipGraphics(this.entityIDFactory.createID("player"));
        player.setShipTexture( atlas.findRegion("ship/ship") );
        player.exhaust = new ExhaustGraphics(atlas);
        // important; this adjusts the exhaust
        // center it at the player
        // TODO ensure that the center is always at 0/0
        player.exhaust.setPosition( - player.exhaust.getWidth()/2f, player.getHeight()/2f - player.exhaust.getHeight()/2f);
        player.addActor(player.exhaust);
        Array<TextureAtlas.AtlasRegion> smoke = atlas.findRegions("prt/smoke");
        Animation particleAnim = this.createAnimation(smoke.toArray());
        particleAnim.setFrameDuration(1);
        particleAnim.setPlayMode(Animation.PlayMode.NORMAL);

        // init engine point trail
        for(EnginePoint engine : player.exhaust.getEngines() ) {
            TrailEmitter trailEmitter = getTrail();
            engine.setTrail(trailEmitter);
            engine.addActor(engine.getTrail());
            trailEmitter.start();
        }
        return player;
    }

    public ShipGraphics assembleShip( ShipGraphicsSettings settings) {
        logger.log(Level.FINE, "Assembling Ship...");
        ShipGraphics ship = new ShipGraphics(this.entityIDFactory.createID(settings.shipTextureRegion) );
        ship.setShipTexture( atlas.findRegion(settings.shipTextureRegion) );
        ship.exhaust = new ExhaustGraphics(atlas);
        // important; this adjusts the exhaust
        // center it at the player
        // TODO ensure that the center is always at 0/0
        ship.exhaust.setPosition( ship.exhaust.getWidth()/2f, ship.getHeight()/2f - ship.exhaust.getHeight()/2f);
        ship.addActor(ship.exhaust);
        Array<TextureAtlas.AtlasRegion> smoke = atlas.findRegions(settings.trailParticleTextureRegion);
        Animation particleAnim = this.createAnimation(smoke.toArray());
        particleAnim.setFrameDuration(1);
        particleAnim.setPlayMode(Animation.PlayMode.NORMAL);

        // init engine point trail
        for(EnginePoint engine : ship.exhaust.getEngines() ) {
            TrailEmitter trailEmitter = getTrail();
            engine.setTrail(trailEmitter);
            engine.addActor(engine.getTrail());
            trailEmitter.start();
        }
        return ship;
    }
}
