package com.n.a.game.repository;

import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.io.json.JSONDataSource;
import com.n.a.io.json.JSONToStarSystemSettingsRepository;

@JSONDataSource(JSONToStarSystemSettingsRepository.class)
public class StarSystemSettingsRepository extends GenericRepository<StarSystemSettings> {
}
