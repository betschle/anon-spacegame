package com.n.a.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.n.a.game.settings.generator.OrbitSettings;
import com.n.a.game.settings.generator.OrbitalBeltSettings;
import com.n.a.game.settings.generator.PlanetSettings;
import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.game.space.EllipsoidOrbit;
import com.n.a.game.space.Orbit;
import com.n.a.game.space.OrbitalBelt;
import com.n.a.game.space.Planet;
import com.n.a.game.space.StarSystem;
import com.n.a.game.space.StaticOrbit;
import com.n.a.gfx.OrbitalBeltGraphics;
import com.n.a.gfx.StarSystemGraphics;
import com.n.a.io.persistence.OrbitPersistence;
import com.n.a.io.persistence.PlanetPersistence;
import com.n.a.io.persistence.StarSystemPersistence;
import com.n.a.util.Counter;
import com.n.a.util.sequences.NumberGenerator;
import com.n.a.XYZ.game.space.*;
import com.n.a.gfx.GraphicsAssembler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <pre>
 * Generates functioning star systems.
 * Right now it only assembles the graphics and randomizes them.
 * </pre>
 */
public class StarSystemFactory {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    /** Maximum extent in which planets are placed*/
    private float maxExtent = 35000;
    private DataPack dataPack;
    private GraphicsAssembler graphicsAssembler;
    private EntityIDFactory entityIDFactory;
    private NumberGenerator numberGenerator;
    private PlanetFactory planetFactory; // TODO Implement

    /** Local planet type counter per system */
    private Counter counter;
    /** Global planet type counter per game */
    private Counter globalCounter;
    private Counter<Integer> lifeLevelCounter = new Counter<>();
    private Counter<Integer> weatherLevelCounter = new Counter<>();
    private Counter<Integer> temperatureLevelCounter = new Counter<>();

    /**
     *
     * @param dataPack the datapack to use colors and planet types from.
     */
    public StarSystemFactory(DataPack dataPack) {
        // TODO the color of each layer should depend A) on the temperature of the planet and B) type of layer (grass etc)
        this.dataPack = dataPack;
        this.entityIDFactory = dataPack.getEntityIDFactory();
        this.numberGenerator = dataPack.getNumberGenerator();
        this.graphicsAssembler = dataPack.getGraphicsAssembler();
        this.planetFactory = dataPack.getPlanetFactory();
        this.counter = new Counter();
        this.globalCounter = new Counter();

        // TODO init by classification
        this.lifeLevelCounter.addDisplayName(-2, "No Life (-1)");
        this.lifeLevelCounter.addDisplayName(-1, "No Life (-2)");
        this.lifeLevelCounter.addDisplayName(0, "No Life");
        this.lifeLevelCounter.addDisplayName(1, "Bacterial");
        this.lifeLevelCounter.addDisplayName(2, "Floral");
        this.lifeLevelCounter.addDisplayName(3, "Animal");
        this.lifeLevelCounter.addDisplayName(4, "Sentient");
        this.lifeLevelCounter.addDisplayName(5, "Space-faring");
        this.lifeLevelCounter.addDisplayName(6, "Space-faring (+1)");
        this.lifeLevelCounter.addDisplayName(7, "Space-faring (+2)");

        this.weatherLevelCounter.addDisplayName(-2, "No Atmosphere (-2)");
        this.weatherLevelCounter.addDisplayName(-1, "No Atmosphere (-1)");
        this.weatherLevelCounter.addDisplayName(0, "No Atmosphere");
        this.weatherLevelCounter.addDisplayName(1, "Thin Atmosphere");
        this.weatherLevelCounter.addDisplayName(2, "Normal Storms");
        this.weatherLevelCounter.addDisplayName(3, "Raging Storms");
        this.weatherLevelCounter.addDisplayName(4, "Extreme Storms");
        this.weatherLevelCounter.addDisplayName(5, "Solar Winds");
        this.weatherLevelCounter.addDisplayName(6, "Solar Winds (+1)");
        this.weatherLevelCounter.addDisplayName(7, "Solar Winds (+2)");

        this.temperatureLevelCounter.addDisplayName(-1, "Extreme Cold (-2)");
        this.temperatureLevelCounter.addDisplayName(-1, "Extreme Cold (-1)");
        this.temperatureLevelCounter.addDisplayName(0, "Extreme Cold");
        this.temperatureLevelCounter.addDisplayName(1, "Cold");
        this.temperatureLevelCounter.addDisplayName(2, "Normal");
        this.temperatureLevelCounter.addDisplayName(3, "Hot");
        this.temperatureLevelCounter.addDisplayName(4, "Extremely Hot");
        this.temperatureLevelCounter.addDisplayName(5, "Extremely Hot (+1)");
        this.temperatureLevelCounter.addDisplayName(6, "Extremely Hot (+2)");
    }

