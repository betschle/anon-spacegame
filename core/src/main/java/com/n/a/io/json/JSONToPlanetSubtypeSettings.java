package com.n.a.io.json;

import com.n.a.game.settings.generator.PlanetSubtypeSettings;
import com.n.a.util.Probability;
import com.n.a.util.Tree;

import java.util.Map;

public class JSONToPlanetSubtypeSettings
        extends AbstractJSONConverter
        implements JSONConverter<PlanetSubtypeSettings, Map>{

    protected JSONToPlanetSubtypeSettings( RepositoryLoader loader) {
        super(loader);
    }

    @Override
    public PlanetSubtypeSettings convert(Map input) {
        JSONToTree jsonToTree = this.getLoader().getLoadedConverter(JSONToTree.class);
        PlanetSubtypeSettings subtypeSettings = new PlanetSubtypeSettings();

        subtypeSettings.setMinAxisTilt( ((Number) input.get("minAxisTilt")).floatValue());
        subtypeSettings.setMaxAxisTilt( ((Number) input.get("maxAxisTilt")).floatValue());
        subtypeSettings.setMinScale( ((Number) input.get("minScale")).floatValue());
        subtypeSettings.setMaxScale( ((Number) input.get("maxScale")).floatValue());

        Map traits = (Map) input.get("traits");
        Tree<Probability> convert = jsonToTree.convert(traits);
        subtypeSettings.setTraits(convert);
        return subtypeSettings;
    }
}
