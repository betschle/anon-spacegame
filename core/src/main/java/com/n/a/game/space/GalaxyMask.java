package com.n.a.game.space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * <pre>
 * A Galaxy mask uses an image to find out in which sectors
 * to generate a star system. The image determines the size of the galaxy and the amount of
 * star systems inside of that galaxy.
 *
 * Requirements for the image to be used:
 * - X/Y Image coordinates correspond to sector coordinates.
 * - Image needs to be in grayscales, where the amount of white
 * determines the chance to spawn a star system.
 * </pre>
 */
public class GalaxyMask {

    /** The "brightness" of grayscale probabilities. This works like
     * a frequency parameter. */
    private float brightness = 2.5f;
    private TextureRegion imageRegion;
    private Pixmap image;

    public GalaxyMask(TextureRegion region) {
        this.imageRegion = region;
        Texture texture = region.getTexture();
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        this.image = texture.getTextureData().consumePixmap();
    }

    public TextureRegion getImageRegion() {
        return imageRegion;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    /**
     * Counts an average system count that is created by this
     * GalaxyMask. The higher the iterations the more accurate
     * this calculation is, at the cost of performance for larger images.
     * Two iterations are recommended.
     * @return
     */
    public int countAverage(int iterations) {
        int count = 0;
        for( int i = 0; i < iterations; i++) {
            int regionWidth = imageRegion.getRegionWidth();
            int regionHeight = imageRegion.getRegionHeight();
            for (int x = -regionWidth/2; x < regionWidth/2; x++) {
                for (int y = -regionHeight/2; y < regionHeight/2; y++) {
                    Vector2 centered = this.centerCoordinates(x, y);
                    float probability = this.getProbability( (int)centered.x, (int)centered.y);
                    if (probability > Math.random()) {
                        count++;
                    }
                }
            }
        }
        return count / iterations;
    }

    /**
     * Centers world sector coordinates and maps them to internal Galaxy Coordinates.
     * @param x
     * @param y
     * @return Normalized coordinates in pixels, where coordinates 0|0 is the center of the galaxy.
     */
    public Vector2 centerCoordinates(int x, int y) {
        return new Vector2( x + imageRegion.getRegionWidth() / 2,
                            y + imageRegion.getRegionHeight() / 2);
    }

    /**
     * 0|0 coordinates are the center of the image per definition
     * @param x world sector coordinates, use {@link #centerCoordinates(int, int)} to obtain them.
     * @param y world sector coordinates, use {@link #centerCoordinates(int, int)} to obtain them.
     * @return null of x/y out of range
     */
    public Color getColor(int x, int y) {
        return new Color(this.image.getPixel(
                imageRegion.getRegionX() + x,
                imageRegion.getRegionY() + y));
    }

    /**
     * 0|0 coordinates are the center of the image per definition
     * @param x world sector coordinates, use {@link #centerCoordinates(int, int)} to obtain them.
     * @param y world sector coordinates, use {@link #centerCoordinates(int, int)} to obtain them.
     * @return a probability chance as factor 0-1.
     * 0 if coordinates outside of range
     */
    public float getProbability(int x, int y) {
        Color color = getColor(x, y);
        //convert to grayscale by using color average
        return (color.r + color.g + color.b ) / 3f * brightness;
    }
}
