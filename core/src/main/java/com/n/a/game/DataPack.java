package com.n.a.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.n.a.game.repository.DataRepository;
import com.n.a.game.settings.DataPackSettings;
import com.n.a.gfx.GraphicsAssembler;
import com.n.a.util.NameRandomizer;
import com.n.a.util.sequences.NumberGenerator;
import com.n.a.util.sequences.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A basic datapack for gamedata. Made for pack specific colors, texture atlases
 * and other data to feed the procedural generator with. Only one instance of a
 * DataRepository-type can be stored in a DataPack, they are like singletons within the
 * scope of a DataPack. Packs also have their factory objects and are strictly disconnected
 * from another, meaning they do not share a common pool of resources. When creating a new world,
 * it's vital that a new seed of this new world is being set correctly.
 *
 * @see DataPackManager
 * @see DataPackSettings
 */
public class DataPack {

    private String name;
    private int version = 0;
    private List<DataRepository<String, ?>> repositories = new ArrayList<>();
    private TextureAtlas textureAtlas;

    private NumberGenerator numberGenerator;
    private NameRandomizer planetNameRandomizer;
    private NameRandomizer galaxyNameRandomizer;
    private StringGenerator stringGenerator;
    private EntityIDFactory entityIDFactory;
    private GraphicsAssembler graphicsAssembler;
    private PlanetFactory planetFactory;
    private StarSystemFactory starSystemFactory;
    private SpaceShipFactory spaceShipFactory;
    private GalaxyFactory galaxyFactory;


    public DataPack() {
        // for serialization
    }

    /**
     * Initializes the DataPack's generators and factories.
     * Only to be called once!
     */
    public void init() {
        // TODO outsource ? make configurable?
        if ( this.numberGenerator == null) {
            this.numberGenerator = new NumberGenerator(0);

            this.planetNameRandomizer = new NameRandomizer();
            this.planetNameRandomizer.setNumberGenerator(this.numberGenerator);

            this.galaxyNameRandomizer = new NameRandomizer();
            this.galaxyNameRandomizer.setNumberGenerator(this.numberGenerator);

            this.stringGenerator = new StringGenerator(this.numberGenerator);
            this.entityIDFactory = new EntityIDFactory(this.stringGenerator);
            this.graphicsAssembler = new GraphicsAssembler(this.textureAtlas, this.numberGenerator, this.entityIDFactory);
            this.planetFactory = new PlanetFactory(this);
            this.starSystemFactory = new StarSystemFactory(this);
            this.spaceShipFactory = new SpaceShipFactory(this);
            this.galaxyFactory = new GalaxyFactory(this);

            // TODO make a name repository out of this, where each name pattern in the repository has its own name randomizer

            this.galaxyNameRandomizer.setUseSpacing(true);
            this.galaxyNameRandomizer.setFirstSet( Arrays.asList("Galaxia","Cosmica", "Canis", "Taurus", "Ursa", "Pegasus", "Orion", "Pegasus", "Aries", "Vulpus", "Lyra", "Pisces", "Hercules",
                    "Corvus", "Scorpius", "Draco", "Cepheus", "Hydra", "Serpens", "Lupus", "Corona", "Vela", "Cygnus", "Lacerta",
                    "Fenestra", "Ferrum", "Iridium", "Cuprum", "Carina", "Grus", "Spatula", "Polis", "Musica", "Mergana", "Libertad",
                    "Usted", "Angus") );
            this.galaxyNameRandomizer.setSecondSet( Arrays.asList("", "", "Major", "Minor", "Primus", "Secundus", "Tertius", "Borealis", "Galacticus" ) );

            // These are names for planets
            this.planetNameRandomizer.setUseSpacing(true);
            this.planetNameRandomizer.setFirstSet( Arrays.asList("Alpha", "Beta", "Gamma", "Delta", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda",
                    "Omicron", "Rho", "Sigma", "Tau", "Epsilon", "Omega") );
            this.planetNameRandomizer.setSecondSet( Arrays.asList("Fantasia", "Hyperion", "Canis", "Taurus", "Ursa", "Pegasus", "Orion", "Pegasus", "Aries", "Vulpus", "Lyra", "Pisces", "Hercules",
                    "Corvus", "Scorpius", "Draco", "Cepheus", "Hydra", "Serpens", "Lupus", "Corona", "Vela", "Cygnus", "Lacerta",
                    "Fenestra", "Ferrum", "Iridium", "Cuprum", "Carina", "Grus", "Spatula", "Polis", "Musica", "Mergana", "Libertad",
                    "Usted", "Angus") );
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public GalaxyFactory getGalaxyFactory() {
        return galaxyFactory;
    }

    public void setGalaxyFactory(GalaxyFactory galaxyFactory) {
        this.galaxyFactory = galaxyFactory;
    }

    public NameRandomizer getGalaxyNameRandomizer() {
        return galaxyNameRandomizer;
    }

    public void setGalaxyNameRandomizer(NameRandomizer galaxyNameRandomizer) {
        this.galaxyNameRandomizer = galaxyNameRandomizer;
    }

    public NameRandomizer getPlanetNameRandomizer() {
        return planetNameRandomizer;
    }

    public void setPlanetNameRandomizer(NameRandomizer planetNameRandomizer) {
        this.planetNameRandomizer = planetNameRandomizer;
    }

    public PlanetFactory getPlanetFactory() {
        return planetFactory;
    }

    public NumberGenerator getNumberGenerator() {
        return numberGenerator;
    }

    public StringGenerator getStringGenerator() {
        return stringGenerator;
    }

    public EntityIDFactory getEntityIDFactory() {
        return entityIDFactory;
    }

    public GraphicsAssembler getGraphicsAssembler() {
        return graphicsAssembler;
    }

    public StarSystemFactory getStarSystemFactory() {
        return starSystemFactory;
    }

    public SpaceShipFactory getSpaceShipFactory() {
        return spaceShipFactory;
    }

    /**
     * Sets the seed for the procedural generation. Will affect
     * how all shuffled data is generated.
     * @param seed the seed to use
     */
    public void setSeed( long seed) {
        this.numberGenerator.setSeed(seed);
    }

    /**
     *
     * @param repository
     */
    public void addRepository(DataRepository<String, ?> repository) {
        this.repositories.add(repository);
    }

    /**
     * Gets a repository
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends DataRepository> T getRepository(Class<T> clazz) {
        for( DataRepository<?,?> dataRepository : repositories ) {
            if( clazz.isInstance(dataRepository) ) return (T) dataRepository;
        }
        return null;
    }

    /**
     * Clears the data inside of all repositories, then
     * clears the internal list of repositories of this object.
     */
    protected void clearAllRepositories() {
        for( DataRepository<?,?> dataRepository : repositories ) {
            dataRepository.clear();
        }
        repositories.clear();
    }

    /**
     * Finds a resource inside this datapack. Works for
     * ColorSet, TextureAtlas and StarSystemSettings.
     * For PlanetGraphicsSettings a different method must be used.
     * @param id the ID of the resource.
     * @return A resource (e.g. datamodel, texture).
     * <code>null</code> if none was found
     */
    public <T> T findResourceByID(String id) {
        for(DataRepository<String,?> repository : this.repositories) {
            if( repository.contains(id) ) return (T) repository.find(id);
        }
        return null;
    }

    /**
     * Finds a resource inside this datapack using
     * an enum as ID.
     * @param enumID enum.name() is used as ID.
     * @return A resource (e.g. datamodel, texture).
     * <code>null</code> if none was found
     */
    public Object findResourceByID(Enum<?> enumID) {
        return findResourceByID(enumID.name());
    }

}
