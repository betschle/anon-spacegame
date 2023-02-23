package com.n.a.game.repository;

import com.n.a.game.space.PlanetFeature;
import com.n.a.io.json.JSONToPlanetFeatureRepository;
import com.n.a.io.json.JSONDataSource;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

@JSONDataSource(JSONToPlanetFeatureRepository.class)
@Deprecated
public class PlanetFeatureRepository extends GenericRepository<PlanetFeature> {
}
