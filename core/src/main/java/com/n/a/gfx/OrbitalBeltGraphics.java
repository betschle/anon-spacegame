package com.n.a.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.n.a.game.settings.generator.OrbitalBeltSettings;
import com.n.a.game.space.OrbitalBelt;

/**
 * The satellites in this class are added with a static orbit.
 * Instead of calculating the, the qhole group is rotated.
 * so they can be rotated with the whole belt
 * (to reduce position calculations).
 *
 * @see OrbitalBelt
 * @see OrbitalBeltSettings
 */
public class OrbitalBeltGraphics extends Group {

    private float time = 0;
    /** The object the belt graphics represent. */
    private OrbitalBelt orbitalBelt;

    public void setOrbitalBelt(OrbitalBelt orbitalBelt) {
        this.orbitalBelt = orbitalBelt;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.time = this.time + delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // self rotate slowly
        this.setRotation( time / 3f );
    }
}
