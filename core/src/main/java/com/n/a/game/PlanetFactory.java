package com.n.a.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.n.a.XYZException;
import com.n.a.game.repository.ColorRepository;
import com.n.a.game.repository.PlanetArchetypeSettingsRepository;
import com.n.a.game.repository.PlanetTraitRepository;
import com.n.a.game.settings.generator.OrbitalBeltSettings;
import com.n.a.game.settings.generator.PlanetArchetypeSettings;
import com.n.a.game.settings.generator.PlanetGraphicsSettings;
import com.n.a.game.settings.generator.PlanetSettings;
import com.n.a.game.settings.generator.PlanetSubtypeSettings;
import com.n.a.game.settings.generator.PlanetTextureSetting;
import com.n.a.game.space.OrbitalBelt;
import com.n.a.game.space.Planet;
import com.n.a.game.space.StarSystem;
import com.n.a.gfx.OrbitalBeltGraphics;
import com.n.a.gfx.ParticleGraphics;
import com.n.a.gfx.PlanetGraphics;
import com.n.a.io.persistence.PlanetPersistence;
import com.n.a.util.Counter;
import com.n.a.util.MathUtil;
import com.n.a.util.Probability;
import com.n.a.util.Tree;
import com.n.a.util.sequences.NumberGenerator;
import com.n.a.game.planet.PlanetStats;
import com.n.a.game.planet.PlanetTrait;
import com.n.a.game.settings.generator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates {@link Planet}s on DataModel level.
 */
public class PlanetFactory {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private DataPack dataPack;
    private EntityIDFactory entityIDFactory;
    private NumberGenerator numberGenerator;
    private PlanetTraitRepository planetTraitRepository;
    private PlanetArchetypeSettingsRepository planetArchetypeSettingsRepository;
    private Counter<String> traitCounter = new Counter<>();

    public PlanetFactory(DataPack dataPack) {
        this.dataPack = dataPack;
        this.entityIDFactory = dataPack.getEntityIDFactory();
        this.numberGenerator = dataPack.getNumberGenerator();
    }

    public void setPlanetArchetypeSettingsRepository(PlanetArchetypeSettingsRepository planetArchetypeSettingsRepository) {
        this.planetArchetypeSettingsRepository = planetArchetypeSettingsRepository;
    }

    public void setPlanetTraitRepository(PlanetTraitRepository planetTraitRepository) {
        this.planetTraitRepository = planetTraitRepository;
    }

    public EntityIDFactory getEntityIDFactory() {
        return entityIDFactory;
    }

    public void setEntityIDFactory(EntityIDFactory entityIDFactory) {
        this.entityIDFactory = entityIDFactory;
    }

    public NumberGenerator getNumberGenerator() {
        return numberGenerator;
    }

    public void setNumberGenerator(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }


    /**
     * Creates a Planet and its graphics using a {@link PlanetPersistence} object.
     * @param planetPersistence
     * @return
     */
    public Planet createPlanetFromPersistence(PlanetPersistence planetPersistence) {
        // TODO move to PersistenceFactory ?
        Planet planet = new Planet();
        planet.setName( planetPersistence.getName() );
        PlanetArchetypeSettings planetArchetypeSettings = this.planetArchetypeSettingsRepository.find(planetPersistence.getPlanetArchetypeSettingsName());
        planet.setArchetypeSettings(planetArchetypeSettings);
        PlanetSubtypeSettings subtypeSettings = planetArchetypeSettings.getPlanetSubtypeSettings().get(planetPersistence.getPlanetSubtypName());
        planet.setSubtypeSettings(subtypeSettings);
        PlanetGraphicsSettings graphicsSettings = createGraphicsSettings(planet);
        planet.setSettings(graphicsSettings);
        PlanetGraphics planetGraphics = this.dataPack.getGraphicsAssembler().assemblePlanet(planetPersistence);
        planetGraphics.setModel(planet);
        planet.setGraphics(planetGraphics);
        // TODO orbiting satellites
        // TODO Planet Stats and Traits
        return planet;
    }

    /**
     * Creates both model and graphics for the planet
     * @param planetSettings
     * @return
     */
    public Planet generatePlanetFromSettings(PlanetSettings planetSettings ) {
        String mainType = (String) planetSettings.getArchetype();
        String subType = planetSettings.getSubtype();
        Planet planet = null;
        if( subType == null || subType.isEmpty()) {
            planet = this.createBasicPlanetFromArcheType(mainType, null );
        } else {
            planet = this.createBasicPlanetFromArcheType(mainType, subType );
        }
        logger.log(Level.FINE, "...generated a planet: " + planet.getPlanetType() + ", id: " + planet.getGraphics().getId().get());
        return planet;
    }

