package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Tests for grep command
 */
public class GrepCommandTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Test for simple match
     */
    @Test
    public void testSimpleMatch() throws Exception {
        Object[] expected = Arrays.asList("text").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("text"))
                .execute(Arrays.asList("texet", "text", "ttt", "xxx", "qqq", "TEXT"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test for multiline match
     */
    @Test
    public void testSimpleMultilineMatch() throws Exception {
        Object[] expected = Arrays.asList("texte", "text").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("text"))
                .execute(Arrays.asList("texte", "text", "ttt", "xxx", "qqq"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test match with star
     */
    @Test
    public void testSimpleMatchStar() throws Exception {
        Object[] expected = Arrays.asList("texet", "text", "ttt", "tsdt", "tt").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("t*t"))
                .execute(Arrays.asList("texet", "text", "ttt", "xxx", "qqq", "tsdt", "tt"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test match with plus
     */
    @Test
    public void testSimpleMatchPlus() throws Exception {
        Object[] expected = Arrays.asList("ttt", "tt").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("t+t"))
                .execute(Arrays.asList("texet", "text", "ttt", "xxx", "qqq", "tsdt", "tt"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test for simple match case insensitive
     */
    @Test
    public void testSimpleMatchCaseInsensitive() throws Exception {
        Object[] expected = Arrays.asList("tExt").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("-i", "teXt"))
                .execute(Arrays.asList("texet", "tExt", "ttt", "xxx", "qqq"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test for multiline match case insensitive
     */
    @Test
    public void testSimpleMultilineMatchCaseInsensitive() throws Exception {
        Object[] expected = Arrays.asList("teXte", "Text").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("-i", "text"))
                .execute(Arrays.asList("teXte", "Text", "ttt", "xxx", "qqq"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test match with star case insensitive
     */
    @Test
    public void testSimpleMatchStarCaseInsensitive() throws Exception {
        Object[] expected = Arrays.asList("teXeT", "Text", "tTt", "tsdt", "tt").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("-i", "t*t"))
                .execute(Arrays.asList("teXeT", "Text", "tTt", "xxx", "qqq", "tsdt", "tt"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test match with plus case insensitive
     */
    @Test
    public void testSimpleMatchPlusCaseInsensitive() throws Exception {
        Object[] expected = Arrays.asList("TTt", "tT").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("-i", "t+t"))
                .execute(Arrays.asList("TeTet", "text", "TTt", "xxx", "qqq", "tsdt", "tT"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test match by word
     */
    @Test
    public void wordMatchTest() throws Exception {
        Object[] expected = Collections.singletonList("so me text").toArray();
        Object[] actual = new GrepCommand(Collections.singletonList("me text"))
                .execute(Collections.singletonList("so me text"), testEnvironment)
                .getOutput().toArray();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test match by word case insensitive
     */
    @Test
    public void byWordMatchTestCaseInsensitive() throws Exception {
        Object[] expected = Arrays.asList("tExt", "teXt", "teXExexEXt").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("-w", "-i", "T(Ex)+T"))
                .execute(Arrays.asList("tExt", "teXt", "teXExexEXt"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }

    /**
     * Test match with force print next lines
     */
    @Test
    public void forcePrintTest() throws Exception {
        Object[] expected = Arrays.asList("tExt", "qqq", "yyy").toArray();
        Object[] actual = new GrepCommand(Arrays.asList("-w", "-i", "-A", "2", "T(Ex)+T"))
                .execute(Arrays.asList("tExt", "qqq", "yyy", "ttt"), testEnvironment)
                .getOutput().toArray();

        assertArrayEquals(expected, actual);
    }
}