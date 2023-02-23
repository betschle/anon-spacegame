package com.n.a.util;

import com.n.a.util.sequences.NumberGenerator;
import com.n.a.util.sequences.RomanNumeralConverter;

import java.util.*;

/**
 * Functions in a way that it patches two words or syllables together to form a new word.
 * E.g. the two sets with the words {Alpha, Beta, Omega} and {Pollux, Centauri} may result in {Alpha Pollux, Beta Centauri}
 * Likewise, the two sets with the syllables { cam, kap } and {um, tam, pa} may result in the words {camtam, campa, kaptam ...}
 * Multiple NameRandomizers may be chained together with other NameRandomizers or String/Number Sequence Generators.
 */
public class NameRandomizer {
    /** First set of words or syllables. */
    private List<String> firstSet = new ArrayList<>(); // read this from a file
    /** Second set of words or syllables. */
    private List<String> secondSet = new ArrayList<>(); // read this from a file
    private NumberGenerator numberGenerator;
    /** Counter for duplicate names, these will have roman numerals added. */
    private Counter<String> nameCounter = new Counter<>();
    private RomanNumeralConverter romanNumeralConverter = new RomanNumeralConverter();
    /** If a single empty white space between generated words or syllables should be used */
    private boolean useSpacing = false;

    public NumberGenerator getNumberGenerator() {
        return numberGenerator;
    }

    public void setNumberGenerator(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public boolean isUseSpacing() {
        return useSpacing;
    }

    public void setUseSpacing(boolean useSpacing) {
        this.useSpacing = useSpacing;
    }

    public void setFirstSet(List<String> firstSet) {
        this.firstSet = firstSet;
    }

    public void setSecondSet(List<String> secondSet) {
        this.secondSet = secondSet;
    }

    /**
     * Generates a name using first and second sets of
     * words/syllables. Also adds a roman numeral to distinguish
     * already existing names from each other.
     *
     * @return
     */
    public String generateName() {
        String name = this.combineName();
        this.nameCounter.addOne(name);
        return name + " " +  romanNumeralConverter.toRoman( this.nameCounter.get(name) );
    }

    private String combineName() {
        String part1 = numberGenerator.getRandomEntry(firstSet);
        String part2 = numberGenerator.getRandomEntry(secondSet);
        return part1 + (useSpacing && !part2.isEmpty()? " " : "") +  (part2.isEmpty() ? "" : part2);
    }

    private void getRomanNumeral() {

    }

    public static void main( String[] args) {
        // TODO remove this once I introduced NameRandomizer SubSets for PlanetSubTypes
        galaxyNames();
        System.out.println("_________");
        starNames();
        System.out.println("_________");
        fantasyNames();
        System.out.println("_________");
        doubleJoinedFantasyNames();

    }


    public static void doubleJoinedFantasyNames() {

        // for planet Names
        // I like this a lot because it gives you a lot of customization
        NameRandomizer randomizer = new NameRandomizer();
        randomizer.setUseSpacing(false);
        randomizer.setFirstSet( Arrays.asList(
                "Arman", "An", "Ala", "Ath", "Ari",
                "Bor", "Buho", "Bium", "Bim",
                "Canda", "Cor", "Cepro", "Can", "Cron",
                "Dan", "Danca", "Don","Di", "Delem", "Den",
                "Espa", "Era", "En",
                "Lyde", "Lup", "Lan",
                "Is", "Ir", "In", "Illi",
                "Hy","Hyph", "Hispa", "Her",
                "Phi", "Peg", "Pri", "Phe",
                "Mac", "Mor", "Mat", "Mete", "Mega",
                "Tau", "Tan", "Tu", "The", "Tha", "Thamega",
                "Ur", "Gob", "Piscu",
                "Od", "Oln", "Osmos", "Orsa",
                "Scor", "Spar", "Ser", "Sus", "Spa",
                "Vega","Veng", "Vultu", "Vul" ));
        randomizer.setSecondSet( Arrays.asList("vus", "os", "or", "um", "na", "us", "k", "un", "is", "am", "er", "da", "du", "eman", "ros", "dor", "leum", "an", "shan", "ium") );

        List<String> fantasyNames = new ArrayList<>();
        for( int i =0; i < 100; i++) {
            fantasyNames.add( randomizer.generateName() );
        }

        NameRandomizer randomizerLevel1 = new NameRandomizer();
        randomizerLevel1.setUseSpacing(true);
        randomizerLevel1.setFirstSet( Arrays.asList("Alpha", "Beta", "Gamma", "Delta", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda",
                "Omicron", "Rho", "Sigma", "Tau", "Epsilon", "Omega") );
        randomizerLevel1.setSecondSet( fantasyNames );
        for( int i =0; i < 200; i++) {
            System.out.println( randomizerLevel1.generateName() );
        }
    }

    public static void fantasyNames() {
        NameRandomizer randomizer = new NameRandomizer();
        randomizer.setUseSpacing(false);
        randomizer.setFirstSet( Arrays.asList("Canda", "Tau", "Ur", "Gob", "Peg", "Ala", "Os", "Orsa", "Arman", "Vultu", "Vin","Vul", "Lym", "Lyde", "Piscu", "Her",
                "Cor", "Scor", "Dan", "Dancat", "Den", "Jun", "Ceph", "Cepro", "Hy","Hyph", "Hispan", "Serb", "Lup", "Osmos", "Sol", "Tud", "Pun" ));
        randomizer.setSecondSet(Arrays.asList("or", "um", "na", "us", "k", "un", "is", "am", "er", "a", "u", "man", "entu", "leum", "an", "din") );
        for( int i =0; i < 100; i++) {
            System.out.println( randomizer.generateName() );
        }
    }

    public static void galaxyNames() {
        NameRandomizer randomizer = new NameRandomizer();
        randomizer.setUseSpacing(true);
        randomizer.setFirstSet( Arrays.asList("Canis", "Taurus", "Ursa", "Pegasus", "Orion", "Pegasus", "Aries", "Vulpus", "Lyra", "Pisces", "Hercules",
                "Corvus", "Scorpius", "Draco", "Cepheus", "Hydra", "Serpens", "Lupus", "Corona", "Vela", "Cygnus", "Lacerta",
                "Fenestra", "Ferrum", "Iridium", "Cuprum", "Carina", "Grus", "Spatula", "Polis", "Musica", "Mergana", "Libertad",
                "Usted", "Angus") );
        randomizer.setSecondSet( Arrays.asList("", "", "Major", "Minor", "Primus", "Secundus", "Tertius", "Borealis", "Galacticus" ) );
        for( int i =0; i < 100; i++) {
            System.out.println( randomizer.generateName() );
        }
    }

    public static void starNames() {
        NameRandomizer randomizer = new NameRandomizer();
        randomizer.setUseSpacing(true);
        randomizer.setFirstSet( Arrays.asList("Alpha", "Beta", "Gamma", "Delta", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda",
                "Omicron", "Rho", "Sigma", "Tau", "Epsilon", "Omega") );
        randomizer.setSecondSet(Arrays.asList("Pollux", "Centaur", "Ursa", "Eridanus", "Cassiopeia", "Capella", "Castor",
                "Orion", "Atlas", "Deneb", "Diadem", "Copernicus", "Fafnir", "Ishtar", "Argentium") );
        for( int i =0; i < 100; i++) {
            System.out.println( randomizer.generateName() );
        }
    }

}


