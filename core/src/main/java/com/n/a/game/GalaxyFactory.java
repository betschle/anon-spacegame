package com.n.a.game;

import com.n.a.game.space.Galaxy;
import com.n.a.game.space.GalaxyMask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Galaxy Factory creates Galaxy BOs
 */
public class GalaxyFactory {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private DataPack dataPack;

    public GalaxyFactory(DataPack dataPack) {
        this.dataPack = dataPack;
    }


    // TODO use GalaxySettings
    // "env/galaxy_spiral"
    public Galaxy createGalaxy(String galaxyMaskTexture) {
        logger.log(Level.WARNING, "Creating a new Galaxy with mask: " + galaxyMaskTexture);
        Galaxy galaxy = new Galaxy();
        EntityID id = dataPack.getEntityIDFactory().createID(galaxy.getClass().getCanonicalName());
        GalaxyMask galaxyMask = new GalaxyMask( dataPack.getTextureAtlas().findRegion(galaxyMaskTexture));

        galaxy.setId(id);
        galaxy.setName( dataPack.getGalaxyNameRandomizer().generateName() );
        galaxy.setGalaxyMask(galaxyMask);

        logger.log(Level.WARNING, "Galaxy Created: " + galaxy);

        return galaxy;
    }



}
