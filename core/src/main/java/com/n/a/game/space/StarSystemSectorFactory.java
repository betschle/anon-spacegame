package com.n.a.game.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.n.a.game.DataPack;
import com.n.a.game.repository.StarSystemSettingsRepository;
import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.io.persistence.Persistence;
import com.n.a.io.persistence.SectorPersistence;
import com.n.a.util.Counter;
import com.n.a.util.sequences.NumberGenerator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates StarSystem Sectors for galaxies. Also determines the rules
 * by which a galaxy is generated sector-wise, with integrated caching mechanism.
 * This class is both a factory and implements the sector generator interface.
 *
 * TODO is this a good idea?? both factory and sector generator pattern is mixed here
 * TODO rename to GalaxyFactory??
 */
public class StarSystemSectorFactory implements SectorGenerator<StarSystem> {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    // TODO need galaxySettings to create a table of starsystems and their probabilities
    // TODO move this to Datapack?
    private Counter<String> starSystemCounter = new Counter<>();
    private Galaxy galaxy;
    private DataPack dataPack;
    private NumberGenerator numberGenerator;

    public StarSystemSectorFactory() {

    }

    public NumberGenerator getNumberGenerator() {
        return numberGenerator;
    }

    public void setNumberGenerator(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public DataPack getDataPack() {
        return dataPack;
    }

    public void setDataPack(DataPack dataPack) {
        this.dataPack = dataPack;
    }

    public Galaxy getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    @Override
    public Sector<StarSystem> generateSector(int worldX, int worldY) {
        logger.log(Level.INFO, "Generating Sector @ " + worldX + " | " + worldY);
        Vector2 centerCoordinates = this.galaxy.getGalaxyMask().centerCoordinates(worldX, worldY);
        float probability = this.galaxy.getGalaxyMask().getProbability((int) centerCoordinates.x, (int) centerCoordinates.y) * 100f;
        boolean generateSystem = this.numberGenerator.isTrue(probability);
        StarSystem system = null;
        Color color = this.galaxy.getGalaxyMask().getColor((int) centerCoordinates.x, (int) centerCoordinates.y);
        logger.log(Level.FINE, "GenerateSystem: "+ generateSystem + " Probability: " + probability + ", Color " + color);
        // for now, there is no star system settings probability table
        if( generateSystem ) {
            StarSystemSettingsRepository repository = this.dataPack.getRepository(StarSystemSettingsRepository.class);
            List<StarSystemSettings> allSystems = repository.all();
            StarSystemSettings randomSystemSettings = numberGenerator.getRandomEntry(allSystems);
            system = this.dataPack.getStarSystemFactory().generateStarSystem(randomSystemSettings);
            this.starSystemCounter.addOne( randomSystemSettings.getName() );

            logger.log(Level.INFO, "Sector created: " + randomSystemSettings.getName() );
        } else {
            StarSystemSettings emptySystemSettings = this.dataPack.<StarSystemSettings>findResourceByID("Empty");
            system = this.dataPack.getStarSystemFactory().generateStarSystem(emptySystemSettings);
            this.starSystemCounter.addOne( emptySystemSettings.getName() );

            logger.log(Level.INFO, "Sector created: " + emptySystemSettings.getName() );
        }
        logger.log(Level.INFO, "Systems generated so far: " + this.starSystemCounter);
        Sector<StarSystem> sector = new Sector<>();
        sector.setCoordinates(worldX, worldY);
        sector.setChunk(system);
        return sector;
    }

    @Override
    public Persistence<Sector<StarSystem>> createPersistence(Sector<StarSystem> sector) {
        return new SectorPersistence(sector);
    }

    @Override
    public Sector<StarSystem> createSector(Persistence<Sector<StarSystem>> sectorPersistence) {
        SectorPersistence sectorPersistence1 = (SectorPersistence) sectorPersistence;

        StarSystem starSystemFromPersistence = this.dataPack.getStarSystemFactory().createStarSystemFromPersistence(sectorPersistence1.getStarSystemPersistence());

        Sector<StarSystem> sector = new Sector<>();
        sector.setCoordinates(sectorPersistence1.getxCoordinates(), sectorPersistence1.getyCoordinates());
        sector.setChunk(starSystemFromPersistence);

        return sector;
    }
}
