package com.n.a.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.n.a.game.space.Planet;
import com.n.a.gfx.PlanetGraphics;

import java.util.ArrayList;
import java.util.List;

public class XYZUtil {

    /**
     * Gets the world position of an actor.
     * @param actor
     * @return
     */
    public static Vector2 getWorldPosition(Actor actor) {
        return actor.localToStageCoordinates( new Vector2() );
        /*
        Vector2 position = new Vector2();
        Actor currentActor = actor;
        while(  currentActor != null) {
            position.x = position.x + currentActor.getX();
            position.y = position.y + currentActor.getY();
            currentActor = currentActor.getParent();
        }
        return position;
         */
    }

    public static List<Actor> orbitToActorList(List<Planet> orbits) {
        List<Actor> result = new ArrayList<>();
        for( Planet orbit : orbits) {
            result.add( orbit.getGraphics() );
        }
        return result;
    }

    /**
     * Does not include objects that are not PlanetGraphics, so it essentially works
     * as filter too.
     * @param planets
     * @return
     */
    public static List<PlanetGraphics> actorToPlanetGraphicsList(List<Actor> planets) {
        List<PlanetGraphics>  result = new ArrayList<>();
        for( Actor actor : planets) {
            if( actor instanceof PlanetGraphics) {
                result.add( (PlanetGraphics) actor);
            }
        }
        return result;
    }

    public static List<PlanetGraphics> planetToGraphicsList(List<Planet> planets) {
        List<PlanetGraphics>  result = new ArrayList<>();
        for( Planet orbit : planets) {
            result.add( orbit.getGraphics() );
        }
        return result;
    }
}
