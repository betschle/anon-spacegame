package com.n.a.game;

import com.n.a.game.settings.generator.ShipGraphicsSettings;
import com.n.a.gfx.ShipGraphics;
import com.n.a.gfx.particles.ParticleRenderer;

/**
 * A Factory object that creates manueverable spaceships.
 */
public class SpaceShipFactory extends AbstractFactory {

    private ParticleRenderer particleRenderer;

    public SpaceShipFactory(DataPack dataPack) {
        super(dataPack);
    }

    public SpaceShip createSpaceShip(ShipGraphicsSettings settings) {
        SpaceShip ship = new SpaceShip();
        ShipGraphics shipGfx = graphicsAssembler.assembleShip( settings );
        shipGfx.setSpaceShip(ship);
        ship.setShipGraphics(shipGfx);
        return ship;
    }
}
