package com.n.a.game.repository;

import com.n.a.game.planet.PlanetTrait;
import com.n.a.io.json.JSONDataSource;
import com.n.a.io.json.JSONToPlanetTraitsRepository;

@JSONDataSource(JSONToPlanetTraitsRepository.class)
public class PlanetTraitRepository extends GenericRepository<PlanetTrait> {
}
