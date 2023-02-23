package com.n.a.game;

import com.n.a.XYZException;
import com.n.a.game.settings.DataPackSettings;
import com.n.a.io.XYZFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages one or more {@link DataPack}. Will always load XYZ core as default datapack and multiple
 * external datapacks (= modpacks). This class allows for removal and addition of mods.
 * This class should be treated as singleton.
 *
 * TODO Does not allow changes to the core datapack.
 */
public class DataPackManager {
    // TODO make sure find() methods work on all abstraction layers
    // TODO create tests?

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private DataPackLoader dataPackLoader = null;
    private List<DataPackSettings> datapackList = new ArrayList<>(); // TODO use a map instead?
    private transient List<DataPack> loadedDataPacks = new ArrayList<>();

    /**
     * Gets the default DataPackManager for XYZ with default file handling.
     * Contains the XYZ core datapack.
     * @return
     */
    public static DataPackManager getDefaultDataPackManager() {
        DataPackManager dataPackManager = getEmptyDataPackManager();
        DataPackLoader dataPackLoader = new DataPackLoader();
        dataPackLoader.setFileHandler( new XYZFiles() );
        dataPackManager.setDataPackLoader(dataPackLoader);
        return dataPackManager;
    }

    /**
     * Gets an Empty DataPackManager where the file handling can be customized.
     * Contains the XYZ core datapack.
     * @return A DataPackManager that uses a custom DataPackLoader (this must be set in order to work)
     */
    public static DataPackManager getEmptyDataPackManager() {
        DataPackManager dataPackManager = new DataPackManager();
        dataPackManager.addDataPack(DataPackSettings.getXYZDatapackSettings());
        return dataPackManager;
    }

    /**
     * Serialization only! Please use any of the static methods to grant
     * functionality
     */
    public DataPackManager() {
        // Serialization only!!
    }

    public DataPackLoader getDataPackLoader() {
        return dataPackLoader;
    }

    public void setDataPackLoader(DataPackLoader dataPackLoader) {
        this.dataPackLoader = dataPackLoader;
    }

    /**
     * Adds a datapack. Only use for modifying mod lists.
     * @param dataPackSettings
     */
    public void addDataPack(DataPackSettings dataPackSettings) {
        this.datapackList.add(dataPackSettings);
    }

    /**
     * Removes a datapack. Only use for modifying mod lists.
     * @param dataPackSettings
     */
    public void removeDataPack(DataPackSettings dataPackSettings) {
        this.datapackList.remove(dataPackSettings);
    }

    /**
     * Unloads all DataPacks from memory. Also disposes of the
     * texture atlas of the datapack
     */
    public void unloadAllDataPacks() {
        for( DataPack dataPack : this.loadedDataPacks) {
            dataPack.clearAllRepositories();
            dataPack.getTextureAtlas().dispose();
        }
        this.loadedDataPacks.clear();
    }

    /**
     * Sets the seed to use for all DataPacks
     * @param seed
     */
    public void setSeed(long seed) {
        for( DataPack dataPack : this.loadedDataPacks) {
            dataPack.setSeed(seed);
        }
    }

    /**
     * Loads all datapacks that have been configured to be loaded
     */
    public void loadAllDataPacks() {
        logger.log(Level.INFO, "Loading All DataPacks");
        for ( DataPackSettings settings : datapackList ) {
            DataPack dataPack = this.dataPackLoader.loadDataPack(settings);
            this.loadedDataPacks.add( dataPack );
        }
    }

    /**
     * Finds a datapack by its name
     * @param datapackName
     * @return
     */
    public DataPack findDataPack(String datapackName) {
        for( DataPack dataPack : this.loadedDataPacks) {
            if(Objects.equals(datapackName, dataPack.getName())) {
                return dataPack;
            }
        }
        return null;
    }

    /**
     * Generic resource finder that looks for data in all loaded DataPacks
     * @param dataPackName
     * @param id
     * @param <R> the type of resource
     * @return
     */
    public <R> R findInAllResources(String dataPackName, String id) {
        if( this.loadedDataPacks.isEmpty() ) {
            throw new XYZException(XYZException.ErrorCode.E0001, "DataPacks");
        }
        DataPack dataPack = findDataPack(dataPackName);
        if( dataPack == null) {
            throw new XYZException(XYZException.ErrorCode.E3100, (String) null);
        }
        return (R) dataPack.findResourceByID(id);
    }
}
