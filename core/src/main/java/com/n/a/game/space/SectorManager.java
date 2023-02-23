package com.n.a.game.space;

import com.n.a.XYZException;
import com.n.a.io.persistence.Persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages a buffering mechanism on entering and leaving map chunks based on player location.
 * Also makes sure the map chunks are being generated as needed.
 *
 * <ul>
 *      <li>Only one sector is active, namely the sector the player is in</li>
 *      <li>Changing sectors: new sector is loaded, old sector cached on disk</li>
 *      <li>Sectors are not expected to change frequently, so performance is mostly ignored</li>
 *      <li>The sector manager is aware of all registered sectors via naming convention</li>
 *      <li>The sector manager creates sectors as needed via SectorGenerator (-> World Generator connection)</li>
 * </ul>
 *
 * TODO this needs libGDX filehandling!! -> make a FileHander Wrapper to use as abstraction
 * TODO base Directory feature is not being used
 * TODO the manager should keep a list of all known sectors in order to detect if data is missing.
 *      If one sector is missing, the sector manager gives out a warning that a known sector has been regenerated!
 */
public class SectorManager<C> {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    /** Currently Loaded sector */
    private Sector<C> currentSector;
    /** The X tile size of each sector. All Sector Objects
     * should always be located inside sector bounds. */
    private int sectorBoundsX = 100;
    /** The Y tile size of each sector. All Sector Objects
     * should always be located inside sector bounds.*/
    private int sectorBoundsY = 100;

    /** File extension to use. */
    private String fileExtension = ".asx"; // asx = XYZ Sector
    /** Base Directory of this world. This is an external directory on disk */
    private String baseDirectory = "";
    private SectorGenerator<C> sectorGenerator;
    private List<SectorManagerListener<C>> listeners = new ArrayList<>();

    public Sector<C> getCurrentSector() {
        return currentSector;
    }

    public int getSectorBoundsX() {
        return sectorBoundsX;
    }

    public void setSectorBoundsX(int sectorBoundsX) {
        this.sectorBoundsX = sectorBoundsX;
    }

    public int getSectorBoundsY() {
        return sectorBoundsY;
    }

