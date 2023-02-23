package com.n.a.world;

import com.n.a.game.space.Sector;
import com.n.a.game.space.SectorManager;
import com.n.a.game.space.SectorManagerListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
  TODO this is a very rudimentary test
  need to verify that the files are being correctly created each time
  need a cleanup procedure too

  TODO needs verification of test results
 */

public class SectorManagerTest implements SectorManagerListener<String> {

    int currentX = 0;
    int currentY = 0;
    int sectorSizeX = 0;
    int sectorSizeY = 0;
    private Sector<String> currentSector;

    public void resetTestsVariables() {
        this.currentX = 0;
        this.currentY = 0;
        this.sectorSizeX = 0;
        this.sectorSizeY = 0;
        this.currentSector = null;
    }

    // TODO provide the old? Sector to this method
    @Override
    public void onSectorPreChange(int oldX, int oldY, int newX, int newY) {
        System.out.println("Fire Sector PreChange Event"+ oldX + "/" + oldY + " " + newX + "/" + newY +" Sector " + currentSector.getXCoordinates() + "/" + currentSector.getYCoordinates());
        // this.currentSector has not changed at this point!!!
        Assertions.assertEquals( currentSector.getXCoordinates(), oldX );
        Assertions.assertEquals( currentSector.getYCoordinates(), oldY );
        Assertions.assertEquals( (int) Math.floor( currentX / sectorSizeX ), newX );
        Assertions.assertEquals( (int) Math.floor( currentY / sectorSizeY ), newY );

    }

    // TODO provide the new? Sector to this method
    @Override
    public void onSectorPostChange(int oldX, int oldY, int newX, int newY) {
        System.out.println("Fire Sector PostChange Event " + oldX + "/" + oldY + " " + newX + "/" + newY +" Sector " + currentSector.getXCoordinates() + "/" + currentSector.getYCoordinates());
        // this.currentSector has not changed at this point!!!
        Assertions.assertEquals( currentSector.getXCoordinates(), oldX );
        Assertions.assertEquals( currentSector.getYCoordinates(), oldY );
        Assertions.assertEquals( (int) Math.floor( currentX / sectorSizeX ), newX );
        Assertions.assertEquals( (int) Math.floor( currentY / sectorSizeY ), newY );
    }

    @Override
    public void onNewSectorGenerated(Sector sector) {
        System.out.println("Fire new Sector Generated Event: " + sector);
    }

    @Override
    public void onSectorLoaded(Sector sector) {
        System.out.println("Fire Sector Loaded Event: " + sector);
    }

    @Override
    public void onSectorSaved(Sector sector) {
        System.out.println("Fire Sector Saved Event: " + sector);
        // this here needs to verify the saved file
    }

    @Test
    public void goAllDirections() {
        // TODO set up a test where one cached file gets deleted and check for exceptions
        // TODO modify cached files so that there is an error thrown, check for the right exception
        DummySectorGenerator sectorGenerator = new DummySectorGenerator();
        SectorManager<String> sectorManager = new SectorManager<>();
        this.sectorSizeX = sectorManager.getSectorBoundsX(); // use default values
        this.sectorSizeY = sectorManager.getSectorBoundsY();
        sectorManager.addListener(this);
        sectorManager.setSectorGenerator(sectorGenerator);
        for( int i = 1; i < 11; i++ ) {
            this.currentX = i * 100;
            this.currentY = i * 20;
            sectorManager.checkSector( this.currentX , this.currentY );
            this.currentSector = sectorManager.getCurrentSector();
        }
    }
}
