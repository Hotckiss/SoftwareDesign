package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Exit command test
 */
public class ExitCommandTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Test that executing command produces exit flag
     */
    @Test
    public void testExit() throws Exception {
        assertTrue(new ExitCommand(Collections.emptyList()).execute(Collections.emptyList(), testEnvironment).isExit());
    }
}