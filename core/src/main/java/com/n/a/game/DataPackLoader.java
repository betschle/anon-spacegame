package com.n.a.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.n.a.XYZException;
import com.n.a.game.repository.DataRepository;
import com.n.a.game.repository.PlanetArchetypeSettingsRepository;
import com.n.a.game.repository.PlanetTraitRepository;
import com.n.a.game.settings.DataPackSettings;
import com.n.a.io.XYZFiles;
import com.n.a.io.FileHandler;
import com.n.a.io.JSONFactory;
import com.n.a.io.json.RepositoryLoader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads DataPacks from disk together with all the assets this pack contains.
 * So far such assets are ColorSets, PlanetGraphicsSettings, TextureAtlas and StarSystemSettings.
 * This class should be treated as singleton.
 */
public class DataPackLoader {

    Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private FileHandler fileHandler = null;
    private JSONFactory jsonFactory = new JSONFactory();
    private RepositoryLoader repositoryLoader = new RepositoryLoader();

    public DataPackLoader() {

    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * Loads all assets of a datapack
     * @param dataPackSettings the datapack settings to use
     * @return a datapack with all of its assets
     */
    public DataPack loadDataPack(DataPackSettings dataPackSettings) {
        logger.log(Level.INFO, "Loading Datapack: " + dataPackSettings.getName());
        DataPack dataPack = new DataPack();
        dataPack.setVersion(dataPackSettings.getVersion());
        dataPack.setName(dataPackSettings.getName());
        dataPack.setTextureAtlas( loadTextureAtlas( dataPackSettings.getTextureAtlas(), dataPackSettings.getFileLocation()));
        loadRepositories(dataPack, dataPackSettings);
        dataPack.init();
        dataPack.getPlanetFactory().setPlanetTraitRepository( dataPack.getRepository(PlanetTraitRepository.class) );
        dataPack.getPlanetFactory().setPlanetArchetypeSettingsRepository( dataPack.getRepository(PlanetArchetypeSettingsRepository.class) );
        return dataPack;
    }

    /**
     * Load all repositories
     * @param pack
     * @param fromSettings
     */
    private void loadRepositories(DataPack pack, DataPackSettings fromSettings ) {
        this.repositoryLoader.setLoadOrder();
        this.repositoryLoader.instantiateConverters();
        for(DataPackSettings.DataRepositorySetting setting : fromSettings.getRepositorySettings() ) {
            pack.addRepository( this.loadRepository(setting, fromSettings.getFileLocation() ));
        }
    }

    /**
     * Loads a single repository
     * @param setting
     * @param location
     * @return
     */
    private DataRepository loadRepository(DataPackSettings.DataRepositorySetting setting, FileHandler.FileLocation location) {
        logger.log(Level.FINE, "Loading type: '" +  setting.getClazz() + "' from path '" + setting.getPath() + "' (" + location+")");
        StringBuilder builder = null;
        try {
            builder = fileHandler.readFileContents(setting.getPath(), location);
        } catch (GdxRuntimeException | IOException e) {
            throw new XYZException(XYZException.ErrorCode.E1010, setting.getClazz().getCanonicalName(), e);
        }
        return  repositoryLoader.loadRepository(setting.getClazz(), builder.toString());
    }

    public TextureAtlas loadTextureAtlas(String filename, FileHandler.FileLocation location) {
        logger.log(Level.FINE, "Loading Texture Atlas: " + filename + " location: " + location);
        FileHandle fileHandle = XYZFiles.toGdxFileHandle(filename, location);
        TextureAtlas textureAtlas = new TextureAtlas(fileHandle);
        return textureAtlas;
    }
}
