package com.n.a.game.planet;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for planet Classification
 * (e.g. life or temperature)
 */
public class Classification {

    /**The id to be stored in a repository*/
    private String id;
    /**A display name*/
    private String name;
    /**The description of this classification*/
    private String description;
    /** Available classification levels */
    private List<Level> levels = new ArrayList<>();

    public static class Level {
        private String name;
        private String description;
        private int level; // set by the object, index inside of levels

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getLevel() {
            return level;
        }
    }

    /**
     * Creates a level. For Persistence purposes only.
     * @param name
     * @param description
     * @param index
     * @return
     */
    public static Level getLevel(String name, String description, int index) {
        Level level = new Level();
        level.name = name;
        level.description = description;
        level.level = index;
        return level;
    }

    /**
     * Gets the classification Level
     * @param level the level of classification. This value is clamped to the
     *              amount of available levels
     */
    public Level getLevel(int level) {
        return this.getLevels().get( MathUtils.clamp(level, 0, levels.size()-1 ) );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }
}
