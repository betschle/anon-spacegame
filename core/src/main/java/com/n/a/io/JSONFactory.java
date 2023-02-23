package com.n.a.io;

import com.badlogic.gdx.graphics.Color;
import com.n.a.game.repository.ColorRepository;
import com.n.a.game.repository.StarSystemSettingsRepository;
import com.n.a.game.settings.generator.PlanetGraphicsSettings;
import com.n.a.game.settings.generator.StarSystemSettings;
import com.n.a.util.Probability;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates usable Game Objects based on JSON data.
 * @Deprecated
 */
@Deprecated
public class JSONFactory {

    // TODO use in conjuction with DataRepository

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    public StarSystemSettingsRepository jsonToStarSystemSettings(String json ) throws JSONException {
        // the json is expected to be a map that contains multiple starsystem entries
        JSONObject systemJSON = new JSONObject(json);
        StarSystemSettingsRepository repository = new StarSystemSettingsRepository();
        for(Map.Entry<String, Object> entry : systemJSON.toMap().entrySet()){
            Map starSystemMap = (Map) entry.getValue();
            String name = (String) starSystemMap.get("name");
            Number minPlanetSpeed = (Number) starSystemMap.get("minPlanetSpeed");
            Number maxPlanetSpeed = (Number) starSystemMap.get("maxPlanetSpeed");
            Number spacing = (Number) starSystemMap.get("spacing");
            // validate that key == object name
            if( !Objects.equals(name, entry.getKey())) {
                logger.severe("Error creating StarSystem because StarSystem.name "+ name+ " does not match Map-key '"+entry.getKey()+"'. System is ignored!");
                continue; // skip
            }
            List<Map> planetProbability = (List<Map>) starSystemMap.get("planetProbability");
            List<Probability> probabilities = this.jsonToProbability(planetProbability);

            StarSystemSettings systemSettings = new StarSystemSettings();
            systemSettings.setName(name);
            systemSettings.setMaxPlanetSpeed( minPlanetSpeed.floatValue() );
            systemSettings.setMaxPlanetSpeed( maxPlanetSpeed.floatValue() );
            systemSettings.setSpacing(spacing.floatValue());

            repository.add(systemSettings.getName(), systemSettings);
        }
        return repository;
    }

    /**
     * Converts a json String to a ColorSet
     * @param json
     * @return
     */
    public ColorRepository jsonToColorSet(String json) throws JSONException {
        ColorRepository colorSet = new ColorRepository();
        JSONObject colorSetJson = new JSONObject(json);
        // colorSet.setName( colorSetJson.get("name").toString() );
        // parse all colors
        JSONObject colors = colorSetJson.getJSONObject("colors");
        Map<String, Object> colorsMap = colors.toMap();
        for(  String colorName : colorsMap.keySet() ) {
            // Convert String #45080dff => int value as 0x45080dff
            String colorObject = (String) colorsMap.get(colorName);
            colorObject = colorObject.replace('#', ' ');
            colorObject = colorObject.trim();
            logger.log(Level.FINEST, "Decoding color '" + colorName + "' of value '" + colorObject+"'" );
            if( colorObject.isEmpty() ) {
                logger.log(Level.SEVERE, "Could not parse color with name: '" + colorName+"' of value '" + colorObject+"'");
            } else {
                try {
                    Color color = Color.valueOf(colorObject);
                    colorSet.add(colorName, color);
                } catch (NumberFormatException ex) {
                    logger.log(Level.SEVERE, "Could not parse color with name: '" + colorName + "': " + ex.getMessage());
                }
            }
        }
        return colorSet;
    }



    /**
     *
     * @param colorset The ColorSet to use to dissolve color names
     * @param jsonMap
     * @return
     */
    private PlanetGraphicsSettings jsonToPlanetGraphicsSettings(ColorRepository colorset, Map jsonMap) {
        PlanetGraphicsSettings settings = new PlanetGraphicsSettings();
        JSONObject planetSettingJSON = new JSONObject(jsonMap);
        settings.name = (String)jsonMap.get("name");
        settings.minScale = ((Number) jsonMap.get("minScale")).floatValue();
        settings.maxScale = ((Number) jsonMap.get("maxScale")).floatValue();
        settings.minAxisTilt = ((Number) jsonMap.get("minAxisTilt")).floatValue();
        settings.maxAxisTilt = ((Number) jsonMap.get("maxAxisTilt")).floatValue();
        settings.atmosphere = (String) jsonMap.get("atmosphere");
        settings.clouds = (String) jsonMap.get("clouds");

        List<Map> textureList = (List<Map>) jsonMap.get("textures");
        for( Map textureProperties : textureList) {
            String texture = (String) textureProperties.get("texture");
            List colorList = (List) textureProperties.get("colors");
            String minRotation = (String) textureProperties.get("minRotation");
            String maxRotation = (String) textureProperties.get("maxRotation");

            // solve colors by ColorSet
            Color[] colors = new Color[colorList.size()];
            int i =0;
            for( Object colorName : colorList) {
                Color color = colorset.find((String) colorName);
                if( color == null) {
                    logger.log(Level.WARNING, "Could not find Color '"+colorName+"' in ColorSet");
                }
                colors[i] = color;
                i++;
            }
            settings.addTextureSetting( getFloat(minRotation), getFloat(maxRotation), texture, colors );
        }
        return settings;
    }

    private List<Probability> jsonToProbability(List<Map> jsonProbabilities) {
        List<Probability> probabilities = new ArrayList<>();
        for( Map probabilityMap : jsonProbabilities) {
            Number weight = (Number) probabilityMap.get("weight");
            String planetTypeName = (String) probabilityMap.get("type");
            String archetype = (String) probabilityMap.get("archetype");

            Probability probability = new Probability();
            probability.setWeight(weight.floatValue());
            probability.setType(planetTypeName);
            probability.setSubtype(archetype);

            probabilities.add(probability);
        }
        return probabilities;
    }

    private float getFloat(String number) {
        if ( number == null) return 0;
        if ( number.isEmpty()) return 0;
        else return Float.valueOf(number);
    }
}
