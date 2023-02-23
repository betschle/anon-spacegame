package com.n.a.util.sequences;

/**
 * An Application Scoped Generator that creates random string sequences, uses a seed
 * and outputs the same values if methods are invoked in the same order with the same seed and
 * the same characterSet.
 */
public class StringGenerator {

    NumberGenerator numberGenerator;
    /** Character set should be numbers. */
    private String characterSet = "0123456789";

    public StringGenerator(NumberGenerator generator) {
        this.numberGenerator = generator;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        if( characterSet == null || characterSet.isEmpty()) throw new IllegalArgumentException("characterSet cannot be empty or null! Input: " + characterSet);
        this.characterSet = characterSet;
    }

    /**
     * Gets a random string sequence using the underlying random seed and charset.
     * @param length the length of the generated sequence
     * @return a string sequence that 1) contains random characters that are elements of characterSet 2) is of the specified length
     */
    public StringBuilder getRandomString(int length ) {
        StringBuilder builder = new StringBuilder();
        for( int i =0; i < length; i++) {
            int randomInteger = numberGenerator.getRandomInteger(characterSet.length(), 0);
            builder.append( characterSet.charAt(randomInteger) );
        }
        return builder;
    }
}
