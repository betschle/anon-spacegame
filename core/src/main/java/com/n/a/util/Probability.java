package com.n.a.util;

/**
 *
 * @param <T> The type to which the probability applies. Can be a String, Enum or Class.
 */
public class Probability<T> {

    /**
     * The type that is used when this probability is chosen.
     */
    private T type;
    /**
     * An Archetype inside of the type. Is optional but type
     * cannot be null if this field is defined.
     */
    private String subtype;
    /**
     * Determines the chance for this probability to be chosen.
     * This value must be between 0 and 100.
     */
    private float weight;

    public Probability(){
        // empty constructor
    }

    public Probability(T type, float probability) {
        this.type = type;
        this.weight = probability;
    }

    public Probability(T type, String subtype, float probability) {
        this.type = type;
        this.weight = probability;
        this.subtype = subtype;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    /**
     * The weight as probability. Determines the chance
     * for this probability to be chosen.
     * @return
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Sets the weight of this probability.
     * @param weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
