package com.n.a.io.json;

import com.n.a.game.repository.DataRepository;
import com.n.a.game.repository.StarSystemSettingsRepository;
import com.n.a.game.settings.generator.OrbitalBeltSettings;
import com.n.a.game.settings.generator.PlanetSettings;
import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.util.Probability;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * A JSON Converter that parses JSON Data into a {@link DataRepository} of {@link StarSystemSettings}.
 *
 * @see JSONConverter
 * @see JSONDataSource
 */
public class JSONToStarSystemSettingsRepository
        extends AbstractJSONConverter
        implements JSONRepositoryConverter<StarSystemSettingsRepository, String> {

    protected JSONToStarSystemSettingsRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public void convertAndStore(StarSystemSettingsRepository repositoryStore, String input) {
        // the json is expected to be a map that contains multiple starsystem entries
        int loaded = 0;
        JSONToPlanetSettings jsonToPlanetSettings = getLoader().getLoadedConverter(JSONToPlanetSettings.class);
        JSONToOrbitalBeltSettings jsonToOrbitalBeltSettings = getLoader().getLoadedConverter(JSONToOrbitalBeltSettings.class);

        JSONObject systemJSON = new JSONObject(input);
        for(Map.Entry<String, Object> entry : systemJSON.toMap().entrySet()){
            Map starSystemMap = (Map) entry.getValue();
            String name = (String) starSystemMap.get("name");
            PlanetSettings star = jsonToPlanetSettings.convert((Map) starSystemMap.get("star"));
            Number minPlanetSpeed = (Number) starSystemMap.get("minPlanetSpeed");
            Number maxPlanetSpeed = (Number) starSystemMap.get("maxPlanetSpeed");
            Number spacing = (Number) starSystemMap.get("spacing");
            // validate that key == object name
            if( !Objects.equals(name, entry.getKey())) {
                this.logger.severe("Error creating StarSystem because StarSystem.name "+ name+ " does not match Map-key '"+entry.getKey()+"'. System is ignored!");
                continue; // skip
            }
            List<Map> planetSettings = (List<Map>) starSystemMap.get("planets");
            if( planetSettings == null) {
                this.logger.log(Level.WARNING, "No Planet Settings specified in StarSystem: " + name);
            }
            List<PlanetSettings> planetSettingsList = new ArrayList<>();
            for( Map planetSetting : planetSettings) {
                PlanetSettings planetSettingsBO = jsonToPlanetSettings.convert(planetSetting);
                planetSettingsList.add(planetSettingsBO);
            }

            List<Map> beltSettings = (List<Map>) starSystemMap.get("beltSettings");
            if( beltSettings == null) {
                this.logger.log(Level.WARNING, "No Belt Settings specified in StarSystem: " + name);
            }
            List<OrbitalBeltSettings> beltSettingsList = new ArrayList<>();
            for( Map beltSetting : beltSettings) {
                OrbitalBeltSettings orbitalBeltSettings = jsonToOrbitalBeltSettings.convert(beltSetting);
                beltSettingsList.add(orbitalBeltSettings);
            }

            StarSystemSettings systemSettings = new StarSystemSettings();
            systemSettings.setName(name);
            systemSettings.setStarSettings(star);
            systemSettings.setMaxPlanetSpeed( minPlanetSpeed.floatValue() );
            systemSettings.setMaxPlanetSpeed( maxPlanetSpeed.floatValue() );
            systemSettings.setSpacing(spacing.floatValue());
            systemSettings.setPlanetSettings( planetSettingsList );
            systemSettings.setBeltSettings(beltSettingsList);

            repositoryStore.add(systemSettings.getName(), systemSettings);
            loaded++;
        }
        this.logger.log(Level.INFO, "Loaded {0} Star Systems", loaded);
    }


    @Override
    public StarSystemSettingsRepository convert(String json) {
        StarSystemSettingsRepository repository = new StarSystemSettingsRepository();
        this.convertAndStore(repository, json);
        return repository;
    }

    private List<Probability> jsonToProbability(List<Map> jsonProbabilities) {
        List<Probability> probabilities = new ArrayList<>();
        for( Map probabilityMap : jsonProbabilities) {
            Number weight = (Number) probabilityMap.get("weight");
            String mainType =( String) probabilityMap.get("type");
            String subType = (String) probabilityMap.get("subtype");

            Probability probability = new Probability();
            probability.setWeight(weight.floatValue());
            probability.setType(mainType);
            probability.setSubtype(subType);

            probabilities.add(probability);
        }
        return probabilities;
    }

}
