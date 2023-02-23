package com.n.a.io.json;

import com.n.a.game.space.PlanetFeature;
import com.n.a.game.space.ScanCategory;
import com.n.a.game.repository.PlanetFeatureRepository;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converts a JSON text to a {@link PlanetFeatureRepository}
 */
public class JSONToPlanetFeatureRepository
        extends AbstractJSONConverter
        implements JSONConverter<PlanetFeatureRepository, String> {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    protected JSONToPlanetFeatureRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public PlanetFeatureRepository convert(String json) {
        int loaded = 0;
        PlanetFeatureRepository planetFeatureRepository = new PlanetFeatureRepository();
        Map<String, Object> featuresMap = new JSONObject(json).toMap();
        for(  String featureKey : featuresMap.keySet() ) {
            PlanetFeature feature = new PlanetFeature();
            feature.setId(featureKey);

            Map singleFeatureMap = (Map) featuresMap.get(featureKey);
            ScanCategory category = ScanCategory.valueOf((String) singleFeatureMap.get("category"));
            feature.setName( (String) singleFeatureMap.get("name"));
            feature.setCategory( category );
            // TODO texture settings
            // feature.setPlanetGraphicsSettings( );
            feature.setScanDescription( (String) singleFeatureMap.get("scanDescription"));
            feature.setDiscoveryDescription( (String) singleFeatureMap.get("discoveryDescription"));
            feature.setDiscoveryPoints( ((Number) singleFeatureMap.get("discoveryPoints")).intValue());
            feature.setDiscoveryLevel( ((Number) singleFeatureMap.get("discoveryLevel")).intValue());

            planetFeatureRepository.add(feature.getId(), feature);
            loaded++;
        }
        logger.log(Level.INFO, "Loaded {0} Planet Features", loaded);
        return planetFeatureRepository;
    }
}
