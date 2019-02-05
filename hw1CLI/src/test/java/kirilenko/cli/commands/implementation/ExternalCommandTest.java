package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * External command test
 */
public class ExternalCommandTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Test constructing echo command external
     */
    @Test
    public void testExternal() throws Exception {
        List<String> result = new ExternalCommand("echo", Collections.singletonList("text"))
                .execute(Collections.emptyList()).getOutput();
        assertEquals(1, result.size());
        assertEquals("text", result.get(0));
    }
}