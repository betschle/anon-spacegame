package com.n.a.io.json;

import java.util.logging.Logger;

/**
 * An Abstraction for JSONConverters. This base class contains a logger
 * and a repository loader field.
 */
public abstract class AbstractJSONConverter {

    protected Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    protected RepositoryLoader loader;

    protected AbstractJSONConverter( RepositoryLoader loader) {
        this.loader = loader;
    }

    public RepositoryLoader getLoader() {
        return loader;
    }
}
