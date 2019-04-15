package kirilenko.cli.utils;

import kirilenko.cli.exceptions.NoSuchVariableException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Environment test class
 */
public class EnvironmentTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Get and set variables testing
     */
    @Test
    public void testSetGet() throws Exception {
        testEnvironment.setVariable("t", "test");
        assertEquals("test", testEnvironment.getVariable("t"));
    }

    /**
     * No variable test case
     */
    @Test(expected = NoSuchVariableException.class)
    public void testNoVariable() throws Exception {
        testEnvironment.getVariable("t");
    }
}