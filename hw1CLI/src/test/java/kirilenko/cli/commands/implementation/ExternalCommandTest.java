package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * External command test
 */
public class ExternalCommandTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Test constructing echo command external
     */
    @Test
    public void testExternal() throws Exception {
        List<String> result;

        if (SystemUtils.IS_OS_WINDOWS) {
            result = new ExternalCommand("cmd.exe", Arrays.asList("/c", "echo", "text"))
                    .execute(Collections.emptyList(), testEnvironment).getOutput();
        } else {
            result = new ExternalCommand("echo", Collections.singletonList("text"))
                    .execute(Collections.emptyList(), testEnvironment).getOutput();
        }
        assertEquals(1, result.size());
        assertEquals("text", result.get(0));
    }
}