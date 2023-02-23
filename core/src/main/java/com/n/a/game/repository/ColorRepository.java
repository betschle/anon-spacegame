package com.n.a.game.repository;

import com.badlogic.gdx.graphics.Color;
import com.n.a.io.json.JSONDataSource;
import com.n.a.io.json.JSONToColorRepository;

/**
 *
 */
@JSONDataSource(JSONToColorRepository.class)
public class ColorRepository extends GenericRepository<Color> {

}
