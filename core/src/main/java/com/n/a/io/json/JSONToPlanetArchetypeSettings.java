package com.n.a.io.json;

import com.n.a.game.settings.generator.PlanetArchetypeSettings;
import com.n.a.game.settings.generator.PlanetSubtypeSettings;

import java.util.*;

public class JSONToPlanetArchetypeSettings
        extends AbstractJSONConverter
        implements JSONConverter<PlanetArchetypeSettings, Map> {

    protected JSONToPlanetArchetypeSettings( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public PlanetArchetypeSettings convert(Map input) {
        JSONToPlanetSubtypeSettings jsonToPlanetSubtypeSettings = this.getLoader().getLoadedConverter(JSONToPlanetSubtypeSettings.class);
        PlanetArchetypeSettings settings = new PlanetArchetypeSettings();
        settings.setId( (String) input.get("id"));
        settings.setName( (String) input.get("name"));
        settings.setIcon( (String) input.get("icon"));
        settings.setDiscoveryDescription( (String) input.get("discoveryDescription"));

        settings.setMinAxisTilt( ((Number) input.get("minAxisTilt")).floatValue());
        settings.setMaxAxisTilt( ((Number) input.get("maxAxisTilt")).floatValue());
        settings.setMinScale( ((Number) input.get("minScale")).floatValue());
        settings.setMaxScale( ((Number) input.get("maxScale")).floatValue());

        Map<String, PlanetSubtypeSettings> outputSubtypes = new HashMap();
        Map subtypeMap = (Map) input.get("subtypes");
        for( Object key : subtypeMap.keySet()) {
            HashMap map = (HashMap) subtypeMap.get(key);
            PlanetSubtypeSettings subTypeSettings = jsonToPlanetSubtypeSettings.convert(map);
            subTypeSettings.setName(key.toString());
            outputSubtypes.put(key.toString(), subTypeSettings);
        }
        settings.setPlanetSubtypeSettings(outputSubtypes);
        return settings;
    }
}
