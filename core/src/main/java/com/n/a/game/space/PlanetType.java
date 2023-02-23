package com.n.a.game.space;

public enum PlanetType {
    /** like Pluto; a small moon */
    PLANEMO,
    /** Like our terrestrial moon */
    MOON,
    /** like Mercury; primitive life only */
    VOLCANIC,
    /** like Venus; primitive life only */
    TOXIC,
    /** A single asteroid. */
    ASTEROID_SMALL,
    ASTEROID_LARGE,
    PLANET_RINGS,
    /** A belt of asteroids. Always in groups! */
    ASTEROID_BELT,
    /** earth like planet with continents; capable to carry advanced life */
    CONTINENTAL,
    /** earth like planet archipelago; capable to carry advanced life, mostly inhabiting the vast oceans */
    OCEANIC,
    /** like Jupiter; has a chance to carry a ring */
    GASEOUS,
    /** Like our sun; microbial life only in the atmosphere */
    STAR,

    // Anomalies
    /** */
    NEUTRON_STAR,
    /** */
    PULSAR,
    /** */
    BLACK_HOLE,
    /** A space station */
    STATION;
}
