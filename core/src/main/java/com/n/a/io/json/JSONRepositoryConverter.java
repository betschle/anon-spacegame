package com.n.a.io.json;

import com.n.a.game.repository.DataRepository;

/**
 * Used in conjunction with the {@link DataRepository} type and
 * the {@link JSONDataSource} Annotation.
 * Classes implementing this interface convert a JSON String into
 * the respective DataRepository. Which Converter to use for this
 * is defined by the JSONRepository Annotation. It's important
 * that each instance implementing this interface has an empty
 * default constructor.
 * @param <T> the target DataRepository
 * @param <JSON> the type of json input, can be a string or a hashmap with key:value pairs
 * @see JSONConverter
 * @see JSONDataSource
 */
public interface JSONRepositoryConverter <T extends DataRepository, JSON> extends JSONConverter<T, JSON> {


    /**
     * Converts the json input and stores all elements (that are contained inside the
     * json input) in the specified repository. This allows for merging items from
     * different files into a single repository.
     * @param repositoryStore the repository to use as store
     * @param input the input JSON
     */
    void convertAndStore(T repositoryStore, JSON input);
}
