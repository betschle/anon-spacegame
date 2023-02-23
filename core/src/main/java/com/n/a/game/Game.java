package com.n.a.game;

import com.n.a.game.space.SectorGenerator;
import com.n.a.game.space.SectorManager;
import com.n.a.game.settings.DataPackSettings;
import com.n.a.game.space.Galaxy;
import com.n.a.game.space.StarSystem;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains all the classes required to initialize
 * and maintain a game. Groups together datamanager and
 * sector manager
 */
public class Game {

    // private GalaxyGenerator galaxyGenerator;
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    /** The seed to use for the world */
    private int seed;
    private String name;
    private String path;
    private Player player; // TODO move to here
    private Galaxy galaxy; // TODO move to here
    private SectorGenerator<StarSystem> sectorGenerator; // TODO move to here

    private transient DataPackManager dataPackManager = DataPackManager.getDefaultDataPackManager();
    private transient SectorManager<StarSystem> sectorManager = new SectorManager<>();

    public static Game get(String name, int seed) {
        Game world = new Game();
        world.setName(name);
        world.setSeed(seed);
        world.setPath( name.trim() ); // TODO verify there are no special characters here
        return world;
    }

    /**
     * Serialization only, use {@link Game#get(String, int)} instead!
     */
    public Game() {
    }

    /**
     * Creates a new world. This loads *all* assets required for
     * the core {@link DataPack} plus any number of modpacks. This
     * includes data and texture atlases. The Packs are managed by
     * {@link DataPackManager}, in a way the loaded assets
     * are ready to use in world generation.
     * @param mods can be null.
     */
    public void createNewWorld(List<DataPackSettings> mods) {
        logger.log(Level.INFO, "Creating new World \"{0}\", seed: {1}", new Object[]{ this.getName(), this.getSeed() });
        if( mods != null) {
            for( DataPackSettings mod : mods) {
                logger.log(Level.INFO, "Adding Mod: {0}", mod.getName());
                this.dataPackManager.addDataPack(mod);
            }
        }
        this.dataPackManager.loadAllDataPacks();
        this.dataPackManager.setSeed(this.seed);
        // TODO setup sector manager, create galaxy here
    }

    /**
     * Unloads all previously loaded datapacks. World needs to be
     * re-initialized after invoking this method.
     */
    public void unload() {
        this.dataPackManager.unloadAllDataPacks();
    }

    public DataPackManager getDataPackManager() {
        return dataPackManager;
    }

    public SectorManager<StarSystem> getSectorManager() {
        return sectorManager;
    }

    public int getSeed() {
        return seed;
    }

    protected void setSeed(int seed) {
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }
}
