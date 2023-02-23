package com.n.a.game.planet;

/**
 * Temperature classification for Planets.
 * @deprecated
 */
@Deprecated
public enum TemperatureClass {

    /** Temperature too low to form life other than microbial life. */
    EXTREMELY_COLD,
    /** Life is possible at low temperatures, but not thriving. */
    COLD,
    /** The Base Level defines the temperature at which Life thrives. */
    NORMAL,
    /** Life is possible at hot temperatures, but not thriving. */
    HOT,
    /** Temperature too high to form life other than microbial life. */
    EXTREMELY_HOT;
}
