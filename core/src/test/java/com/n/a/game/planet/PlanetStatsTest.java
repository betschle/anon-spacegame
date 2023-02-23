package com.n.a.game.planet;

import com.n.a.XYZException;
import com.n.a.game.space.ScanCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PlanetStatsTest {

    private PlanetBaseTrait[] getBaseTraits() {
        PlanetBaseTrait lifeTrait = new PlanetBaseTrait();
        lifeTrait.setClassification(3);
        lifeTrait.setId("Life");
        lifeTrait.setCategory(ScanCategory.BIOLOGIC);

        PlanetBaseTrait temperatureTrait = new PlanetBaseTrait();
        temperatureTrait.setClassification(1);
        temperatureTrait.setId("Temperature");
        temperatureTrait.setCategory(ScanCategory.ATMOSPHERIC);

        PlanetBaseTrait weatherTrait = new PlanetBaseTrait();
        weatherTrait.setClassification(4);
        weatherTrait.setId("Weather");
        weatherTrait.setCategory(ScanCategory.ATMOSPHERIC);
        return new PlanetBaseTrait[] { lifeTrait, temperatureTrait, weatherTrait};
    }

    @Test
    void modifyTraits() {

        Map<String, Integer> modifier1 = new HashMap<>();
        modifier1.put("Weather", 1);
        modifier1.put("Life", -1);
        modifier1.put("Temperature", -5);

        Map<String, Integer> modifier2 = new HashMap<>();
        modifier2.put("Weather", -1);
        modifier2.put("Life", 1);

        PlanetTrait trait1 = new PlanetTrait();
        trait1.setModifiers(modifier1);

        PlanetTrait trait2 = new PlanetTrait();
        trait2.setModifiers(modifier2);

        PlanetStats planetStats = new PlanetStats( getBaseTraits() );

        planetStats.addTrait(trait1);
        Assertions.assertEquals(1,  planetStats.getPlanetRating("Weather") );
        Assertions.assertEquals(-1,  planetStats.getPlanetRating("Life") );
        Assertions.assertEquals(-5,  planetStats.getPlanetRating("Temperature") );

        planetStats.addTrait(trait2);

        Assertions.assertEquals(-5,  planetStats.getPlanetRating("Temperature") );
        Assertions.assertEquals(0,  planetStats.getPlanetRating("Life") );
        Assertions.assertEquals(0,  planetStats.getPlanetRating("Weather") );
    }

    @Test
    void erroneousTraits() {
        Map<String, Integer> modifier1 = new HashMap<>();
        modifier1.put("Weather", null);
        modifier1.put("Life", -1);

        PlanetTrait trait1 = new PlanetTrait();
        trait1.setModifiers(modifier1);

        PlanetStats planetStats = new PlanetStats( getBaseTraits() );
        try {
            planetStats.addTrait(trait1);
        } catch ( XYZException e) {
            Assertions.assertEquals(XYZException.ErrorCode.E0004, e.getErrorCode());
            return; // terminate test
        }
        Assertions.fail("Exception expected");
    }

    @Test
    void discoverTraits() {
        Map<String, Integer> modifier1 = new HashMap<>();
        modifier1.put("Weather", 1);
        modifier1.put("Life", -1);

        PlanetTrait trait1 = new PlanetTrait();
        trait1.setName("Trait 1");
        trait1.setModifiers(modifier1);

        PlanetTrait trait2 = new PlanetTrait();
        trait2.setName("Trait 2");

        PlanetStats planetStats = new PlanetStats( getBaseTraits() );
        planetStats.addTrait(trait1);
        planetStats.addTrait(trait2);

        PlanetTrait nextTrait = planetStats.discoverNextTrait();
        Assertions.assertEquals(trait1.getName(),  nextTrait.getName());

        PlanetTrait nextTrait2 = planetStats.discoverNextTrait();
        Assertions.assertEquals(trait2.getName(),  nextTrait2.getName());
    }


    @Test
    void removeTraits() {
        Map<String, Integer> modifier1 = new HashMap<>();
        modifier1.put("Weather", 1);
        modifier1.put("Life", -1);

        Map<String, Integer> modifier2 = new HashMap<>();
        modifier2.put("Weather", 2);
        modifier2.put("Life", -2);

        PlanetTrait trait1 = new PlanetTrait();
        trait1.setName("Trait 1");
        trait1.setModifiers(modifier1);

        PlanetTrait trait2 = new PlanetTrait();
        trait2.setName("Trait 2");
        trait2.setModifiers(modifier2);

        PlanetStats planetStats = new PlanetStats( getBaseTraits() );
        planetStats.addTrait(trait1);
        planetStats.addTrait(trait2);

        planetStats.removeTrait(trait1);

        Assertions.assertEquals(0,  planetStats.getPlanetRating("Temperature") );
        Assertions.assertEquals(-2,  planetStats.getPlanetRating("Life") );
        Assertions.assertEquals(2,  planetStats.getPlanetRating("Weather") );
    }
}