    /**
     * Creates a Planet Model and its Graphics
     * @param planetType
     * @return
     */
    private Planet createBasicPlanetFromArcheType(String archetypeID, String planetType) {
        Object resourceByID = this.dataPack.findResourceByID(archetypeID);
        PlanetArchetypeSettings archetype = (PlanetArchetypeSettings)resourceByID;
        Planet planet = null;
        if( planetType == null) {
            planet = generatePlanetFromArchetype(archetype);
        } else {
            planet = generatePlanetFromArchetype(planetType, archetype);
        }
        PlanetGraphicsSettings settings = createGraphicsSettings(planet); // TODO outsource?
        PlanetGraphics planetGraphics =  this.dataPack.getGraphicsAssembler().assemblePlanet(planet.getName(), settings);
        planet.setGraphics(planetGraphics);
        planet.setSettings(settings);
        planetGraphics.setModel(planet);

        return planet;
    }

    /**
     * Shuffles archetype settings in order to create a planet, using a specified planet type.
     * @param planetType the planet type to use
     * @param settings the archetype settings to derive traits from which affect graphics and model
     * @return
     */
    public Planet generatePlanetFromArchetype(String planetType, PlanetArchetypeSettings settings) {
        logger.log(Level.INFO, "Creating Planet from Archetype {0}", settings.getId());
        Planet planet = new Planet();
        planet.setName(this.dataPack.getPlanetNameRandomizer().generateName());
        planet.setArchetypeSettings(settings);
        planet.setPlanetType(planetType);

        Map<String, PlanetSubtypeSettings> planetSubtypeSettings = settings.getPlanetSubtypeSettings();
        PlanetSubtypeSettings subtype = planetSubtypeSettings.get(planetType);
        if( subtype != null) {
            planet.setSubtypeSettings(subtype);
            this.generateTraits(planet, subtype.getTraits());
        } else {
            this.logger.log(Level.WARNING, "Trait tree for '" + planetType +"' and name '"+ planet.getName() +"' is empty!");
            this.logger.log(Level.WARNING, "No Traits were generated.");
        }
        return planet;
    }

    /**
     * Shuffles archetype settings in order to create a planet, using a random planet type from within
     * the archetype.
     * @param settings the archetype settings to derive traits from which affect graphics and model
     * @return
     */
    public Planet generatePlanetFromArchetype(PlanetArchetypeSettings settings) {
        Set<String> keys = settings.getPlanetSubtypeSettings().keySet();
        String randomPlanetType = (String) this.numberGenerator.getRandomEntry(keys.toArray());
        return this.generatePlanetFromArchetype(randomPlanetType,settings);
    }

    /**
     * Creates graphics settings based on an individual planet.
     * For Planet there is a special case in the assembling process that
     * PlanetGraphics are derived from BOTH the archetype and the
     * individual object's properties (such as individual life/atmosphere etc class)
     * @param planet
     * @return
     */
    public PlanetGraphicsSettings createGraphicsSettings(Planet planet) {
        // TODO move to PlanetGfxFactory
        PlanetGraphicsSettings planetGraphicsSettings = new PlanetGraphicsSettings();
        planetGraphicsSettings.name = planet.getName();
        planetGraphicsSettings.minAxisTilt = planet.getSubtypeSettings().getMinAxisTilt();
        planetGraphicsSettings.maxAxisTilt = planet.getSubtypeSettings().getMaxAxisTilt();
        planetGraphicsSettings.minScale = planet.getSubtypeSettings().getMinScale();
        planetGraphicsSettings.maxScale = planet.getSubtypeSettings().getMaxScale();
        PlanetStats planetStats = planet.getPlanetStats();
        List<PlanetTrait> traits = planetStats.getTraits();

        for( PlanetTrait trait : traits) {
            List<PlanetTextureSetting> textureSettings = trait.getTextureSettings();
            for( PlanetTextureSetting textureSetting : textureSettings) {
                planetGraphicsSettings.addTextureSetting(textureSetting);
            }
        }
        // TODO the AO must be adjusted/set/removed via archetype settings instead!
        if( planet.getArchetypeSettings().getId().equals("archetype_satellite")) {
            planetGraphicsSettings.ambientOcclusion = null; //
        }
        return planetGraphicsSettings;
    }


    /**
     * Generates an orbital belt based on its OrbitalBeltSettings.
     * @param settings
     * @return
     */
    public OrbitalBelt generateOrbitalBelt(OrbitalBeltSettings settings) {
        OrbitalBelt belt = new OrbitalBelt();
        belt.setSettings(settings);

        int count = (int) MathUtil.getAsteroidCountByDensity(settings.getDensity(), settings.getOrbitMaxRadius(), settings.getOrbitMinRadius() );
        for( int i =0; i < count; i++) {
            List<PlanetSettings> allowedSubtypes = settings.getAllowedSubtypes();
            PlanetSettings planetaryObject = this.numberGenerator.getRandomEntry(allowedSubtypes);
            Planet planet = this.generatePlanetFromSettings(planetaryObject); // TODO logging of types/levels
            belt.addBeltObject(planet);
        }
        return belt;
    }

