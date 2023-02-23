package com.n.a.io.json;

import com.n.a.game.planet.Classification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JSON-Converter for {@link Classification} objects.
 */
public class JSONToClassification
        extends AbstractJSONConverter
        implements JSONConverter<Classification, Map>  {

    protected JSONToClassification( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public Classification convert(Map input) {
        Classification classification = new Classification();
        classification.setId( (String) input.get("id")  );
        classification.setName( (String) input.get("name")  );
        classification.setDescription( (String) input.get("description")  );

        List<Map> levels = (List<Map>) input.get("levels");
        List<Classification.Level> levelOutput = new ArrayList<>();

        int levelIndex = 0;
        for( Map levelMap : levels) {
            levelOutput.add( Classification.getLevel(
                        (String) levelMap.get("name"),
                        (String) levelMap.get("description"),
                        levelIndex
                    )
            );
            levelIndex++;
        }
        classification.setLevels( levelOutput );
        return classification;
    }
}
