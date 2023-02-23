package com.n.a.game.repository;

import com.n.a.game.DataPack;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A generic Repository for storing gamedata in DataPacks, using Strings as identifiers. Has
 * some basic finder methods that can be extended in Implementations.
 * @param <DATA> the data to store
 * @see DataRepository
 * @see DataPack
 */
public class GenericRepository<DATA> implements DataRepository<String, DATA> {

    protected transient Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    private Map<String, DATA> storage = new HashMap<>();

    @Override
    public List<DATA> all() {
        List<DATA> data = new ArrayList<>();
        for( Map.Entry<String, DATA> entry : storage.entrySet()) {
            data.add( entry.getValue());
        }
        return data;
    }

    @Override
    public DATA find(String id) {
        return storage.get(id);
    }

    @Override
    public void add(String id, DATA data) {
        if( !this.storage.containsKey(id)) {
            this.storage.put(id, data);
        } else {
            logger.log(Level.WARNING, "Could not add data: data with name "+ id +" already exists!");
        }
    }

    @Override
    public boolean contains(String id) {
        return storage.containsKey(id);
    }

    @Override
    public int count() {
        return storage.size();
    }

    @Override
    public void clear() {
        this.storage.clear();
    }
}
