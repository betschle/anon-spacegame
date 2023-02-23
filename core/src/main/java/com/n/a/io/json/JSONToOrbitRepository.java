package com.n.a.io.json;

import com.n.a.game.repository.OrbitRepository;
import com.n.a.game.settings.generator.OrbitSettings;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;

public class JSONToOrbitRepository
        extends AbstractJSONConverter
        implements JSONRepositoryConverter<OrbitRepository, String>{

    protected JSONToOrbitRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public void convertAndStore(OrbitRepository repositoryStore, String input) {
        int loaded = 0;
        JSONToOrbitSettings orbitConverter = getLoader().getLoadedConverter(JSONToOrbitSettings.class);
        JSONObject orbitsJSON = new JSONObject(input);
        Map<String, Object> orbitsMap = orbitsJSON.toMap();
        for(  String orbitID : orbitsMap.keySet() ) {
            Map parameters = (Map) orbitsMap.get(orbitID);
            OrbitSettings orbitSettings = orbitConverter.convert(parameters);
            repositoryStore.add(orbitID, orbitSettings);
            loaded++;
        }
        this.logger.log(Level.INFO, "Loaded {0} Orbits", loaded);
    }

    @Override
    public OrbitRepository convert(String input) {
        OrbitRepository orbitRepository = new OrbitRepository();
        this.convertAndStore(orbitRepository, input);
        return orbitRepository;
    }
}
