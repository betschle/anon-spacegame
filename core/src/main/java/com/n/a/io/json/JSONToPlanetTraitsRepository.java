package com.n.a.io.json;

import com.n.a.game.repository.PlanetTraitRepository;
import com.n.a.game.planet.PlanetTrait;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Converts JSON String to a {@link PlanetTraitRepository}
 */
public class JSONToPlanetTraitsRepository
        extends AbstractJSONConverter
        implements JSONConverter<PlanetTraitRepository, String> {

    protected JSONToPlanetTraitsRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public PlanetTraitRepository convert(String input) {
        int loaded = 0;
        PlanetTraitRepository traitRepository = new PlanetTraitRepository();
        JSONToPlanetTraits jsonToPlanetTraits = this.loader.getLoadedConverter(JSONToPlanetTraits.class);

        JSONArray names = new JSONArray();
        JSONArray objects = new JSONObject(input).getJSONArray("traitList");
        List<Object> traits = objects.toList();
        for( Object trait : traits ) {
            Map traitMap = (Map) trait; // in list are hashmap objects
            PlanetTrait planetTrait = jsonToPlanetTraits.convert(traitMap);
            traitRepository.add(planetTrait.getId(), planetTrait);
            if( planetTrait != null ) {
                loaded++;
            }
        }
        // TODO finish
        logger.log(Level.INFO, "Loaded {0} Planet Traits", loaded);
        return traitRepository;
    }
}
