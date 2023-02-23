package com.n.a.game.planet;

/**
 * Weather classification for Planets.
 * @deprecated
 */
@Deprecated
public enum WeatherClass {

    /** No gaseous matter present. Nothing to see here. */
    NONE,
    /** Thin Atmosphere. Dust storms, limited rainfall. */
    THIN,
    /** Normal, earth-like atmosphere with occasional
     * hurricanes and tornadoes */
    NORMAL,
    /** Severe rain & erosion, thunderstorms. Frequent
     * hurricanes, tornadoes or monsoons. Allows for
     * existence of life, but it is hard
     * on vegetation and biodiversity.  */
    STORMY,
    /** Regular storms of +200km/h speed. Does not
     * allow plant life to grow. */
    EXTREME,
    /** Searing-hot, intense particle storms. */
    SOLAR;
}
