package com.n.a.io.json;

import com.n.a.util.Probability;
import com.n.a.util.Tree;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JSONConverterTest {

    String treeJSON = "{\n" +
            "  \"object\": { \"weight\": 100, \"type\": \"planetTrait_surfaceContinental\"},\n" +
            "  \"children\": [\n" +
            "    {\n" +
            "      \"object\": { \"weight\": 100, \"type\": \"planetTrait_ocean80\"},\n" +
            "      \"children\": [\n" +
            "        {\n" +
            "          \"object\": {\"weight\": 5, \"type\": \"planetTrait_fossilSite\"},\n" +
            "          \"children\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"object\": { \"weight\": 50, \"type\": \"planetTrait_volcanicActivity\"},\n" +
            "      \"children\": [\n" +
            "        {\n" +
            "          \"object\": { \"weight\": 7, \"type\": \"planetTrait_superVolcanoes\"},\n" +
            "          \"children\": []\n" +
            "        },\n" +
            "\n" +
            "        {\n" +
            "          \"object\": { \"weight\": 7, \"type\": \"planetTrait_extremeMountains\"},\n" +
            "          \"children\": []\n" +
            "        },\n" +
            "\n" +
            "        {\n" +
            "          \"object\": { \"weight\": 7, \"type\": \"planetTrait_geysers\"},\n" +
            "          \"children\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"object\": { \"weight\": 50, \"type\": \"planetTrait_magneticField\"},\n" +
            "      \"children\": []\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private String loadJSONFromTestResources(String filename) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        File file = new File(url.getPath());
        // TODO finish this
        return "";
    }

    @Test
    public void JSONToTree() {
        JSONObject systemJSON = new JSONObject(treeJSON);
        JSONToTree jsonToTree = new JSONToTree(new RepositoryLoader());
        Map<String, Object> treeMap = systemJSON.toMap();

        Tree<Probability> convertedTree = jsonToTree.convert(treeMap);
        Probability probability = convertedTree.getRoot().getObject();

        Assertions.assertTrue(convertedTree.getChildAt(1).size() > 0 );
        Assertions.assertEquals( 3, convertedTree.childrenCount() );
        Assertions.assertEquals( 100, probability.getWeight() );
        Assertions.assertEquals( "planetTrait_surfaceContinental", probability.getType() );

    }
}
