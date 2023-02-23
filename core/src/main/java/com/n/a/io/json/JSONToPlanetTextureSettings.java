package com.n.a.io.json;

import com.badlogic.gdx.graphics.Color;
import com.n.a.game.repository.ColorRepository;
import com.n.a.game.settings.generator.PlanetTextureSetting;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class JSONToPlanetTextureSettings
        extends AbstractJSONConverter
        implements JSONConverter<PlanetTextureSetting, Map>
{

    protected JSONToPlanetTextureSettings( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public PlanetTextureSetting convert(Map textureProperties){
        if ( textureProperties == null ) return null;

        ColorRepository colorRepository = loader.<ColorRepository>getLoadedDataRepository(ColorRepository.class);

        String texture = (String) textureProperties.get("texture");
        List colorList = (List) textureProperties.get("colors");
        String minRotation = (String) textureProperties.get("minRotation");
        String maxRotation = (String) textureProperties.get("maxRotation");
        String name = (String) textureProperties.get("name");

        // solve colors by ColorSet
        Color[] colors = new Color[colorList.size()];
        int i =0;
        for( Object colorName : colorList) {
            Color color = colorRepository.find((String) colorName);
            if( color == null) {
                logger.log(Level.WARNING, "Could not find Color '"+colorName+"' in ColorSet");
            }
            colors[i] = color;
            i++;
        }
        PlanetTextureSetting planetTextureSetting = new PlanetTextureSetting();
        planetTextureSetting.setTexture(texture);
        planetTextureSetting.setMinRotation( getFloat(minRotation) );
        planetTextureSetting.setMaxRotation( getFloat(maxRotation) );
        planetTextureSetting.setColor(colors);
        return planetTextureSetting;
    }

    private float getFloat(String number) {
        if ( number == null) return 0;
        if ( number.isEmpty()) return 0;
        else return Float.valueOf(number);
    }
}
