package com.n.a.io.json;

import com.n.a.XYZException;
import com.n.a.game.DataPack;
import com.n.a.game.repository.ColorRepository;
import com.n.a.game.repository.DataRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Generic Repository Loader that works with any
 * repositories that were configured with {@link JSONDataSource}.
 * The Repository internally keeps track of all laoded repositories so they
 * are accessible during set up. For this reason, one RepositoryLoader
 * needs to load many {@link DataPack}s. This also ensures simple
 * cross-referencing between mods or the core data pack.
 * However, the order at which data packs are loaded is extremely
 * important. The RepositoryLoader may perform additional
 * actions on certain repositories where a very basic data validation
 * is required, e.g. {@link ColorRepository}.
 * This is meant to be a singleton!
 *
 * @see JSONConverter
 * @see DataRepository
 */
public class RepositoryLoader {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private Map<Class, DataRepository> loadedRepositories = new HashMap<>();
    private Map<Class, JSONConverter> loadedConverters = new HashMap<>();

    private List<Class<? extends JSONConverter>> loadOrder = new ArrayList<>();


    /**
     * Defines the loading order of aiding {@link JSONConverter}s.
     */
    public void setLoadOrder() {
        // TODO this needs to be configured (in DataPackSettings)

        // converters
        loadOrder.add(JSONToTree.class);
        loadOrder.add(JSONToClassification.class);
        loadOrder.add(JSONToClassification.class);
        loadOrder.add(JSONToPlanetSettings.class);
        loadOrder.add(JSONToOrbitalBeltSettings.class);
        loadOrder.add(JSONToOrbitSettings.class);
        loadOrder.add(JSONToColorRepository.class);
        loadOrder.add(JSONToPlanetTextureSettings.class);
        loadOrder.add(JSONToPlanetTraits.class);
        loadOrder.add(JSONToPlanetSubtypeSettings.class);
        loadOrder.add(JSONToPlanetArchetypeSettings.class);
        // repositories
        loadOrder.add(JSONToClassificationRepository.class);
        loadOrder.add(JSONToOrbitRepository.class);
        loadOrder.add(JSONToStarSystemSettingsRepository.class);
        loadOrder.add(JSONToPlanetTraitsRepository.class);
        loadOrder.add(JSONToPlanetArchetypeSettingsRepository.class);

    }

    /**
     * Instantiate aiding converters required by instances of DataRepository. Must be called
     * prior loading the repositories themselves.
     */
    public void instantiateConverters() {
        for( Class<? extends JSONConverter> clazz : this.loadOrder) {
            this.newConverterInstance(clazz);
        }
    }
    /**
     * Gets a converter that was loaded with this loader.
     * @param clazz the class of converter
     * @param <T> the class type
     * @return
     */
    public <T extends JSONConverter> T getLoadedConverter( Class<T> clazz ) {
        return (T) this.loadedConverters.get(clazz);
    }

    /**
     * Gets a DataRepository that was loaded with this loader.
     * @param clazz the repository
     * @param <T> a class that must extend DataRepository
     * @return
     */
    public <T extends DataRepository> T getLoadedDataRepository( Class<T> clazz) {
        return (T) this.loadedRepositories.get(clazz);
    }

    /**
     * Instantiates a new Converter Instance and registers it to the loader. Also initializes
     * the converter.
     * @param clazz the class
     * @param <T> the type extending JSONConverter
     * @return
     */
    public <T extends JSONConverter> T newConverterInstance(Class<T> clazz) {
        this.logger.log(Level.FINEST, "Instantiating Converter of type " + clazz.getCanonicalName());
        T newInstance = null;
        try {
            newInstance = (T) clazz.getDeclaredConstructor( RepositoryLoader.class ).newInstance( this );
        } catch (InstantiationException e) {
            throw new XYZException(XYZException.ErrorCode.E2003, e);
        } catch (IllegalAccessException e) {
            throw new XYZException(XYZException.ErrorCode.E2003, e);
        } catch (InvocationTargetException e) {
            throw new XYZException(XYZException.ErrorCode.E2003, e);
        } catch (NoSuchMethodException e) {
            throw new XYZException(XYZException.ErrorCode.E2003, e);
        }
        this.loadedConverters.put(clazz, newInstance);
        return newInstance;
    }

    /**
     *
     * @param repository
     */
    private void registerDataRepository(DataRepository repository) {
        this.logger.log(Level.FINEST, "Registering Data Repository " + repository.getClass().getCanonicalName() );
        this.loadedRepositories.put(repository.getClass(), repository);
    }

    /**
     * Loads the text content into the repository as accessible data
     * @param dataRepository the type of Repository to use
     * @param fileTextContent a json with the content
     * @param <T> a subclass of DataRepository
     * @return an instance of DataRepository
     */
    public <T extends DataRepository> T loadRepository(Class<T> dataRepository, String fileTextContent) {
        // find out which converter to use
        JSONDataSource annotation = dataRepository.getAnnotation(JSONDataSource.class);
        if ( annotation == null ) {
            throw new XYZException(XYZException.ErrorCode.E0002,
                    "Annotation " + JSONDataSource.class.getCanonicalName() +
                    " expected in class " + dataRepository.getCanonicalName() +
                    ". Please define Annotation and JSONConverter, then retry!");
        }
        Class<? extends JSONConverter> jsonConverterClass = annotation.value();
        JSONConverter converter = (JSONConverter) this.newConverterInstance(jsonConverterClass);
        // converter.initialize(this);
        T repository = (T) converter.convert(fileTextContent);
        this.registerDataRepository(repository);
        return repository;
    }
}
