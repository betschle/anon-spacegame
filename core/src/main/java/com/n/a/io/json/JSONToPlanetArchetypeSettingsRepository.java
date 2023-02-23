package com.n.a.io.json;

import com.n.a.game.repository.PlanetArchetypeSettingsRepository;
import com.n.a.game.settings.generator.PlanetArchetypeSettings;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;

public class JSONToPlanetArchetypeSettingsRepository
        extends AbstractJSONConverter
        implements JSONConverter<PlanetArchetypeSettingsRepository, String> {

    protected JSONToPlanetArchetypeSettingsRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public PlanetArchetypeSettingsRepository convert(String input) {
        int loaded = 0;
        JSONToPlanetArchetypeSettings jsonToPlanetArchetypeSettings = this.loader.getLoadedConverter( JSONToPlanetArchetypeSettings.class );
        PlanetArchetypeSettingsRepository repository = new PlanetArchetypeSettingsRepository();
        Map<String, Object> archetypeMap = new JSONObject(input).toMap();

        for(  String archetypeName : archetypeMap.keySet() ) {
            Map archetypeProperties = (Map) archetypeMap.get(archetypeName);
            PlanetArchetypeSettings settings = jsonToPlanetArchetypeSettings.convert(archetypeProperties);
            if ( settings != null) {
                repository.add(settings.getId(), settings);
                loaded++;
            } else {
              logger.log(Level.WARNING, "Could not load planet archetype: " + archetypeMap );
            }
        }
        logger.log(Level.INFO, "Loaded {0} Planet Archetype Settings", loaded);
        return repository;
    }
}