    public EntityIDFactory getEntityIDFactory() {
        return entityIDFactory;
    }

    public GraphicsAssembler getGraphicsAssembler() {
        return graphicsAssembler;
    }

    public float getMaxExtent() {
        return maxExtent;
    }

    public void setMaxExtent(float maxExtent) {
        this.maxExtent = maxExtent;
    }

    /**
     *
     * @param starSystemPersistence
     * @return
     */
    // TODO test this
    public StarSystem createStarSystemFromPersistence(StarSystemPersistence starSystemPersistence) {
        // setup the star
        PlanetPersistence sunPersistence = starSystemPersistence.getSun();
        Planet sunPlanet = this.planetFactory.createPlanetFromPersistence(sunPersistence);
        // setup the system itself
        StarSystem starSystem = this.createBasicStarSystem(starSystemPersistence.getId(), sunPlanet);
        // setup the orbits
        // I see where the error is! Orbits is a collection of ALL orbits without taking its hierarchy into account
        // the order of which planet revolves around which is not preserved! I must nest the persistence objects instead
        for( OrbitPersistence orbitPersistence : starSystemPersistence.getOrbits()) {
            Orbit orbitFromPersistence = this.createOrbitFromPersistence(orbitPersistence);
            // starSystem.registerTarget(orbitFromPersistence.getPlanetGraphics());
            // starSystem.getGraphics().addActor( orbitFromPersistence.getPlanetGraphics() );
        }
        return starSystem;
    }

    /**
     * Assmbles an orbit (planet included) from a Persistence Object
     * @param orbitPersistence
     * @return
     */
    // TODO test this
    public Orbit createOrbitFromPersistence(OrbitPersistence orbitPersistence) {
        PlanetPersistence planetPersistence = orbitPersistence.getPlanet();
        Planet planet = this.planetFactory.createPlanetFromPersistence(planetPersistence);
        Orbit orbit = null;
        switch( orbitPersistence.getOrbitType() ){
            case STATIC: {
                StaticOrbit staticOrbit = new StaticOrbit();
                staticOrbit.setX( orbitPersistence.getX() );
                staticOrbit.setX( orbitPersistence.getY() );
                orbit = staticOrbit;
            } break;
            case ELLIPSOID: {
                EllipsoidOrbit ellipsoidOrbit = new EllipsoidOrbit();
                ellipsoidOrbit.setTime(orbitPersistence.getTime());
                ellipsoidOrbit.setHeight(orbitPersistence.getHeight());
                ellipsoidOrbit.setWidth(orbitPersistence.getWidth());
                ellipsoidOrbit.setVelocity(orbitPersistence.getVelocity());
                orbit = ellipsoidOrbit;
            } break;
        }
        planet.getGraphics().setOrbit(orbit);
        return orbit;
    }

    /**
     * Creates a basic star system
     * @param id the ID to use, it is passed as is.
     * @param star the sun Graphics to use for the center of the star system
     * @return
     */
    private StarSystem createBasicStarSystem(String id, Planet star) {
        StarSystem starSystem = new StarSystem( star );
        StarSystemGraphics starSystemGraphics = new StarSystemGraphics( this.entityIDFactory.get(id), star.getGraphics());
        starSystemGraphics.setPosition(0,0, Align.center);
        starSystemGraphics.setModel(starSystem);
        return starSystem;
    }

