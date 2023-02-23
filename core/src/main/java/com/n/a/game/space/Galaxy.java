package com.n.a.game.space;

import com.n.a.game.EntityID;

public class Galaxy {

    /** The ID of the galaxy */
    private EntityID id;
    /** The Name of the Galaxy */
    private String name;
    /** The Galaxy Mask which determines where star systems are generated with which probability. */
    private GalaxyMask galaxyMask;
    /** The amount of theoretically amount of systems possible in this galaxy. */
    private int theoreticalAmountOfSystems = 0;
    // TODO StarSystemHeaders

    public Galaxy() {

    }

    public EntityID getId() {
        return id;
    }

    public void setId(EntityID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GalaxyMask getGalaxyMask() {
        return galaxyMask;
    }

    public void setGalaxyMask(GalaxyMask galaxyMask) {
        this.galaxyMask = galaxyMask;
        this.theoreticalAmountOfSystems = galaxyMask != null ? galaxyMask.countAverage(3) : 0;
    }

    public int getTheoreticalAmountOfSystems() {
        return theoreticalAmountOfSystems;
    }

    @Override
    public String toString() {
        return "Galaxy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theoreticalAmountOfSystems='" + this.theoreticalAmountOfSystems + '\'' +
                '}';
    }
}
