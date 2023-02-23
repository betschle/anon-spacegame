package com.n.a.world;

import com.n.a.game.space.Sector;
import com.n.a.game.space.SectorGenerator;
import com.n.a.io.persistence.Persistence;

public class DummySectorGenerator implements SectorGenerator<String> {

    @Override
    public Sector<String> generateSector(int worldX, int worldY) {
        Sector<String> sector = new Sector<>();
        sector.setChunk(worldX+"_"+worldY);
        sector.setCoordinates(worldX, worldY);
        return sector;
    }

    @Override
    public Persistence<Sector<String>> createPersistence(Sector<String> sector) {
        return null;
    }

    @Override
    public Sector<String> createSector(Persistence<Sector<String>> sectorPersistence) {
        return null;
    }
}