    /**
     * Creates a shuffled orbit from settings. Also shuffles the time value
     * @param orbitSettings the settings to use to construct the orbit.
     * @param orbitWidth the default width of the orbit, as determined by star system
     * @param orbitHeight the default height of the orbit, as determined by star system
     * @return null if the orbit type is not supported yet.
     */
    private Orbit generateOrbitFromSettings(OrbitSettings orbitSettings, float orbitWidth, float orbitHeight) {
        switch( orbitSettings.getOrbitType() ) {
            case ELLIPSOID: {
                this.logger.log(Level.FINE, "Creating an Ellipsoid Orbit");
                EllipsoidOrbit ellipsoidOrbit = new EllipsoidOrbit();
                ellipsoidOrbit.setWidth( orbitWidth + this.numberGenerator.getRandomFloat( orbitSettings.getMaxWidthMargin(), orbitSettings.getMinWidthMargin()) );
                ellipsoidOrbit.setHeight( orbitHeight + this.numberGenerator.getRandomFloat( orbitSettings.getMaxHeightMargin(), orbitSettings.getMinHeightMargin()) );
                ellipsoidOrbit.setVelocity( this.numberGenerator.getRandomFloat( orbitSettings.getMaxVelocity(), orbitSettings.getMinVelocity()) );
                ellipsoidOrbit.setTime( this.numberGenerator.getRandomFloat(360, 0 ));
                return ellipsoidOrbit;
            }
            case STATIC: {
                this.logger.log(Level.FINE, "Creating a Static Orbit");
                StaticOrbit staticOrbit = new StaticOrbit();
                staticOrbit.setX( orbitSettings.getStaticPositionX() );
                staticOrbit.setY( orbitSettings.getStaticPositionY() );
                return staticOrbit;
            }
            default: {
                this.logger.log(Level.WARNING, "Orbit Type {0} not supported", orbitSettings.getOrbitType());
                return null;
            }
        }
    }

    /**
     * Creates Model and Graphics for a StarSystem
     * including model and graphics for all of its children.
     * @param settings
     * @return
     */
    public StarSystem generateStarSystem(StarSystemSettings settings) {
        logger.log(Level.INFO, "Generating System '" + settings.getName() +"'...");
        PlanetSettings starSettings = settings.getStarSettings();

        Planet star = this.planetFactory.generatePlanetFromSettings(starSettings);
        StarSystem starSystem = this.createBasicStarSystem(this.entityIDFactory.createID().get(), star);
        starSystem.setSettings(settings);

        List<PlanetSettings> planetProbability = settings.getPlanetProbability();
        if( planetProbability.isEmpty() ) {
            logger.log(Level.WARNING, "No planets available in star system! In some cases, this may be intended.");
        }

        // add star
        Orbit starOrbit = this.generateOrbitFromSettings(starSettings.getOrbitSettings(), 0, 0);
        star.getGraphics().setOrbit(starOrbit);
        starSystem.registerTarget( star.getGraphics() );

        this.addPlanets(starSystem, star, planetProbability, this.maxExtent);
        this.addOrbitalBelts(starSystem, settings);
        logger.log(Level.INFO, "Planets Generated this time:\n" + counter.toString());
        logger.log(Level.INFO, "Total Statistics:\n" + this.toString());
        counter.reset();
        return starSystem;
    }

