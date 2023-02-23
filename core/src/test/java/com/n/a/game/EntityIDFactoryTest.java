package com.n.a.game;

import com.n.a.util.sequences.NumberGenerator;
import com.n.a.util.sequences.StringGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntityIDFactoryTest {

    private NumberGenerator numberGenerator = new NumberGenerator(123);
    private StringGenerator stringGenerator = new StringGenerator( this.numberGenerator);
    private EntityIDFactory factory = new EntityIDFactory(stringGenerator);

    @Test
    public void createID() {
        EntityID id = factory.createID();
        Assertions.assertEquals(id.get().length(), factory.getIdLength());
    }

    @Test
    public void createID_WithPrefix() {
        // prefix ID format: PREFIX_XXXXXX
        // use pattern matching to improve this test
        String prefix = "planet";
        EntityID id = factory.createID(prefix);
        Assertions.assertEquals(id.get().length() - prefix.length() - 1, factory.getIdLength());
        Assertions.assertTrue( id.get().contains(prefix));
        Assertions.assertTrue( id.get().contains("_"));
    }

    @Test
    public void createID_WithPrefix_empty() {
        try {
            factory.createID(null);
        } catch( NullPointerException ex) {
            return;
        }
        Assertions.fail("NullPointerException expected");
    }

    @Test
    public void idEqualsToItself() {
        String prefix = "planet";
        EntityID id = factory.createID(prefix);
        Assertions.assertTrue( id.equalsTo( id.get() ));
    }
    @Test
    public void idEqualsItself() {
        EntityID id = factory.createID();
        EntityID otherId = factory.createID();
        Assertions.assertNotEquals(id, otherId);
    }
}
