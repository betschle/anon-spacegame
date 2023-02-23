package com.n.a.io;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.Serializable;

/**
 * Utility Class to easily store GDX Actor properties for serialization.
 */
public class SpatialInformation implements Serializable {
    public float positionX;
    public float positionY;
    public float rotation;
    public float width;
    public float height;
    public float originX;
    public float originY;
    public float scaleX;
    public float scaleY;

    /**
     *
     * @param actor
     * @return
     */
    public static SpatialInformation getInformation(Actor actor){
        SpatialInformation settings = new SpatialInformation();
        settings.scaleX = actor.getScaleX();
        settings.scaleY = actor.getScaleY();
        settings.positionX = actor.getX();
        settings.positionY = actor.getY();
        settings.width = actor.getWidth();
        settings.height = actor.getHeight();
        settings.rotation = actor.getRotation();
        settings.originX = actor.getOriginX();
        settings.originY = actor.getOriginY();

        return settings;
    }

    /**
     *
     * @param actor
     * @param information
     */
    public static void applyToActor( Actor actor, SpatialInformation information) {
        actor.setPosition(information.positionX, information.positionY);
        actor.setScale(information.scaleX, information.scaleY);
        actor.setHeight( information.height );
        actor.setWidth( information.width );
        actor.setRotation( information.rotation );
        actor.setOrigin( information.originX, information.originY);
    }
}