    /**
     *
     * @param starSystem
     * @param planetToAdd
     * @param planetSettingList
     */
    private void addPlanets(StarSystem starSystem, Planet planetToAdd, List<PlanetSettings> planetSettingList, float maxExtent) {
        if ( planetSettingList.isEmpty()) return;;
        List<Vector2> orbitalSections = getOrbitalSections(planetToAdd, planetSettingList.size(), maxExtent);

        for( int planetCount = 0; planetCount < orbitalSections.size(); planetCount++  ) {
            PlanetSettings planetSettings = planetSettingList.get(planetCount);
            if ( this.numberGenerator.isTrue(planetSettings.getProbability()) ) {

                OrbitSettings orbitSettings = planetSettings.getOrbitSettings();
                float orbitwidth = orbitalSections.get(planetCount).x + this.numberGenerator.getRandomFloat(orbitSettings.getMaxWidthMargin(), orbitSettings.getMinWidthMargin());
                float orbitheight = orbitalSections.get(planetCount).y + this.numberGenerator.getRandomFloat(orbitSettings.getMaxHeightMargin(), orbitSettings.getMinHeightMargin() );

                // add planet around star
                Planet planet = this.planetFactory.generatePlanetFromSettings(planetSettings);
                this.countPlanetForStatistic(planet);

                Orbit orbit = this.generateOrbitFromSettings(planetSettings.getOrbitSettings(), orbitwidth, orbitheight);
                planet.getGraphics().setOrbit(orbit);

                planetToAdd.getGraphics().addActor(planet.getGraphics());
                starSystem.registerTarget(planet.getGraphics());

                // add planet satellites
                this.addPlanets(starSystem, planet, planetSettings.getSatellites(), 800);
            }
        }
    }

    /**
     *
     * @param starSystem
     * @param settings
     */
    private void addOrbitalBelts(StarSystem starSystem, StarSystemSettings settings) {
        // for now creates both model and graphics. Later: return list of belts
        List<OrbitalBeltSettings> beltSettings = settings.getBeltSettings();
        for(OrbitalBeltSettings beltSetting : beltSettings) {
            if ( this.numberGenerator.isTrue(beltSetting.getProbability()) ) {
                OrbitalBelt belt = this.planetFactory.generateOrbitalBelt(beltSetting);
                belt.setSettings(beltSetting);

                OrbitalBeltGraphics orbitalBeltGraphics = this.planetFactory.createOrbitalBeltGraphics(starSystem, belt, beltSetting);
                orbitalBeltGraphics.setOrbitalBelt(belt);
                starSystem.addBelt(belt);
                starSystem.getGraphics().addActor(orbitalBeltGraphics);
            }
        }
    }

    /**
     * Gets a list of orbital sections, constrained by the maximum
     * planet system size. Planets will be scattered over the available space.
     * @param count the amount of planets.
     * @return A list of vectors where the X is the minimum extent and Y the maximum extent.
     */
    public List<Vector2> getOrbitalSections( Planet sun, int count, float maxExtent) {
        List<Vector2> sections = new ArrayList<>();
        float orbitWidth = maxExtent / count;
        float startMin = sun.getGraphics().getPlanetScale() * sun.getGraphics().getWidth();
        float min = startMin;
        float max = startMin + orbitWidth;
        for( int i = 0; i < count; i++) {
            sections.add( new Vector2(min, max));
            min = min + orbitWidth;
            max = max + orbitWidth;
        }
        return sections;
    }

    private void countPlanetForStatistic(Planet planet) {

        Integer life = planet.getPlanetStats().getTraitCounter().get("classification_life");
        Integer temp = planet.getPlanetStats().getTraitCounter().get("classification_temperature");
        Integer weather = planet.getPlanetStats().getTraitCounter().get("classification_weather");

        this.counter.addOne( planet.getPlanetType() );
        this.globalCounter.addOne( planet.getPlanetType() );

        this.lifeLevelCounter.addOne(life);
        this.temperatureLevelCounter.addOne(temp);
        this.weatherLevelCounter.addOne(weather);
    }

    @Override
    public String toString() {
        return "StarSystemFactory {\n" +
                "== globalCounter ==" + globalCounter +
                "== lifeLevelCounter ==" + lifeLevelCounter +
                "== weatherLevelCounter ==" + weatherLevelCounter +
                "== temperatureLevelCounter ==" + temperatureLevelCounter +
                "== traitCounter ==" + planetFactory.getGeneratedTraitsInfo() +
                '}';
    }
}
