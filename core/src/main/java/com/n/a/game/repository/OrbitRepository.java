package com.n.a.game.repository;

import com.n.a.game.settings.generator.OrbitSettings;
import com.n.a.io.json.JSONDataSource;
import com.n.a.io.json.JSONToOrbitRepository;

@JSONDataSource(JSONToOrbitRepository.class)
public class OrbitRepository extends GenericRepository<OrbitSettings> {
}
