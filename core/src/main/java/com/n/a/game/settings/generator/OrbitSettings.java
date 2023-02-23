package com.n.a.game.settings.generator;

/**
 * Orbit settings that orbits are created with
 * Different types of orbits are created using this object, so not
 * all of its fields are relevant for creating one orbit type.
 */
public class OrbitSettings {

    public enum OrbitType {
        /**No orbit, planets do not move around. If this is set, irrelevant values are ignored. */
        STATIC,
        /** A normal ellipsoid orbit. If this is set, irrelevant values are ignored. */
        ELLIPSOID;
    }

    protected OrbitType orbitType = OrbitType.ELLIPSOID;
    // for static orbits
    /** For Static Orbits: The static X position. This value is ignored for other orbit types. */
    protected float staticPositionX = 0;
    /** For Static Orbits: The static Y position. This value is ignored for other orbit types. */
    protected float staticPositionY = 0;

    // for ellipsoid orbits
    protected float minVelocity = 0.003f;
    protected float maxVelocity = 0.003f;
    /** For Ellipsoid Orbits. Minimum width, this is being added to the default width by star system. */
    protected float minWidthMargin = 100f;
    protected float maxWidthMargin = 100f;
    /** For Ellipsoid Orbits: */
    protected float maxHeightMargin = 100f;
    protected float minHeightMargin = 100f;


    /**
     * Gets a predefined orbital setting for ellipse type orbits.
     * @param velocity
     * @param width
     * @param height
     * @return
     */
    public static OrbitSettings getEllipsoidOrbitSettings(float velocity, float width, float height) {
        OrbitSettings orbitSettings = new OrbitSettings();
        orbitSettings.orbitType = OrbitType.ELLIPSOID;

        orbitSettings.maxVelocity = velocity;
        orbitSettings.minVelocity = velocity * 0.90f;

        orbitSettings.maxWidthMargin = width;
        orbitSettings.minWidthMargin = width * 0.90f;

        orbitSettings.maxHeightMargin = height;
        orbitSettings.minHeightMargin = height * 0.90f;
        return orbitSettings;
    }

    /**
     * Gets a predefined orbital setting for static type orbits. These orbits do not move.
     * @param x
     * @param y
     * @return
     */
    public static OrbitSettings getStaticOrbitSettings(float x, float y) {
        OrbitSettings orbitSettings = new OrbitSettings();
        orbitSettings.orbitType = OrbitType.STATIC;
        orbitSettings.staticPositionX = x;
        orbitSettings.staticPositionY = y;
        orbitSettings.minWidthMargin = 0;
        orbitSettings.maxWidthMargin = 0;
        orbitSettings.minHeightMargin = 0;
        orbitSettings.maxHeightMargin = 0;
        orbitSettings.minVelocity = 0;
        orbitSettings.maxVelocity = 0;
        return orbitSettings;
    }

    public float getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(float minVelocity) {
        this.minVelocity = minVelocity;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getMinWidthMargin() {
        return minWidthMargin;
    }

    public void setMinWidthMargin(float minWidthMargin) {
        this.minWidthMargin = minWidthMargin;
    }

    public float getMaxWidthMargin() {
        return maxWidthMargin;
    }

    public void setMaxWidthMargin(float maxWidthMargin) {
        this.maxWidthMargin = maxWidthMargin;
    }

    public float getMaxHeightMargin() {
        return maxHeightMargin;
    }

    public void setMaxHeightMargin(float maxHeightMargin) {
        this.maxHeightMargin = maxHeightMargin;
    }

    public float getMinHeightMargin() {
        return minHeightMargin;
    }

    public void setMinHeightMargin(float minHeightMargin) {
        this.minHeightMargin = minHeightMargin;
    }

    public OrbitType getOrbitType() {
        return orbitType;
    }

    public void setOrbitType(OrbitType orbitType) {
        this.orbitType = orbitType;
    }

    public float getStaticPositionX() {
        return staticPositionX;
    }

    public void setStaticPositionX(float staticPositionX) {
        this.staticPositionX = staticPositionX;
    }

    public float getStaticPositionY() {
        return staticPositionY;
    }

    public void setStaticPositionY(float staticPositionY) {
        this.staticPositionY = staticPositionY;
    }

    @Override
    public String toString() {
        return "OrbitSettings{" +
                "orbitType=" + orbitType +
                ", staticPositionX=" + staticPositionX +
                ", staticPositionY=" + staticPositionY +
                ", minVelocity=" + minVelocity +
                ", maxVelocity=" + maxVelocity +
                ", minWidthMargin=" + minWidthMargin +
                ", maxWidthMargin=" + maxWidthMargin +
                ", maxHeightMargin=" + maxHeightMargin +
                ", minHeightMargin=" + minHeightMargin +
                '}';
    }
}
