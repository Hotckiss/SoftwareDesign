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
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Echo empty test
     */
    @Test
    public void emptyTest() throws Exception {
        List<String> result = new EchoCommand(Collections.emptyList()).execute(Collections.emptyList(), testEnvironment).getOutput();
        assertTrue(result.isEmpty());
    }

    /**
     * Echo one arg test
     */
    @Test
    public void testEchoOne() throws Exception {
        String t1 = "text1";
        List<String> result = new EchoCommand(Collections.singletonList(t1)).execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(1, result.size());
        assertEquals(t1, result.get(0));
    }

    /**
     * Echo two args test
     */
    @Test
    public void testEchoTwo() throws Exception {
        String t1 = "text1";
        String t2 = "text2";
        List<String> result = new EchoCommand(Arrays.asList(t1, t2)).execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(1, result.size());
        assertEquals(t1 + " " + t2, result.get(0));
    }
}