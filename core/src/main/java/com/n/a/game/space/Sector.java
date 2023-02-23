package com.n.a.game.space;

import java.io.Serializable;

/**
 * Sectors contain celestial objects, entities and other information
 * meant to be serialized.
 * <ul>
 * <li> A Sector is part of a larger, infinitely scrolling map (comparable to chunks in Minecraft) </li>
 * <li> Multiple sectors bundled together form a map</li>
 * <li> Idle sectors are stored/cached on disk</li>
 * </ul>
 * @param <C> the Map chunk type to save, containing world information. This is an element
 *           chunk, or part of a much larger, global map.
 */
public class Sector<C> implements Serializable {

    private static final long serialVersionUID = 2405172041950251807L;
    protected int xCoordinates;
    protected int yCoordinates;

    protected C chunk;

    public void setCoordinates( int xCoords, int YCoords) {
        this.yCoordinates = YCoords;
        this.xCoordinates = xCoords;
    }

    public void setChunk(C map) {
        this.chunk = map;
    }

    public C getChunk() {
        return chunk;
    }

    public int getXCoordinates() {
        return xCoordinates;
    }

    public void setXCoordinates(int xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public int getYCoordinates() {
        return yCoordinates;
    }

    public void setYCoordinates(int yCoordinates) {
        this.yCoordinates = yCoordinates;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "xCoordinates=" + xCoordinates +
                ", yCoordinates=" + yCoordinates +
                ", chunk=" + chunk +
                '}';
    }

    public static String getSectorName(int x, int y) {
        return "sector_"+ x + "_" + y;
    }
}
