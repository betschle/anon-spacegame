package com.n.a.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.*;

/**
 * A Sorter that determines the distance of
 * Actors to any given point.
 */
public class PlanetDistanceSorter {

    public class Target {
        private Actor actor;
        protected float distance;

        public Target(Actor actor, float distance) {
            this.actor = actor;
            this.distance = distance;
        }

        public Actor getActor() {
            return actor;
        }

        public float getDistance() {
            return distance;
        }
    }

    private class DistanceComparator implements Comparator<Target> {

        @Override
        public int compare(Target o1, Target o2) {
            if( o1.distance < o2.distance) return -1;
            if( o1.distance > o2.distance) return 1;
            return 0;
        }
    }

    private DistanceComparator comparator = new DistanceComparator();
    private List<Target> targets = new ArrayList<>();

    /**
     *
     * @return a (sorted) array of calculated distances. If this is sorted depends on whether {@link #calculate(float, float)} or
     * {@link #sort()} were both called.
     */
    public List<Target> getTargets() {
        return targets;
    }

    /**
     *
     * @param maxDistance the maximum allowed distance
     * @return a copy of OrbitDistance list
     */
    public List<Target> getTargets(float maxDistance) {
        List<Target> result = new ArrayList<>();
        for( Target orbit : this.targets) {
            if( orbit.getDistance() < maxDistance) {
                result.add(orbit);
            }
        }
        return result;
    }

    /**
     * Sets the actors to calculate distances from.
     * @param actors
     */
    public void setActors(List<Actor> actors) {
        this.targets.clear();
        for( Actor actor : actors) {
            this.targets.add ( new Target(actor, 0));
        }
    }

    /**
     * Calculates the distance of all orbits to the specified position.
     *
     * @param x the x position to calculate the distance from
     * @param y the y position to calculate the distance from
     */
    public void calculate(float x, float y ) {
        for( Target distance : targets) {
            Vector2 worldPosition = XYZUtil.getWorldPosition(distance.getActor());
            distance.distance = Vector2.dst(
                    x, y,
                    worldPosition.x,
                    worldPosition.y
                    );
        }
    }

    /**
     * Sorts the list based on currently calculated distances. {@link #calculate(float, float)} must
     * be called first in order for any change to take effect.
     */
    public void sort() {
        Collections.sort(this.targets, comparator);
    }

}
