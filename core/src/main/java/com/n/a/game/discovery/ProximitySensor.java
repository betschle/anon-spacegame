package com.n.a.game.discovery;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.n.a.util.PlanetDistanceSorter;

import java.util.ArrayList;
import java.util.List;

/**
 * A proximity Scanner capable to detect
 * within a given range.
 */
public class ProximitySensor {

    /** The parent actor to obtain the proximity from. */
    private Actor parentActor;
    /** The current time until targets are refreshed */
    private float time = 0;
    /** Determines in which interval target list is being updated for better performance */
    private float distanceRefreshRate = 0.2f;
    /** Turns the target list updates on and off, e.g. for NPCs*/
    private boolean updateTargetList = true;
    /** The target distance sorter to use */
    private PlanetDistanceSorter orbitSorter = new PlanetDistanceSorter();
    /**A complete list of actors to operate on, usually planet graphics objects in currently loaded star system. */
    private List<Actor> actors = new ArrayList<>();
    private float maxRange = 1200f;


    public Actor getParentActor() {
        return parentActor;
    }

    public void setParentActor(Actor parentActor) {
        this.parentActor = parentActor;
    }

    public boolean isUpdateTargetList() {
        return updateTargetList;
    }

    public void setUpdateTargetList(boolean updateTargetList) {
        this.updateTargetList = updateTargetList;
    }

    /**
     *
     * @param actor
     * @return true if the actor was found in this sensor's target list.
     */
    public boolean containsTarget(Actor actor) {
        return actors.contains(actor);
    }

    /**
     * Gets all currently calculated targets, sorted from closest to
     * farthest. updateTargetList must be enabled
     * @return a sorted list of planets
     */
    public List<PlanetDistanceSorter.Target> getAllTargets() {
        return this.orbitSorter.getTargets();
    }

    /**
     * Gets all currently calculated targets currently in sensor range,
     * sorted from closest to farthest. updateTargetList must be enabled
     * @return a sorted list of planets
     */
    public List<PlanetDistanceSorter.Target> getTargets() {
        return this.orbitSorter.getTargets( this.maxRange );
    }


    /**
     * Gets the closest target as orbit Within the max sensor range.
     * @return the closest target, or null if none was found.
     */
    public Actor getClosestTargetAsActor() {
        PlanetDistanceSorter.Target closestTarget = getClosestTarget();
        return closestTarget != null ? closestTarget.getActor() : null;
    }

    /**
     * Gets the closest target as OrbitDistance Within the max sensor range.
     * @return the closest target, or null if none was found.
     */
    public PlanetDistanceSorter.Target getClosestTarget() {
        List<PlanetDistanceSorter.Target> orbitDistances = this.orbitSorter.getTargets(this.maxRange);
        if( orbitDistances.isEmpty() ) {
            return null;
        }
        else {
            return orbitDistances.get(0);
        }
    }

    /**
     * Must be set in order for the scanner to work.
     * @param actors
     */
    public void setActors(List<Actor> actors) {
        this.actors = actors;
        this.orbitSorter.setActors(actors);
    }

    public void update(float delta) {
        this.time += delta;
        if( this.time > this.distanceRefreshRate && this.updateTargetList) {
            this.orbitSorter.calculate( this.parentActor.getX(), this.parentActor.getY() );
            this.orbitSorter.sort();
            this.time = 0;
        }
    }
}
