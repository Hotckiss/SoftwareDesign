package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;
import kirilenko.cli.commands.AbstractCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pipeline command test
 */
public class PipelineCommandTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * Pipeline echo and wc
     */
    @Test
    public void testEchoWc() throws Exception {
        String t1 = "text1";
        String t2 = "text2      xxx";
        AbstractCommand echo = new EchoCommand(Arrays.asList(t1, t2));
        AbstractCommand wc = new WcCommand(Collections.emptyList());
        List<String> result = new PipelineCommand(Collections.emptyList(), echo, wc)
                .execute(Collections.emptyList())
                .getOutput();

        assertEquals(3, result.size());
        assertEquals("2", result.get(0));
        assertEquals("3", result.get(1));
        assertEquals(String.valueOf(t1.length() + t2.length() + 1), result.get(2));
    }

    /**
     * Pipeline echo, cat and wc
     */
    @Test
    public void testEchoCatWc() throws Exception {
        String t1 = "text1";
        String t2 = "text2      xxx";
        AbstractCommand echo = new EchoCommand(Arrays.asList(t1, t2));
        AbstractCommand cat = new CatCommand(Collections.emptyList());
        AbstractCommand wc = new WcCommand(Collections.emptyList());
        AbstractCommand echoCat = new PipelineCommand(Collections.emptyList(), echo, cat);
        List<String> result = new PipelineCommand(Collections.emptyList(), echoCat, wc)
                .execute(Collections.emptyList())
                .getOutput();

        assertEquals(3, result.size());
        assertEquals("2", result.get(0));
        assertEquals("3", result.get(1));
        assertEquals(String.valueOf(t1.length() + t2.length() + 1), result.get(2));
    }

    /**
     * Pipeline echo, wc and wc
     */
    @Test
    public void testEchoWcWc() throws Exception {
        String t1 = "text1";
        String t2 = "text2      xxx";
        AbstractCommand echo = new EchoCommand(Arrays.asList(t1, t2));
        AbstractCommand wc1 = new WcCommand(Collections.emptyList());
        AbstractCommand wc2 = new WcCommand(Collections.emptyList());
        List<String> result = new PipelineCommand(Collections.emptyList(),
                new PipelineCommand(Collections.emptyList(), echo, wc1),
                wc2).execute(Collections.emptyList()).getOutput();

        assertEquals(3, result.size());
        assertEquals("3", result.get(0));
        assertEquals("3", result.get(1));
        assertEquals("6", result.get(2));
    }
}