package com.n.a.io.persistence;

import com.n.a.game.space.StarSystem;
import com.n.a.io.SpatialInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * An object that helps to persist {@link StarSystem}s.
 */
public class StarSystemPersistence extends Persistence<StarSystem>  {

    private PlanetPersistence sun;
    private List<OrbitPersistence> orbits = new ArrayList<>();
    private SpatialInformation spatialInformation;
    private String id;

    public StarSystemPersistence( StarSystem system ) {
        this.sun = new PlanetPersistence(system.getSun());
        this.id = system.getGraphics().getId().get();
        /*
        for( Orbit orbit : system.getTargetableActors()) {
            this.orbits.add( new OrbitPersistence( orbit));
        }
        // TODO Finish
         */
        this.spatialInformation = SpatialInformation.getInformation(system.getGraphics());
    }

    public String getId() {
        return id;
    }

    public PlanetPersistence getSun() {
        return sun;
    }

    public List<OrbitPersistence> getOrbits() {
        return orbits;
    }

    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }
}
