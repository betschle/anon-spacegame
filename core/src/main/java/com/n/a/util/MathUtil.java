package com.n.a.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class MathUtil {

    static Random random = new Random();




    /**
     *
     * @param density from 0 - 1
     * @param maxradius the max radius of the asteroid disc
     * @param minradius the min radius of the asteroid disc
     * @return
     */
    public static float getAsteroidCountByDensity(float density, float maxradius, float minradius ) {
        density = MathUtils.clamp(density, 0f, 1f);
        float area = getDiscArea(maxradius, minradius);
        return density * (area / 10000f * density );
    }

    /**
     * Calculates the area of a disc with given max and min radii
     * @param maxradius
     * @param minradius
     * @return
     */
    public static float getDiscArea(float maxradius, float minradius) {
        float maxVol = (float) (Math.PI * Math.pow(maxradius, 2));
        float minVol = (float) (Math.PI * Math.pow(minradius, 2));
        return maxVol - minVol;
    }

    /**
     *
     *
     * @return the angle between the two vectors in radians
     */
    public static float vectorsToAngle(float x1, float y1, float x2, float y2) {
        float delta_x = Math.abs(x1 - x2 );
        float delta_y = Math.abs(y1 - y2);
        return (float) Math.atan2(delta_y, delta_x);
    }
    /**
     *
     * @param v1
     * @param v2
     * @return the angle between the two vectors in radians
     */
    public static float vectorsToAngle(Vector2 v1, Vector2 v2) {
        float delta_x = Math.abs(v1.x - v2.x);
        float delta_y =  Math.abs(v1.y - v2.y);
        return (float) Math.atan2(delta_y, delta_x);
    }

    /**
     * Converts a hex string to a color
     * @param hex
     * @return
     */
    public static Color hexToColor(String hex) {
        String hexColor = hex.replace('#', ' ');
        hexColor = hexColor.trim();
        return Color.valueOf(hexColor);
    }

    /**
     * Converts a color to a hex string
     * @return
     */
    public static String colorToHex(Color color) {
        return "#" + color.toString();
    }

    /**
     * Creates a directional or velocity vector using a rotation in radians.
     *
     * @param length the length (or "speed") of the desired output vector
     * @param radians the rotation in radians
     * @return
     */
    public static Vector2 getDirectionalVector(float length, float radians) {
        double scalex = Math.cos(radians);
        double scaley = Math.sin(radians);
        double velox = length * scalex;
        double veloy = length * scaley;
        return new Vector2((float) velox, (float) veloy);
    }

    /**
     * Gets an angle in radians based on a direction vector
     * @param direction the direction
     * @return angle in radians
     */
    public static float getAngle( Vector2 direction) {
        return (float) Math.atan2(direction.y, direction.x);
    }

    /**
     * Rotates a vector around the origin vector
     * @param toRotate the vector to rotate
     * @param origin the origin of rotation
     * @param angle the angle in radians
     * @return
     */
    public static Vector2 rotate(Vector2 toRotate, Vector2 origin, float angle) {
        float x = (float) (((toRotate.x - origin.x) * Math.cos(angle)) - ((toRotate.y - origin.y) * Math.sin(angle)) + origin.x);
        float y = (float) (((origin.y - toRotate.y) * Math.cos(angle)) - ((toRotate.x - origin.x) * Math.sin(angle)) + origin.y);
        return new Vector2(x, y);
    }
    /**
     * Rotates a vector around the origin 0,0 and returns a new vector
     * @param v
     * @param radians
     * @return
     */
    public static Vector2 rotate(Vector2 v, double radians) {
        Vector2 result = new Vector2();
        float x = (float) ((v.x * Math.cos(radians)) - (v.y * Math.sin(radians)));
        float y = (float) ((v.x * Math.sin(radians)) + (v.y * Math.cos(radians)));
        result.x = x;
        result.y = y;
        return result;
    }

    public static float getRandomFloat(float max, float min) {
        if ( max < min) throw new IllegalArgumentException("Min can't be larger than max!");
        return random.nextFloat() * (max - min) + min;
    }

    /**
     * Gets a random value that is not bound to any seed
     *
     * @param rangedValue
     * @param <T>
     * @return
     */
    public static <T extends Number> T getRandom(Ranged<T> rangedValue) {
        if( rangedValue.getMax() instanceof Double  ) {
            return (T) Double.valueOf( random.nextDouble() * ( (Double)rangedValue.getMax() - (Double)rangedValue.getMin() ) + (Double)rangedValue.getMin() );
        } else
        if( rangedValue.getMax() instanceof Float  ) {
            return (T) Float.valueOf( random.nextFloat() * ( (Float)rangedValue.getMax() - (Float)rangedValue.getMin() ) + (Float)rangedValue.getMin() );
        } else
        if( rangedValue.getMax() instanceof Integer  ) {
            return (T) Integer.valueOf( random.nextInt() * ( (Integer)rangedValue.getMax() - (Integer)rangedValue.getMin() ) + (Integer)rangedValue.getMin() );
        } else
        if( rangedValue.getMax() instanceof Long  ) {
            return (T) Long.valueOf( random.nextLong() * ( (Long)rangedValue.getMax() - (Long)rangedValue.getMin() ) + (Long)rangedValue.getMin() );
        }
        return (T) Integer.valueOf(0);
    }

}
