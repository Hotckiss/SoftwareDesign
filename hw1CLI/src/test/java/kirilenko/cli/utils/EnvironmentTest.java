package kirilenko.cli.utils;

import kirilenko.cli.exceptions.NoSuchVariableException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Environment test class
 */
public class EnvironmentTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Get and set variables testing
     */
    @Test
    public void testSetGet() throws Exception {
        Environment.setVariable("t", "test");
        assertEquals("test", Environment.getVariable("t"));
    }

    /**
     * No variable test case
     */
    @Test(expected = NoSuchVariableException.class)
    public void testNoVariable() throws Exception {
        Environment.getVariable("t");
    }
}