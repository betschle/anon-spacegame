package com.n.a.util;

import com.badlogic.gdx.graphics.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MathUtilTest {

    Color whiteColor = Color.WHITE;
    Color blackColor = Color.BLACK;
    Color purpleColor = Color.PURPLE;

    String whiteHex = "#ffffffff";
    String blackHex = "#000000ff";
    String purpleHex = "#a020f0ff";

    @Test
    public void colorToHex() {
        Color[] colors = new Color[] {whiteColor, blackColor, purpleColor};
        String[] hexes = new String[] {whiteHex, blackHex, purpleHex};
        for( int i =0; i < colors.length; i++) {
            String hex = MathUtil.colorToHex(colors[i]);
            Assertions.assertEquals(hexes[i], hex);
        }
    }

    @Test
    public void hexToColor() {
        Color[] colors = new Color[] {whiteColor, blackColor, purpleColor};
        String[] hexes = new String[] {whiteHex, blackHex, purpleHex};
        for( int i =0; i < colors.length; i++) {

            Color convertedColor = MathUtil.hexToColor(hexes[i]);

            Assertions.assertEquals(convertedColor.a, colors[i].a);
            Assertions.assertEquals(convertedColor.r, colors[i].r);
            Assertions.assertEquals(convertedColor.g, colors[i].g);
            Assertions.assertEquals(convertedColor.b, colors[i].b);
        }

    }
}
