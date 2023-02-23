package com.n.a.game.settings;

import com.n.a.XYZGame;
import com.n.a.game.repository.ClassificationRepository;
import com.n.a.game.repository.ColorRepository;
import com.n.a.game.repository.DataRepository;
import com.n.a.game.repository.OrbitRepository;
import com.n.a.game.repository.PlanetArchetypeSettingsRepository;
import com.n.a.game.repository.PlanetTraitRepository;
import com.n.a.game.repository.StarSystemSettingsRepository;
import com.n.a.XYZ.game.repository.*;
import com.n.a.io.FileHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * A DataPack for gamedata. Serves as a
 * basis for core data and mods.
 * Default values = core DataPack
 */
public class DataPackSettings {

    /**
     * Internal helper class to register repositories to load
     */
    public class DataRepositorySetting {
        protected String path;
        protected Class<? extends DataRepository> clazz;

        protected DataRepositorySetting ( String path, Class<? extends DataRepository> clazz) {
            this.path = path;
            this.clazz = clazz;
        }

        public String getPath() {
            return path;
        }

        public Class<? extends DataRepository> getClazz() {
            return clazz;
        }
    }

    /** Internal or external datapack? Internal
     * means it is the core datapack of the game. For mods
     * use internal = false */
    private String name = "Untitled Datapack";
    private int version = 0;
    private FileHandler.FileLocation fileLocation = FileHandler.FileLocation.LOCAL;
    /** The list of repositories to load */
    private List<DataRepositorySetting> repositorySettingList =  new ArrayList<>();
    /** The texture atlas to use. */
    private String textureAtlas = "";
    private String uiTextureAtlas = ""; // TODO implement

    /**
     * Gets the default datapack for XYZ
     * @return
     */
    public static DataPackSettings getXYZDatapackSettings() {
        DataPackSettings settings = new DataPackSettings();
        settings.setName(XYZGame.DEFAULT_DATAPACK);
        settings.setVersion(1);
        settings.setFileLocation(FileHandler.FileLocation.LOCAL); // TODO switch to internal later
        settings.addRepository("data/colors.json", ColorRepository.class);
        settings.addRepository("data/orbits.json", OrbitRepository.class);
        settings.addRepository("data/classification.json", ClassificationRepository.class);
        settings.addRepository("data/starsystems_2.json", StarSystemSettingsRepository.class);
        settings.addRepository("data/planetTraits.json", PlanetTraitRepository.class);
        settings.addRepository("data/planetArchetypes.json", PlanetArchetypeSettingsRepository.class);
        settings.setTextureAtlas("gfx/image_atlas.atlas");
        return settings;
    }

    public void addRepository(String path, Class<? extends DataRepository> type) {
        this.repositorySettingList.add( new DataRepositorySetting(path, type));
    }

    public List<DataRepositorySetting> getRepositorySettings() {
        return repositorySettingList;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextureAtlas() {
        return textureAtlas;
    }

    public void setTextureAtlas(String textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public FileHandler.FileLocation getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(FileHandler.FileLocation fileLocation) {
        this.fileLocation = fileLocation;
    }
}
