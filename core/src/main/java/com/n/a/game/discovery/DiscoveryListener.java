package com.n.a.game.discovery;

import com.n.a.game.space.Planet;
import com.n.a.game.planet.PlanetTrait;
import com.n.a.gfx.PlanetGraphics;

/**
 * A listener for planet scanners that creates events on discovery.
 */
public interface DiscoveryListener {

    void onPlanetTraitDiscovered(Planet planet, PlanetTrait planetTrait);
    void onPlanetDiscovered(PlanetGraphics planet);
}
