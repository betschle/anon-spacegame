package com.n.a.io.json;

import com.n.a.game.settings.generator.OrbitSettings;

import java.util.Map;

/**
 * Reads all values contained in orbit settings, even those fields who may not be used due
 * to the OrbitType.
 */
public class JSONToOrbitSettings
        extends AbstractJSONConverter
        implements JSONConverter<OrbitSettings, Map> {

    protected JSONToOrbitSettings( RepositoryLoader loader ) {
        super(loader);
    }

    @Override
    public OrbitSettings convert(Map input) {
        OrbitSettings orbitSettings = new OrbitSettings();
        OrbitSettings.OrbitType orbitType = OrbitSettings.OrbitType.valueOf( (String) input.get("orbitType") );

        orbitSettings.setOrbitType(orbitType);

        if( input.containsKey("maxVelocity") )  orbitSettings.setMaxVelocity( ( (Number ) input.get("maxVelocity") ).floatValue() );
        if( input.containsKey("minVelocity") )  orbitSettings.setMinVelocity( ( (Number ) input.get("minVelocity") ).floatValue() );

        if( input.containsKey("maxWidthMargin") ) orbitSettings.setMaxWidthMargin( ( (Number ) input.get("maxWidthMargin") ).floatValue() );
        if( input.containsKey("minWidthMargin") ) orbitSettings.setMinWidthMargin( ( (Number ) input.get("minWidthMargin") ).floatValue() );
        if( input.containsKey("maxHeightMargin") )  orbitSettings.setMaxHeightMargin( ( (Number ) input.get("maxHeightMargin") ).floatValue() );
        if( input.containsKey("minHeightMargin") ) orbitSettings.setMinHeightMargin( ( (Number ) input.get("minHeightMargin") ).floatValue() );
        if( input.containsKey("x") ) orbitSettings.setStaticPositionX( ( (Number ) input.get("x") ).floatValue() );
        if( input.containsKey("y") ) orbitSettings.setStaticPositionY(((Number) input.get("y")).floatValue());


        return orbitSettings;
    }
}
