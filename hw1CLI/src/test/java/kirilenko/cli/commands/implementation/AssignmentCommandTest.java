package kirilenko.cli.commands.implementation;

import kirilenko.cli.exceptions.NoSuchVariableException;
import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Assign command tests
 */
public class AssignmentCommandTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Assign new variable test
     */
    @Test
    public void assignTest() throws Exception {
        new AssignmentCommand(Collections.singletonList("text"), "t").execute(Collections.emptyList(), testEnvironment);
        assertEquals("text", testEnvironment.getVariable("t"));
    }

    /**
     * Reassign variable test
     */
    @Test
    public void reassignTest() throws Exception {
        new AssignmentCommand(Collections.singletonList("text"), "t").execute(Collections.emptyList(), testEnvironment);
        new AssignmentCommand(Collections.singletonList("text1"), "t").execute(Collections.emptyList(), testEnvironment);
        assertEquals("text1", testEnvironment.getVariable("t"));
    }

    /**
     * Get variable that doesn't exist
     */
    @Test(expected = NoSuchVariableException.class)
    public void noSuchVariableTest() throws Exception {
        testEnvironment.getVariable("ex");
    }
}