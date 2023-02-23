package com.n.a.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.n.a.game.discovery.PlanetScanner;
import com.n.a.game.discovery.ProximitySensor;
import com.n.a.game.settings.generator.ShipGraphicsSettings;
import com.n.a.gfx.ShipGraphics;

import java.util.List;

public class SpaceShip {

    /** The display name of this ship. */
    private String name;
    /** The graphics */
    private ShipGraphics shipGraphics;
    private ShipGraphicsSettings shipGraphicsSettings; // TODO not set
    /** Capable to scan planets */
    private PlanetScanner planetScanner;
    /** A Proximity Sensor that detects celestial objects nearby. */
    private ProximitySensor proximitySensor = new ProximitySensor();

    // private float maxFuel = 1000;
    // private float fuel = 1000;

    /** Sound management */
    private long playerJetId;
    private Sound jetsound;

    public SpaceShip() {
        this.jetsound = Gdx.audio.newSound(Gdx.files.internal("sfx/actor/thruster2.wav"));
    }

    public void startEngines() {
        this.playerJetId = this.jetsound.loop();
    }

    public void stopEngines() {
        this.jetsound.stop();
    }

    public ShipGraphics getShipGraphics() {
        return shipGraphics;
    }

    public void setShipGraphics(ShipGraphics shipGraphics) {
        this.shipGraphics = shipGraphics;
        this.proximitySensor.setParentActor(this.shipGraphics);
    }

    /**
     * Must be set in order for the scanner to work.
     * @param actors
     */
    public void setTargetableActors(List<Actor> actors) {
        this.proximitySensor.setActors(actors);
    }

    public ProximitySensor getProximitySensor() {
        return proximitySensor;
    }

    public void update(float delta) {
        this.proximitySensor.update(delta);
        this.jetsound.setPitch(this.playerJetId, Math.max(0.3f, this.shipGraphics.getThrusterPower() * 1.1f ));
        this.jetsound.setVolume(this.playerJetId, Math.max(0.7f, this.shipGraphics.getThrusterPower() * 0.6f ));

    }
}
