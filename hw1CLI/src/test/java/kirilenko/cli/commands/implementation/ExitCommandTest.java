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
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Test that executing command produces exit flag
     */
    @Test
    public void testExit() throws Exception {
        assertTrue(new ExitCommand(Collections.emptyList()).execute(Collections.emptyList()).isExit());
    }
}