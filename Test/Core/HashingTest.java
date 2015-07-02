package Core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nika on 04:00, 6/26/2015.
 */
public class HashingTest {

    @Test
    public void testGetHash() throws Exception {
        String text = "asdasd";
        String hash = "85136c79cbf9fe36bb9d05d0639c70c265c18d37";
        assertTrue(Hashing.getHash(text).equals(hash));
        assertTrue(Hashing.getHash("nika").equals("34bff7be484da58a7c244a79ef278630f334a732"));
        assertTrue(Hashing.getHash("34bff7be484da58a7c244a79ef278630f334a732").
                equals("5b11d74db91a9a8835de107c809fa63776151629"));
    }
}