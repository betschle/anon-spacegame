package com.n.a.io.json;

/**
 * A general interface aiding in conversion of JSON objects.
 * Each class implementing JSONConverter must also extends {@link AbstractJSONConverter}
 * and have a single protected Constructor with {@link RepositoryLoader} as single parameter.
 * @param <JSON> the JSON input (can be a String, list or map of Strings)
 * @param <T> The output type
 * @see JSONRepositoryConverter
 * @see JSONDataSource
 */
public interface JSONConverter<T, JSON> {

    /**
     * Converts a JSON String into a DataRepository.
     * @param input the JSON containing data, usually in form of a map.
     * @return a DataRepository Object that contains the JSON-Data converted to Java objects
     */
    T convert(JSON input);

}
