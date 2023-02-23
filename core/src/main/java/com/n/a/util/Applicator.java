package com.n.a.util;

/**
 * A utility object that applies values from one object to the other.
 * Possible use cases are: to apply logic to graphics controllers etc.
 * They work in a similar ways to listeners except they constantly update
 * the target object.
 *
 * @param <S> the source object to use the values from
 * @param <T> the target object to apply values to
 */
public interface Applicator<S, T> {

    public void applyValues(S source, T target);
}
