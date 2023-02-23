package com.n.a.game.space;

import com.n.a.io.persistence.Persistence;

/**
 * A Sector Generator. Creates a Sector Wrapper for a chunk type.
 * When persisting, wraps the chunk type into a persistent object and back.
 * @param <C> the chunk type to generate
 */
public interface SectorGenerator<C> {

    /**
     * Creates a new, functioning sector using chunk data
     * @param worldX
     * @param worldY
     */
    Sector<C> generateSector(int worldX, int worldY);

    /**
     * Creates a persistence object made from a pre-existing sector.
     * Used when saving.
     * @param sector
     * @return
     */
    Persistence<Sector<C>> createPersistence(Sector<C> sector);

    /**
     * Converts a persistence object back to a sector.
     * Used when loading.
     * @param sectorPersistence
     * @return
     */
    Sector<C>createSector(Persistence<Sector<C>> sectorPersistence);
}