    /**
     *
     * @param orbitalBelt
     * @param settings
     * @return
     */
    public OrbitalBeltGraphics createOrbitalBeltGraphics(StarSystem parentSystem, OrbitalBelt orbitalBelt, OrbitalBeltSettings settings ) {
        List<Sprite> chunkSprites = new ArrayList<>();
        OrbitalBeltGraphics orbitalBeltGraphics = new OrbitalBeltGraphics();
        // randomize chunks
        List<String> chunkNames = settings.getChunkNames();
        ColorRepository colorRepository = this.dataPack.getRepository(ColorRepository.class);
        int chunkCount = (int) MathUtil.getAsteroidCountByDensity(settings.getChunkDensity(), settings.getOrbitMaxRadius(), settings.getOrbitMinRadius() );
        for( int i =0 ; i < chunkCount; i++){
            Vector2 randomPosition = this.getRandomPointInDisc( settings.getOrbitMaxRadius(), settings.getOrbitMinRadius());
            String randomChunkName = this.numberGenerator.getRandomEntry(chunkNames);
            String randomColorName = this.numberGenerator.getRandomEntry(settings.getColors());

            Array<TextureAtlas.AtlasRegion> regions = this.dataPack.getGraphicsAssembler().atlas.findRegions(randomChunkName);
            TextureAtlas.AtlasRegion randomAsteroidParticle = this.numberGenerator.getRandomEntry(regions);
            Sprite sprite = new Sprite(randomAsteroidParticle);
            sprite.setColor( colorRepository.find(randomColorName) );
            ParticleGraphics asteroidParticle = new ParticleGraphics(sprite);
            asteroidParticle.setRotationSpeed(this.numberGenerator.getRandomFloat(5, 0.8f));
            asteroidParticle.setPosition(randomPosition.x, randomPosition.y);
            asteroidParticle.setRotation( this.numberGenerator.getRandomFloat( 360, 0));
            orbitalBeltGraphics.addActor(asteroidParticle);

        }

        for( Planet satellite : orbitalBelt.getSatellites() ) {
            PlanetGraphicsSettings graphicsSettings = this.createGraphicsSettings(satellite);
            PlanetGraphics planetGraphics = this.dataPack.getGraphicsAssembler().assemblePlanet(satellite.getName(), graphicsSettings);
            satellite.setGraphics(planetGraphics);
            planetGraphics.setModel(satellite);
            // random point within disc
            Vector2 randomPosition = this.getRandomPointInDisc( settings.getOrbitMaxRadius(), settings.getOrbitMinRadius());
            planetGraphics.setPosition(randomPosition.x, randomPosition.y);
            parentSystem.registerTarget(planetGraphics);
            orbitalBeltGraphics.addActor(planetGraphics);
        }
        return orbitalBeltGraphics;
    }

    private boolean addPlanetTrait(Planet planet, Probability<String> probability) {
        if ( this.numberGenerator.isTrue(probability.getWeight()) ) {

            PlanetTrait trait = this.planetTraitRepository.find(probability.getType());
            // unhandled error case: probability can have empty type and subtype here if archetype is not setup properly
            if (trait == null) {
                throw new XYZException(XYZException.ErrorCode.E3000, "For planet Trait: " + probability.getType());
            }
            this.logger.log(Level.FINEST, "Added trait: {0}", trait.getId());
            planet.getPlanetStats().addTrait(trait);
            this.traitCounter.addOne(trait.getId());
            return true;
        }
        return false;
    }

    private void generateTraits(Planet planet, Tree<Probability> probabilityTree) {
        Tree.Node<Probability> root = probabilityTree.getRoot();
        Probability rootProbability = root.getObject();
        addPlanetTrait(planet, rootProbability);

        generateTraitsRecursive(planet, root);
    }

    /**walk through the whole tree and add applicable traits
     * if one does not apply, go to the next branch
     * @param planet
     * @param node
     */
    private void generateTraitsRecursive(Planet planet, Tree.Node<Probability> node) {

        for( int i =0 ; i < node.size(); i++ ) {
            Tree.Node<Probability> child = node.getChild(i);
            boolean added = addPlanetTrait(planet, child.getObject());
            if( added && child.size() > 0) {
                // go deeper into the tree
                generateTraitsRecursive(planet, child);
            }
        }
    }


    /**
     * Gets a random point located inside of a disc
     * @param maxradius
     * @param minradius
     * @return
     */
    private Vector2 getRandomPointInDisc(float maxradius, float minradius) {
        float randomWidth = this.numberGenerator.getRandomFloat( maxradius, minradius);
        float randomTime = this.numberGenerator.getRandomFloat(360, 0);
        float x = (float) (randomWidth * Math.cos(randomTime));
        float y = (float) (randomWidth * Math.sin(randomTime));
        return new Vector2(x, y);
    }

    public String getGeneratedTraitsInfo() {
        return this.traitCounter.toString();
    }
}
