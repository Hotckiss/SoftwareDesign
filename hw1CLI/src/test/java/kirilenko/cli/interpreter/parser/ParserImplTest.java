package kirilenko.cli.interpreter.parser;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.commands.implementation.*;
import kirilenko.cli.exceptions.ParseException;
import kirilenko.cli.interpreter.substitutor.Substitutor;
import kirilenko.cli.interpreter.substitutor.SubstitutorImpl;
import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Parser test class
 */
public class ParserImplTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * Parse echo command test
     */
    @Test
    public void parseEcho() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        testEnvironment.setVariable("t", "text");
        AbstractCommand result = parser.parse(substitutor.substitute("echo $t", testEnvironment));
        assertTrue(result instanceof EchoCommand);

        List<String> executionResult = result.execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(1, executionResult.size());
        assertEquals("text", executionResult.get(0));
    }

    /**
     * Parse echo with substitution test
     */
    @Test
    public void parseEchoFromVariables() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        testEnvironment.setVariable("a", "ec");
        testEnvironment.setVariable("b", "ho");
        testEnvironment.setVariable("c", "xxx");
        AbstractCommand result = parser.parse(substitutor.substitute("$a$b $c", testEnvironment));
        assertTrue(result instanceof EchoCommand);

        List<String> executionResult = result.execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(1, executionResult.size());
        assertEquals("xxx", executionResult.get(0));
    }

    /**
     * Parse pipeline in quotes
     */
    @Test
    public void parseQuotedPipe() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("echo \"xxx|\"", testEnvironment));
        assertTrue(result instanceof EchoCommand);

        List<String> executionResult = result.execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(1, executionResult.size());
        assertEquals("xxx|", executionResult.get(0));
    }

    /**
     * Parse assignment test
     */
    @Test
    public void parseAssign() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("a=5", testEnvironment));
        assertTrue(result instanceof AssignmentCommand);

        CommandResult res = result.execute(Collections.emptyList(), testEnvironment);
        assertEquals("5", testEnvironment.getVariable("a"));
    }

    /**
     * Parse pipeline test
     */
    @Test
    public void parsePipes() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("echo 123 | cat | wc", testEnvironment));
        assertTrue(result instanceof PipelineCommand);

        List<String> res = result.execute(Collections.emptyList(), testEnvironment).getOutput();
        assertEquals(3, res.size());
        assertEquals("1", res.get(0));
        assertEquals("1", res.get(1));
        assertEquals("3", res.get(2));
    }

    /**
     * Parse cat command test
     */
    @Test
    public void parseCat() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("cat file.in", testEnvironment));
        assertTrue(result instanceof CatCommand);
    }

    /**
     * Parse exit command test
     */
    @Test
    public void parseExit() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("exit", testEnvironment));
        assertTrue(result instanceof ExitCommand);
    }

    /**
     * Parse pwd command test
     */
    @Test
    public void parsePwd() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("pwd", testEnvironment));
        assertTrue(result instanceof PwdCommand);
    }

    /**
     * Parse wc command test
     */
    @Test
    public void parseWc() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("wc", testEnvironment));
        assertTrue(result instanceof WcCommand);
    }

    /**
     * Parse external command test
     */
    @Test
    public void parseExternal() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("ls -a", testEnvironment));
        assertTrue(result instanceof ExternalCommand);
    }

    /**
     * Parse error because incorrect variable name test
     */
    @Test(expected = ParseException.class)
    public void parseError1() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("''=text", testEnvironment));
    }

    /**
     * Parse error because input was empty test
     */
    @Test(expected = ParseException.class)
    public void parseError2() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("", testEnvironment));
    }

    /**
     * Parse error because pipe was without right side test
     */
    @Test(expected = ParseException.class)
    public void parseError3() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("echo 123 |", testEnvironment));
    }

    /**
     * Parse grep command test
     */
    @Test
    public void parseGrep() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("grep -A xxx yyy"));
        assertTrue(result instanceof GrepCommand);
    }

    /**
     * Parse pipeline with grep test
     */
    @Test
    public void parseGrepInPipes() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand result = parser.parse(substitutor.substitute("echo 12t 155T 122 | grep -i 1*t | wc"));
        assertTrue(result instanceof PipelineCommand);

        List<String> res = result.execute(Collections.emptyList()).getOutput();
        assertEquals(3, res.size());
        assertEquals("2", res.get(0));
        assertEquals("2", res.get(1));
        assertEquals("8", res.get(2));
    }
}