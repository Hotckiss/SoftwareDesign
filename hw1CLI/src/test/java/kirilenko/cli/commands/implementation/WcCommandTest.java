package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Wc command test
 */
public class WcCommandTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Wc empty test
     */
    @Test
    public void testWcEmpty() throws Exception {
        List<String> result = new WcCommand(Collections.emptyList()).execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(3, result.size());
        assertEquals("0", result.get(0));
        assertEquals("0", result.get(1));
        assertEquals("0", result.get(2));
    }

    /**
     * Wc word test
     */
    @Test
    public void testWc1() throws Exception {
        String t = "text";
        List<String> result = new WcCommand(Collections.emptyList()).execute(Collections.singletonList(t), testEnvironment).getOutput();
        assertEquals(3, result.size());
        assertEquals("1", result.get(0));
        assertEquals("1", result.get(1));
        assertEquals(String.valueOf(t.length()), result.get(2));
    }

    /**
     * Wc two lines test
     */
    @Test
    public void testWc2() throws Exception {
        String t1 = "text1";
        String t2 = "text2   xx";
        List<String> result = new WcCommand(Collections.emptyList()).execute(Arrays.asList(t1, t2), testEnvironment).getOutput();
        assertEquals(3, result.size());
        assertEquals("2", result.get(0));
        assertEquals("3", result.get(1));
        assertEquals(String.valueOf(t1.getBytes().length + t2.getBytes().length), result.get(2));
    }
}