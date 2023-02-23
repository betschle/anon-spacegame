package com.n.a.io.json;

import com.n.a.XYZException;
import com.n.a.game.repository.OrbitRepository;
import com.n.a.game.settings.generator.OrbitSettings;
import com.n.a.game.settings.generator.PlanetSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONToPlanetSettings
        extends AbstractJSONConverter
        implements JSONConverter<PlanetSettings, Map>{

    protected JSONToPlanetSettings(RepositoryLoader loader) {
        super(loader);
    }

    @Override
    public PlanetSettings convert(Map input) {
        OrbitRepository orbitRepository = this.getLoader().getLoadedDataRepository(OrbitRepository.class);

        PlanetSettings settings = new PlanetSettings();
        if( input.containsKey("probability") ) {
            settings.setProbability(((Number) input.get("probability")).floatValue());
        }
        settings.setArchetype( (String) input.get("archetype") );
        settings.setSubtype( (String) input.get("subtype") );

        OrbitSettings orbitSettings = orbitRepository.find((String) input.get("orbitSettings"));
        if( orbitSettings == null) throw new XYZException(XYZException.ErrorCode.E3000, "Orbit ID of " +  input.get("orbitSettings"));
        settings.setOrbitSettings( orbitSettings );

        if( input.containsKey("satellites") ) {
            List<PlanetSettings> satellites = new ArrayList<>();
            List<Map> satellitesList = (List<Map>) input.get("satellites");
            for( Map satellite : satellitesList ) {
                satellites.add( this.convert(satellite) );
            }
            settings.setSatellites(satellites);
        }

        return settings;
    }
}
