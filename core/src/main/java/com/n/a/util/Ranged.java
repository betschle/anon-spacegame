package com.n.a.util;

public class Ranged<T extends Number> {

    T min;
    T max;

    public Ranged(T max, T min) {
        this.max = max;
        this.min = min;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public void set(T max, T min) {
        this.max = max;
        this.min = min;
    }
}