    public void setSectorBoundsY(int sectorBoundsY) {
        this.sectorBoundsY = sectorBoundsY;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public SectorGenerator<C> getSectorGenerator() {
        return sectorGenerator;
    }

    public void setSectorGenerator(SectorGenerator<C> sectorGenerator) {
        this.sectorGenerator = sectorGenerator;
    }

    public void addListener(SectorManagerListener<C> listener) {
        if( listener != null) listeners.add(listener);
    }

    public void removeListener(SectorManagerListener<C> listener) {
        listeners.remove(listener);
    }

    /**
     * Loads a sector from a file. If sector does not exist, it is created
     * @param xCoords
     * @param yCoords
     * @return
     */
    private Sector<C> loadSector(int xCoords, int yCoords) {
        logger.log(Level.FINE, "Load sector " + xCoords + " " + yCoords);
        // if sector does not exist, create it
        File file = new File( Sector.getSectorName(xCoords, yCoords) + fileExtension);
        Sector<C> resultSector = null;
        if( file.exists() ) {
            if( file.canRead() ) {
                // Deserialization of Sector
                logger.log(Level.FINEST, " - Loading Sector from file");
                try
                {
                    // Reading the object from a file
                    FileInputStream fis = new FileInputStream(file.getName());
                    ObjectInputStream in = new ObjectInputStream(fis);

                    Persistence<Sector<C>>  sectorPersistence = (Persistence<Sector<C>>) in.readObject();
                    resultSector = this.sectorGenerator.createSector(sectorPersistence);

                    in.close();
                    fis.close();
                } catch (IOException ex) {
                    throw new XYZException(XYZException.ErrorCode.E1010, "Could not read chunk in file " + file.getAbsolutePath(), ex);
                } catch (ClassNotFoundException ex) {
                    throw new XYZException(XYZException.ErrorCode.E2001, "Could find class in file " + file.getAbsolutePath(), ex);
                }
            } else {
                throw new XYZException(XYZException.ErrorCode.E1012, "File " + file.getAbsolutePath());
            }
        } else {
            logger.log(Level.FINEST," - New sector generated");
            resultSector = this.sectorGenerator.generateSector(xCoords, yCoords);
            // this.saveSector(resultSector);
            this.fireNewSectorGenerated(resultSector);
        }
        this.fireSectorLoaded(resultSector);
        return resultSector;
    }

    private void saveSector(Sector<C> sector) {
        String filename = Sector.getSectorName(sector.xCoordinates, sector.yCoordinates) + fileExtension;
        logger.log(Level.FINE,"Saving sector as " + filename );
        // Save Sector to file via Serialization
        Persistence<Sector<C>> persistedSector = this.sectorGenerator.createPersistence(sector);
        try
        {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(persistedSector);
            out.close();
            fos.close();
        } catch(IOException ex) {
            // TODO logger Message
            ex.printStackTrace();
        }
        this.fireSectorSaved(sector);
    }

    /**
     * Sector is left when the player ship exceeds the sector bounds.
     *
     * @param sector
     */
    private void leaveSector(Sector<C> sector) {
        logger.log(Level.FINE,"Leave sector");
        // this.saveSector(sector);
    }

    private void enterSector(int sectorXCoords, int sectorYCoords) {
        logger.log(Level.FINE,"Enter sector");
        this.currentSector = this.loadSector(sectorXCoords, sectorYCoords);
    }

    private void fireSectorPreChange(int oldX, int oldY, int newX, int newY) {
        logger.log(Level.FINEST,"Fire Sector PreChange Event");
        for( SectorManagerListener<C> listener : listeners) {
            listener.onSectorPreChange(oldX, oldY, newX, newY);
        }
    }

    private void fireSectorPostChange(int oldX, int oldY, int newX, int newY) {
        logger.log(Level.FINEST,"Fire Sector PostChange Event");
        for( SectorManagerListener<C> listener : listeners) {
            listener.onSectorPostChange(oldX, oldY, newX, newY);
        }
    }

    private void fireNewSectorGenerated(Sector<C> sector) {
        logger.log(Level.FINEST,"Fire new Sector Generated Event");
        for( SectorManagerListener<C> listener : listeners) {
            listener.onNewSectorGenerated(sector);
        }
    }

    private void fireSectorLoaded(Sector<C> sector) {
        logger.log(Level.FINEST,"Fire Sector Loaded Event");
        for( SectorManagerListener<C> listener : listeners) {
            listener.onSectorLoaded(sector);
        }
    }

    private void fireSectorSaved(Sector<C> sector) {
        logger.log(Level.FINEST,"Fire Sector Saved Event");
        for( SectorManagerListener<C> listener : listeners) {
            listener.onSectorSaved(sector);
        }
    }

    /**
     * Invoked on sector change
     * @param oldX old sector coords x
     * @param oldY old sector coords y
     * @param newX new sector coords x
     * @param newY new sector coords y
     */
    private void onSectorChange(int oldX, int oldY, int newX, int newY) {
        this.fireSectorPreChange(oldX, oldY, newX, newY);
        this.leaveSector(currentSector);
        this.enterSector(newX, newY);
        this.fireSectorPostChange(oldX, oldY, newX, newY);
    }

    public int getSectorXCoordinate(float playerXCoords) {
        return (int) Math.floor( (playerXCoords + this.sectorBoundsX/2f ) / this.sectorBoundsX );
    }

    public int getSectorYCoordinate(float playerYCoords) {
        return (int) Math.floor( (playerYCoords + this.sectorBoundsY/2f ) / this.sectorBoundsY );
    }

    /**
     * Checks if Sector was left, in which case it invokes sector change
     * @param playerXCoords
     * @param playerYCoords
     */
    public void checkSector(float playerXCoords, float playerYCoords) {
        int sectorXCoords = this.getSectorXCoordinate(playerXCoords);
        int sectorYCoords = this.getSectorYCoordinate(playerYCoords);

        // self-initialize of first sector on first map build up
        if( currentSector == null ) {
            currentSector = this.loadSector(sectorXCoords, sectorYCoords);
        }

        // if sector could be not loaded, something is wrong with the map!
        if( currentSector == null) {
           throw new XYZException(XYZException.ErrorCode.E2000, "Is the map corrupted!?");
        }

        if( sectorXCoords != currentSector.xCoordinates ||
            sectorYCoords != currentSector.yCoordinates ) {

            onSectorChange(currentSector.xCoordinates, currentSector.yCoordinates,
                           sectorXCoords, sectorYCoords);
        }
    }
}
