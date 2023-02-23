package com.n.a.game.repository;

import com.n.a.game.planet.Classification;
import com.n.a.io.json.JSONDataSource;
import com.n.a.io.json.JSONToClassificationRepository;

@JSONDataSource(JSONToClassificationRepository.class)
public class ClassificationRepository extends GenericRepository<Classification> {
}
