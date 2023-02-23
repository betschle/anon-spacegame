package com.n.a.game;

import com.n.a.util.sequences.NumberGenerator;
import com.n.a.util.sequences.StringGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringGeneratorTest {

    private NumberGenerator numberGenerator = new NumberGenerator(123);
    private StringGenerator stringGenerator = new StringGenerator( this.numberGenerator);

    @Test
    public void randomString() {
        int length = 20;
        StringBuilder randomString = stringGenerator.getRandomString(length);
        Assertions.assertTrue(randomString.length() == length, "Length is not equal"  );
    }

    @Test
    public void changedCharacterSet_RandomString() {
        int length = 10;
        this.stringGenerator.setCharacterSet("ABC");
        StringBuilder randomString = stringGenerator.getRandomString(length);

        Assertions.assertTrue( randomString.toString().contains("A") || randomString.toString().contains("B") || randomString.toString().contains("C"));
        Assertions.assertTrue(randomString.length() == length, "Length is not equal of random string " + randomString   );
    }

    @Test
    public void changedCharacterSet_Empty() {
        int length = 10;
        try {
            this.stringGenerator.setCharacterSet("");
            Assertions.fail("Exception expected! Character set can never be empty");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue( e.getMessage().contains("cannot be") );
            e.printStackTrace();
        }
    }

    @Test
    public void changedCharacterSet_Null() {
        int length = 10;
        try {
            this.stringGenerator.setCharacterSet(null);
            Assertions.fail("Exception expected! Character set can never be null");
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue( e.getMessage().contains("cannot be") );
            e.printStackTrace();
        }
    }
}
