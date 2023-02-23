package com.n.a.io.json;

import com.badlogic.gdx.graphics.Color;
import com.n.a.game.repository.ColorRepository;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a ColorRepository using a JSON-String.
 * CommandPattern.
 */
public class JSONToColorRepository
        extends AbstractJSONConverter
        implements JSONRepositoryConverter<ColorRepository, String> {

    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    protected JSONToColorRepository( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public void convertAndStore(ColorRepository repositoryStore, String input) {
        int loaded = 0;
        JSONObject colorSetJson = new JSONObject(input);
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
                    repositoryStore.add(colorName, color);
                    loaded++;
                } catch (NumberFormatException ex) {
                    logger.log(Level.SEVERE, "Could not parse color with name: '" + colorName + "': " + ex.getMessage());
                }
            }
        }
        logger.log(Level.INFO, "Loaded {0} Colors", loaded);
    }

    @Override
    public ColorRepository convert(String json) {
        int loaded = 0;
        ColorRepository colorSet = new ColorRepository();
        this.convertAndStore(colorSet, json);
        return colorSet;
    }
}
