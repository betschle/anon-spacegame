package com.n.a.game;

import com.n.a.game.discovery.PlanetScanner;
import com.n.a.XYZException;
import com.n.a.game.planet.PlanetTrait;
import com.n.a.gfx.ShipGraphics;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Player Object. Only loosely integrated right now
 * TODO where does this belong? StarSystemFactory?
 */
public class Player {
    /** The name of the player */
    private String name = "Frog";
    /** The shipgraphics of the player ship */
    private ShipGraphics playerShipGraphics;
    /** The controlled ship of the player */
    private SpaceShip playerShip; // TODO not set
    /** Capable to scan planets */
    private PlanetScanner planetScanner;
    /** Discovery Points */
    private int discoveryPoints = 0;
    /** Money bling bling */
    private int chips = 0;
    /**A map of all discovered planet traits. This map does not contain duplicates. */
    private Map<String, PlanetTrait> discoveredPlanetTraits = new LinkedHashMap<>();

    public Player() {

    }

    public int getDiscoveryPoints() {
        return discoveryPoints;
    }

    public int getChips() {
        return chips;
    }

    /**
     * Gets the count of all unique planet traits so far discovered.
     * @return
     */
    public int getDiscoveredPlanetTraits() {
        return this.discoveredPlanetTraits.size();
    }

    /**
     * Adds Discovery Points from a discovered PlanetTrait. Also
     * adds the trait to discovered
     * @param discovered
     */
    public void addDiscoveryPointsFromTrait(PlanetTrait discovered) {
        if( discovered == null) throw new XYZException(XYZException.ErrorCode.E0004, "Trait is null!");
        this.discoveryPoints = this.discoveryPoints + discovered.getDiscoveryPoints();
        if( !this.discoveredPlanetTraits.containsKey(discovered.getId()) ) {
            this.discoveredPlanetTraits.put(discovered.getId(), discovered);
        }
    }

    /**
     * Adds discovery points
     * @param points
     */
    public void addDiscoveryPoints(int points) {
        this.discoveryPoints = this.discoveryPoints + points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShipGraphics getPlayerShipGraphics() {
        return playerShipGraphics;
    }

    public void setPlayerShipGraphics(ShipGraphics playerShipGraphics) {
        this.playerShipGraphics = playerShipGraphics;
        this.playerShip = playerShipGraphics.getSpaceShip();
    }

    public SpaceShip getPlayerShip() {
        return playerShip;
    }

    public PlanetScanner getPlanetScanner() {
        return planetScanner;
    }

    public void setPlanetScanner(PlanetScanner planetScanner) {
        this.planetScanner = planetScanner;
    }
}
