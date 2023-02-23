package com.n.a.io.persistence;

import com.n.a.game.space.Sector;
import com.n.a.game.space.StarSystem;

public class SectorPersistence extends Persistence<Sector<StarSystem>>{

    private StarSystemPersistence starSystemPersistence;
    private int xCoordinates;
    private int yCoordinates;

    public SectorPersistence( Sector<StarSystem> sector) {
        this.starSystemPersistence = new StarSystemPersistence(sector.getChunk());
        this.xCoordinates = sector.getXCoordinates();
        this.yCoordinates = sector.getYCoordinates();
    }

    public StarSystemPersistence getStarSystemPersistence() {
        return starSystemPersistence;
    }

    public void setStarSystemPersistence(StarSystemPersistence starSystemPersistence) {
        this.starSystemPersistence = starSystemPersistence;
    }

    public int getxCoordinates() {
        return xCoordinates;
    }

    public void setxCoordinates(int xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public int getyCoordinates() {
        return yCoordinates;
    }

    public void setyCoordinates(int yCoordinates) {
        this.yCoordinates = yCoordinates;
    }
}
