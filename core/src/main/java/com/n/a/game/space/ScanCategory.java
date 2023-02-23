package com.n.a.game.space;

/**
    TODO Inside the json file, planet features need to be grouped by
    planet type. Each planet type has its own set of features

    TODO there is also leveling/hierarchy of features, such as the presence of life, e.g.:
            Life 1: microbiotic
            Life 2: plants
            Life 3: animals
            Life 4: intelligent
            Life 5: space-faring
          How do I represent that in code?
     TODO needs something like public PlanetGraphics GraphicsAssembler.createPlanet(Features[] features)
 */
public enum ScanCategory {
    /** Composition of Atmosphere, Climate and related */
    ATMOSPHERIC,
    /** Bodies of water, if available */
    HYDROSPHERIC,
    /** Composition of the surface, volcanic activity, rocks, geographic features */
    LITHOSPERIC,
    /** */
    BIOLOGIC,
    /** Spatial scan type (position or rotation) */
    SPATIAL;

}
