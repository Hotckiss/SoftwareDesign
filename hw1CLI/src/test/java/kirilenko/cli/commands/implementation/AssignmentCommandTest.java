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
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Assign new variable test
     */
    @Test
    public void assignTest() throws Exception {
        new AssignmentCommand(Collections.singletonList("text"), "t").execute(Collections.emptyList());
        assertEquals("text", Environment.getVariable("t"));
    }

    /**
     * Reassign variable test
     */
    @Test
    public void reassignTest() throws Exception {
        new AssignmentCommand(Collections.singletonList("text"), "t").execute(Collections.emptyList());
        new AssignmentCommand(Collections.singletonList("text1"), "t").execute(Collections.emptyList());
        assertEquals("text1", Environment.getVariable("t"));
    }

    /**
     * Get variable that doesn't exist
     */
    @Test(expected = NoSuchVariableException.class)
    public void noSuchVariableTest() throws Exception {
        Environment.getVariable("ex");
    }
}