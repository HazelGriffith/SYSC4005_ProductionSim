package base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomNumberGeneratorTest {
    private RandomNumberGenerator r;

    @Before
    public void setUp() throws Exception {
        r = new RandomNumberGenerator(17,43,100,27,1.5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void generateRandomNumber() {
        assertEquals(0.02, r.generateRandomNumber(), 0.005);
        assertEquals(0.77, r.generateRandomNumber(), 0.005);
        assertEquals(0.52, r.generateRandomNumber(), 0.005);
    }

    @Test
    public void generateRandomVariate() {
        assertEquals(2.6080, r.generateRandomVariate(), 0.005);
        assertEquals(0.1742, r.generateRandomVariate(), 0.005);
        assertEquals(0.4359, r.generateRandomVariate(), 0.005);
    }
}