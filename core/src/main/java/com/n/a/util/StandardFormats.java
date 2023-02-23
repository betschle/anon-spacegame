package com.n.a.util;

/**
 * Describes standard formats for measurements, distances or currency.
 * In theory, also be used for more complex formatting with strings.
 */
public enum StandardFormats {

    // TODO write tests for this class
    DISTANCE("%,.1f"),
    PERCENT("%.2f"),
    FACTOR("%.3f"),
    MONEY_ITEM("%,.2f"),
    MONEY_HUD("%,09d");


    private String format = "";

    private StandardFormats( String format) {
        this.format = format;
    }

    public String format(Object... values) {
        if( values == null) throw new IllegalArgumentException("Value cannot be null!");
        return String.format(this.format, values);
    }

    public static void main( String[] args) {
        System.out.println( StandardFormats.MONEY_HUD.format(7000000f) );
        System.out.println( StandardFormats.MONEY_ITEM.format(1300.99f) );
        System.out.println( StandardFormats.DISTANCE.format(45300.1f) );
        System.out.println( StandardFormats.PERCENT.format(56.08898f) );
    }
}
