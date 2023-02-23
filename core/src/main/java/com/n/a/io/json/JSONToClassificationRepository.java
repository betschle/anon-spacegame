package com.n.a.io.json;

import com.n.a.game.repository.ClassificationRepository;
import com.n.a.game.planet.Classification;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;

/**
 * Repository for {@link ClassificationRepository} that stores {@link Classification} objects.
 */
public class JSONToClassificationRepository
        extends AbstractJSONConverter
        implements JSONRepositoryConverter<ClassificationRepository, String> {

    protected JSONToClassificationRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public void convertAndStore(ClassificationRepository repositoryStore, String input) {
        JSONToClassification jsonToClassification = getLoader().getLoadedConverter(JSONToClassification.class);

        int loaded = 0;
        Map<String, Object> classificationMap = new JSONObject(input).toMap();
        for(  String classKey : classificationMap.keySet() ) {
            Classification classification = jsonToClassification.convert((Map) classificationMap.get(classKey));
            repositoryStore.add(classification.getId(), classification);
            loaded++;
        }
        logger.log(Level.INFO, "Loaded {0} Planet Classifications", loaded);
    }

    @Override
    public ClassificationRepository convert(String input) {
        ClassificationRepository classificationRepository = new ClassificationRepository();
        this.convertAndStore(classificationRepository, input);
        return classificationRepository;
    }
}
