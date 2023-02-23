package com.n.a.io;

import com.badlogic.gdx.graphics.Color;
import com.n.a.game.repository.ColorRepository;
import com.n.a.game.repository.StarSystemSettingsRepository;
import com.n.a.game.settings.generator.StarSystemSettings;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// TODO Refactoring: Test JSONToXZY Classes instead, remove JSONFactory as class
public class JSONFactoryTest {

    String functionalStarSystemJSON="{\n" +
            "\t\"SolSystem\": {\n" +
            "\t  \"name\": \"SolSystem\",\n" +
            "\t  \"minPlanetSpeed\": 0.0001,\n" +
            "\t  \"maxPlanetSpeed\": 0.01,\n" +
            "\t  \"spacing\": 100,\n" +
            "\t  \"planetProbability\": [\n" +
            "\t\t{ \"weight\": 100, \"type\": \"PLANEMO\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"MOON\" },\n" +
            "\t\t{ \"weight\": 30, \"type\": \"VOLCANIC\" },\n" +
            "\t\t{ \"weight\": 40, \"type\": \"TOXIC\" },\n" +
            "\t\t{ \"weight\": 50, \"type\": \"ASTEROID_BELT\" },\n" +
            "\t\t{ \"weight\": 90, \"type\": \"CONTINENTAL\" },\n" +
            "\t\t{ \"weight\": 20, \"type\": \"CONTINENTAL\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"GASEOUS\" },\n" +
            "\t\t{ \"weight\": 80, \"type\": \"GASEOUS\" },\n" +
            "\t\t{ \"weight\": 40, \"type\": \"GASEOUS\" },\n" +
            "\t\t{ \"weight\": 40, \"type\": \"GASEOUS\", \"archetype\": \"GaseousRed\" }\n" +
            "\t  ]\n" +
            "\t},\n" +
            "\t\"GenerateStarSystemTest\": {\n" +
            "\t  \"name\": \"GenerateStarSystemTest\",\n" +
            "\t  \"minPlanetSpeed\": 0.0001,\n" +
            "\t  \"maxPlanetSpeed\": 0.01,\n" +
            "\t  \"spacing\": 100,\n" +
            "\t  \"planetProbability\": [\n" +
            "\t\t{ \"weight\": 100, \"type\": \"PLANEMO\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"MOON\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"VOLCANIC\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"TOXIC\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"ASTEROID_BELT\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"CONTINENTAL\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"OCEANIC\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"GASEOUS\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"STAR\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"NEUTRON_STAR\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"PULSAR\" },\n" +
            "\t\t{ \"weight\": 100, \"type\": \"BLACK_HOLE\" }\n" +
            "\t  ]\n" +
            "\t}\n" +
            "}";
    String functionalColorSetJSON = "{\n" +
            "\t\"name\": \"jsonFactoryTest\",\n" +
            "\t\"colors\": {\n" +
            "\t\t\"errorColor\": \"#\",\n" +
            "\t\t\"workingColor\": \"#45080dff\"\n" +
            "\t}\n" +
            "}";

    // a { is missing at the end here!
    String erroneousColorSetJSON = "{\n" +
            "\t\"name\": \"jsonFactoryTest\",\n" +
            "\t\"colors\": {\n" +
            "\t\t\"errorColor\": \"#\",\n" +
            "\t\t\"workingColor\": \"#45080dff\"\n" +
            "\t}\n";

    @Test
    public void jsonToColorSet() {
        JSONFactory factory = new JSONFactory();
        ColorRepository colorSet = factory.jsonToColorSet(functionalColorSetJSON);
        Color color = colorSet.find("workingColor");
        Color errorColor = colorSet.find("errorColor");
        Assertions.assertNotNull(color);
        Assertions.assertNull(errorColor);
    }

    @Test
    public void jsonToColorSet_syntaxError() {
        JSONFactory factory = new JSONFactory();
        try {
            factory.jsonToColorSet(erroneousColorSetJSON);
        } catch( JSONException ex ) {
            ex.printStackTrace(); // TODO logger
            return;
        }
        Assertions.fail("JSON Exception expected!");
    }

    @Test
    public void jsonToPlanetGraphicsSettingsMap() {
        JSONFactory factory = new JSONFactory();
        // TODO
    }

    @Test
    public void jsonToStarSystemSettings() {
        JSONFactory factory = new JSONFactory();
        StarSystemSettingsRepository repository = factory.jsonToStarSystemSettings(functionalStarSystemJSON);
        StarSystemSettings systemSetting = repository.find("GenerateStarSystemTest");
        Assertions.assertTrue(systemSetting.getPlanetProbability().size() > 0);
    }

}
