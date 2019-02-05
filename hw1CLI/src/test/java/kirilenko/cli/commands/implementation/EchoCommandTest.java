package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Echo command test
 */
public class EchoCommandTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Echo empty test
     */
    @Test
    public void emptyTest() throws Exception {
        List<String> result = new EchoCommand(Collections.emptyList()).execute(Collections.emptyList()).getOutput();
        assertTrue(result.isEmpty());
    }

    /**
     * Echo two args test
     */
    @Test
    public void testEcho() throws Exception {
        String t1 = "text1";
        String t2 = "text2";
        List<String> result = new EchoCommand(Arrays.asList(t1, t2)).execute(Collections.emptyList()).getOutput();
        assertEquals(2, result.size());
        assertEquals(t1, result.get(0));
        assertEquals(t2, result.get(1));
    }
}