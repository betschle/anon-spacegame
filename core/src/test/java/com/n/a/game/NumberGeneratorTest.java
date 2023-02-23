package com.n.a.game;

import com.badlogic.gdx.utils.Array;
import com.n.a.util.sequences.NumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class NumberGeneratorTest {

    private NumberGenerator numberGenerator = new NumberGenerator(123);

    @Test
    public void probability_100Percent() {
        for( int i =0; i < 1000; i++) {
            boolean result = this.numberGenerator.isTrue(100);
            Assertions.assertTrue(result, "Must be true! " + result);
        }
    }

    @Test
    public void probability_0Percent() {
        for( int i =0; i < 1000; i++) {
            boolean result = this.numberGenerator.isTrue(0);
            Assertions.assertTrue(!result, "Must be false! " + result);
        }
    }

    @Test
    public void probability_average() {
        int falseCount = 0;
        int trueCount = 0;
        int totalIterations = 127000;
        for( int i =0; i < totalIterations; i++) {
            boolean result = this.numberGenerator.isTrue(50);
            if( result ) {
                trueCount++;
            } else {
                falseCount++;
            }
        }
        // should be around 50:50 rate, with a +/- 5 % variance
        float threshold = (totalIterations/2f) * 1.04f;
        Assertions.assertTrue(falseCount < threshold, "Count of false does not meet +/- 5 % variance: " + falseCount);
        Assertions.assertTrue(trueCount < threshold, "Count of true does not meet +/- 5 % variance: " + trueCount);
    }

    @Test
    public void randomInteger_inRange() {
        for( int i =0; i < 10; i++) {
            int randomInteger = this.numberGenerator.getRandomInteger(20, 0);
            Assertions.assertTrue(randomInteger < 20, "Random Integer is not smaller than 20: " + randomInteger);
        }
    }

    @Test
    public void randomFloat_inRange() {
        for( int i =0; i < 10; i++) {
            float randomFloat = this.numberGenerator.getRandomFloat(5.5f, 0f);
            Assertions.assertTrue(randomFloat < 5.5f, "Random Float is not smaller than 20: " + randomFloat);
        }
    }

    @Test
    public void randomInteger_MinLargerThanMax() {
        try {
            int randomInteger = this.numberGenerator.getRandomInteger(0, 12345);
            Assertions.fail("Exception expected!");
        } catch (Exception e) {
            Assertions.assertTrue( e.getMessage().contains("larger than") );
            e.printStackTrace();
        }
    }

    @Test
    public void randomFloat_MinLargerThanMax() {
        try {
            float randomFloat = this.numberGenerator.getRandomFloat(0f, 12345f);
            Assertions.fail("Exception expected!");
        } catch (Exception e) {
            Assertions.assertTrue( e.getMessage().contains("larger than") );
            e.printStackTrace();
        }
    }

    @Test
    public void randomEntry_GdxArray() {
        Array array = new Array();
        array.add("a", "b", "c");

        Object entry = this.numberGenerator.getRandomEntry(array);
        int index = array.indexOf(entry, true);
        Assertions.assertTrue( index >= 0);
    }

    @Test
    public void randomEntry_JavaArray() {
        String[] array = new String[] {"a", "b", "c"};
        String entry = this.numberGenerator.<String>getRandomEntry(array);
        boolean fail = true;
        for( String element : array) {
            if( element.equals(entry))  {
                fail = false;
            }
        }
        if( fail ) Assertions.fail("Random Element not part of source array!");
    }

    @Test
    public void randomEntry_ArrayList() {
        ArrayList<String> array = new ArrayList<>();
        array.add("a");
        array.add("b");
        array.add("c");

        Object entry = this.numberGenerator.getRandomEntry(array);
        int index = array.indexOf(entry);
        Assertions.assertTrue( index >= 0);
    }
}
