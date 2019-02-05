package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Cat command test
 */
public class CatCommandTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Cat empty test
     */
    @Test
    public void testEmpty() throws Exception {
        List<String> result = new CatCommand(Collections.emptyList()).execute(Collections.emptyList()).getOutput();
        assertTrue(result.isEmpty());
    }

    /**
     * Cat two variables (they may come from pipe)
     */
    @Test
    public void testCat() throws Exception {
        String t1 = "text1";
        String t2 = "text2";
        List<String> result = new CatCommand(Collections.emptyList()).execute(Arrays.asList(t1, t2)).getOutput();
        assertEquals(2, result.size());
        assertEquals(t1, result.get(0));
        assertEquals(t2, result.get(1));
    }

    /**
     * Cat variables from echo using pipe
     */
    @Test
    public void testCatPiped() throws Exception {
        String t = "text";
        List<String> result = new PipelineCommand(Collections.emptyList(),
                new EchoCommand(Collections.singletonList(t)),
                new CatCommand(Collections.emptyList()))
                .execute(Collections.emptyList())
                .getOutput();
        assertEquals(1, result.size());
        assertEquals(t, result.get(0));
    }
}