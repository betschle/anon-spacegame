package com.n.a.io.json;

import com.n.a.game.settings.generator.OrbitalBeltSettings;
import com.n.a.game.settings.generator.PlanetSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONToOrbitalBeltSettings
        extends AbstractJSONConverter
        implements JSONConverter<OrbitalBeltSettings, Map> {

    public JSONToOrbitalBeltSettings(RepositoryLoader loader) {
        super(loader);
    }

    @Override
    public OrbitalBeltSettings convert(Map input) {
        JSONToPlanetSettings jsonToPlanetSettings = this.loader.getLoadedConverter(JSONToPlanetSettings.class);
        OrbitalBeltSettings settings = new OrbitalBeltSettings();
        settings.setProbability( ((Number) input.get("probability")).floatValue() );
        settings.setDensity( ((Number) input.get("density")).floatValue() );
        settings.setChunkDensity( ((Number) input.get("chunkDensity")).floatValue() );
        settings.setOrbitMinRadius( ((Number) input.get("orbitMinRadius")).floatValue() );
        settings.setOrbitMaxRadius( ((Number) input.get("orbitMaxRadius")).floatValue() );

        List<PlanetSettings> planetSettingsList = new ArrayList<>();
        List subtypesMap = (List) input.get("allowedSubTypes");
        for( Object planetTypeMap : subtypesMap) {
            planetSettingsList.add( jsonToPlanetSettings.convert( (Map) planetTypeMap ));
        }
        settings.setAllowedSubtypes( planetSettingsList );
        settings.setChunkNames((List) input.get("chunkNames"));
        settings.setColors((List) input.get("colors"));

        return settings;
    }
}
