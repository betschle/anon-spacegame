package com.n.a.gfx;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.n.a.game.EntityID;

/**
 * Abstract Graphics define Graphics that are hooked to a business object.
 */
public abstract class AbstractGraphics extends Group {

    protected EntityID id = null;

    protected AbstractGraphics(EntityID id) {
        this.id = id;
    }

    public EntityID getId() {
        return id;
    }
}
