package ua.yandex.tuple;

import org.junit.Assert;
import org.junit.Test;

public class TupleTest {

    @Test
    public void testEquals() {
        Tuple k = new Tuple("kho", 1);
        Assert.assertFalse(k.equals(null));
    }
}