package com.n.a.io.json;

import com.n.a.game.DataPack;
import com.n.a.game.repository.DataRepository;

import java.lang.annotation.*;

/**
 * Annotation that marks a type as JSON-backed {@link DataRepository}.
 * This Annotation is part of an automatic mechanism that loads JSON-Data into
 * a repository, on creating a {@link DataPack}. When a <code>DataRepository</code>
 * is being loaded, its class is scanned for the presence of this annotation.
 * Then using the {@link JSONConverter} that is being defined via <code>value</code>,
 * the JSON-Data is loaded into the repository.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JSONDataSource {
    /**
     * The class to use for conversion
     * @return
     */
    Class<? extends JSONConverter> value();
}
