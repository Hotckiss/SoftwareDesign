package kirilenko.cli.interpreter.tokenizer;

import kirilenko.cli.exceptions.ParseException;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Tokenizer test class
 */
public class TokenizerImplTest {

    /**
     * Parse name token test
     */
    @Test
    public void parseCommandTokenTest() throws ParseException {
        Tokenizer tokenizer = new TokenizerImpl();
        String statement = "echo xxx";
        Queue<Character> queue = new LinkedList<>();

        for (Character c : statement.toCharArray()) {
            queue.add(c);
        }

        assertEquals("echo", tokenizer.parseToken(queue, TokenizerImpl.isNameCharacter, false));
    }

    /**
     * Parse name until special character test
     */
    @Test
    public void parseBeforeDollar() throws ParseException {
        Tokenizer tokenizer = new TokenizerImpl();
        String statement = "a$b";
        Queue<Character> queue = new LinkedList<>();

        for (Character c : statement.toCharArray()) {
            queue.add(c);
        }

        assertEquals("a", tokenizer.parseToken(queue, TokenizerImpl.isNameCharacter, false));
    }
}