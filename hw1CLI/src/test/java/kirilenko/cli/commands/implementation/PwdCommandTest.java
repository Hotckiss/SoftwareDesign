package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pwd command test
 */
public class PwdCommandTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Test pwd command output
     */
    @Test
    public void testPwd() throws Exception {
        String expected = Environment.getCurrentDirectory().normalize().toString();

        List<String> result = new PwdCommand(Collections.emptyList()).execute(Collections.emptyList()).getOutput();
        assertEquals(1, result.size());
        assertEquals(expected, result.get(0));
    }
}