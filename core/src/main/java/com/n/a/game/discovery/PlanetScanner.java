package com.n.a.game.discovery;

import com.n.a.game.SpaceShip;
import com.n.a.game.planet.PlanetTrait;
import com.n.a.game.space.StarSystem;
import com.n.a.gfx.PlanetGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Determines by which rules planets and their traits are discovered.
 * For Player-centered scanning Mechanics. This Scanner uses the
 * {@link ProximitySensor} and is directly tied to its maximumRange.
 */
public class PlanetScanner {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    // TODO should this be decoupled from Player or is it part of player?
    // if not, it'd be part of SpaceShip
    /** The current system the spaceship is in. */
    private StarSystem system;
    /** The currently controlled Spaceship. */
    private SpaceShip spaceShip;
    /** An event listener for discovering planets and their traits. */
    private List<DiscoveryListener> discoveryListeners = new ArrayList<>();

    public void setSystem(StarSystem system) {
        this.system = system;
    }

    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public void addDiscoveryListener(DiscoveryListener discoveryListener) {
        if( discoveryListener != null) {
            this.discoveryListeners.add(discoveryListener);
        }
    }

    public PlanetScanner (SpaceShip spaceShip ) {
        this.spaceShip = spaceShip;
    }

    /**
     * Performs a scan of the first scan within scanner range.
     */
    public void scanFirstTarget() {
        PlanetGraphics focused = (PlanetGraphics) this.spaceShip.getProximitySensor().getClosestTargetAsActor();
        this.scanTarget(focused);
    }

    /**
     * Scans any target. Target must be verified to be inside
     * the scanner's range, otherwise this method won't do anything.
     * @param target
     */
    public void scanTarget(PlanetGraphics target) {
        boolean isTarget = this.spaceShip.getProximitySensor().containsTarget(target);
        if( target != null && isTarget) {
            if( !this.system.isDiscovered(target)) {
                this.system.discoverPlanet(target);
                for( DiscoveryListener listener : this.discoveryListeners) {
                    listener.onPlanetDiscovered(target);
                }
                logger.log(Level.INFO, "Discovered a planet: " + target.getId() );
            } else {
                PlanetTrait trait = target.getModel().getPlanetStats().discoverNextTrait();
                for( DiscoveryListener listener : this.discoveryListeners) {
                    listener.onPlanetTraitDiscovered(target.getModel(), trait);
                }
                logger.log(Level.INFO, "Discovered a trait: " + (trait !=  null ? trait.getId() : "null") );
            }
        }
    }
}
