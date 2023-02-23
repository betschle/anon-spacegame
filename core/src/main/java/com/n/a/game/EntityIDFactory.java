package com.n.a.game;

import com.n.a.util.sequences.StringGenerator;

/**
 * Creates EntityIDs using a set of well-defined rules.
 */
public class EntityIDFactory {

    private StringGenerator stringGenerator;
    // Factory Settings
    private int idLength = 10;

    public EntityIDFactory( StringGenerator generator) {
        this.stringGenerator = generator;
    }

    public int getIdLength() {
        return idLength;
    }

    public void setIdLength(int idLength) {
        this.idLength = idLength;
    }

    /**
     * Creates a simple ID with prefix. The ID Length is sacrificed to
     * fit the prefix into the ID.
     * @param prefix the prefix to use
     * @param length the length to use
     * @return
     */
    public EntityID createID( String prefix, int length) {
        if( prefix == null ) throw new NullPointerException("Prefix cannot be null!");
        StringBuilder randomString = this.stringGenerator.getRandomString(length);
        randomString.insert(0, prefix +"_");
        return new EntityID(  randomString.toString() );
    }
    /**
     * Creates a simple ID with prefix. The ID Length is sacrificed to
     * fit the prefix into the ID.
     * @param prefix the prefix to use
     * @return
     */
    public EntityID createID( String prefix) {
        return this.createID(prefix, this.idLength);
    }

    /**
     * Creates a simple ID without prefix
     * @return
     */
    public EntityID createID() {
        return new EntityID( stringGenerator.getRandomString(idLength).toString() );
    }

    /**
     * For persistence purposes
     * @param id
     * @return
     */
    public EntityID get(String id) {
        return new EntityID(id);
    }
}
