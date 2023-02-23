package com.n.a.io.persistence;

import java.io.Serializable;

/**
 * An object that provides enough information to be able to
 * reconstruct an object's graphics and spatial state. These
 * objects are serialized instead of the object to persist.
 *
 * @param <T> the object to persist
 */
public class Persistence<T> implements Serializable {
}
