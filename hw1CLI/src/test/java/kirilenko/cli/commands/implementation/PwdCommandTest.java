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
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Test pwd command output
     */
    @Test
    public void testPwd() throws Exception {
        String expected = Paths.get("").toAbsolutePath().toString();

        List<String> result = new PwdCommand(Collections.emptyList()).execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(1, result.size());
        assertEquals(expected, result.get(0));
    }
}