package com.n.a.io.json;

import com.n.a.XYZException;
import com.n.a.game.settings.generator.PlanetTextureSetting;
import com.n.a.game.space.ScanCategory;
import com.n.a.game.planet.PlanetTrait;

import java.util.List;
import java.util.Map;

/**
 * Converts a key/value Map to a {@link PlanetTrait}.
 * Dependency: ColorRepository -> TextureSettingsConverter -> PlanetTraits
 */
public class JSONToPlanetTraits
        extends AbstractJSONConverter
        implements JSONConverter<PlanetTrait, Map<String, Object>> {

    protected JSONToPlanetTraits( RepositoryLoader repositoryLoader){
        super(repositoryLoader);
    }

    @Override
    public PlanetTrait convert(Map<String, Object> input) {
        JSONToPlanetTextureSettings textureSettingsConverter = this.loader.getLoadedConverter(JSONToPlanetTextureSettings.class);
        PlanetTrait trait = new PlanetTrait();
        trait.setId( (String) input.get("id") );
        trait.setName( (String) input.get("name"));
        trait.setIcon( (String) input.get("icon"));
        trait.setDiscoveryDescription( (String) input.get("discoveryDescription"));
        trait.setDiscoveryPoints( ((Number) input.get("discoveryPoints")).intValue() );

        if( input.containsKey("textureSettings")) {
            for (Object textureSettingMap : (List) input.get("textureSettings")) {
                PlanetTextureSetting textureSetting = textureSettingsConverter.convert((Map) textureSettingMap);
                trait.addTextureSetting(textureSetting);
            }
        }

        String category = (String) input.get("category");
        trait.setCategory( ScanCategory.valueOf(category));
        Object modifiers = input.get("modifiers");
        if( modifiers instanceof Map) {
            trait.setModifiers((Map) input.get("modifiers"));
        } else {
            throw new XYZException(XYZException.ErrorCode.E2010, "Expected a Map for field 'modifiers' in entry:  " + trait.getId());
        }
        return trait;
    }
}
