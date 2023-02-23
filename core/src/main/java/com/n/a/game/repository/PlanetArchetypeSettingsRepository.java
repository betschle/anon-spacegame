package com.n.a.game.repository;

import com.n.a.game.settings.generator.PlanetArchetypeSettings;
import com.n.a.io.json.JSONDataSource;
import com.n.a.io.json.JSONToPlanetArchetypeSettingsRepository;
import com.n.a.io.json.*;

@JSONDataSource(JSONToPlanetArchetypeSettingsRepository.class)
public class PlanetArchetypeSettingsRepository extends GenericRepository<PlanetArchetypeSettings> {

}
