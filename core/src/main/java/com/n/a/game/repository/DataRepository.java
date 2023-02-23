package com.n.a.game.repository;

import java.util.List;

/**
 * A Data Repository that can be managed by a DataPack. Provides
 * a finder functionality. A DataRepository cannot be modified by definition as its contents
 * are loaded from a disk. One DataPack may consist of one or more DataRepositories.
 * @param <ID> the ID type to use as "primary key"
 * @param <DATA> the data type to store
 */
public interface DataRepository <ID, DATA>{


    /**
     * returns all elements in this repository.
     * @return
     */
    List<DATA> all();
    /**
     * Find an Object by its ID
     * @param id
     * @return
     */
    DATA find(ID id);

    /**
     * Adds a set of data. Only to be used by Serialization
     * @param id
     * @param data
     */
    void add(ID id, DATA data);

    /**
     * Checks if an object with ID is found within this repository.
     * @param id true if an object with this ID was found
     * @return
     */
    boolean contains(ID id);

    /**
     * The amount of objects stored in this repository.
     * @return
     */
    int count();

    /**
     * Clears the repository's content.
     */
    void clear();


}
