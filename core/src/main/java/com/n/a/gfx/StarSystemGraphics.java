package com.n.a.gfx;

import com.n.a.game.space.StarSystem;
import com.n.a.game.EntityID;

/**
 * A graphics object that visually represents a StarSystem.
 */
public class StarSystemGraphics extends AbstractGraphics {

    private StarSystem model;
    private PlanetGraphics sun;

    public StarSystemGraphics(EntityID id, PlanetGraphics sun) {
        super(id);
        if( sun == null) {
            throw new NullPointerException("Sun Graphics cannot be null");
        }
        this.sun = sun;
        this.addActor(sun);
    }

    public StarSystem getModel() {
        return model;
    }

    /**
     * Sets the DataModel. Establishes a connection between
     * Entity and its Graphics in both directions.
     * @param model
     */
    public void setModel(StarSystem model) {
        this.model = model;
        this.model.setGraphics(this);
    }
}
