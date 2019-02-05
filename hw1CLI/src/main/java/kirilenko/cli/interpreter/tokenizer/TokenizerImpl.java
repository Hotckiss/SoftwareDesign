package kirilenko.cli.interpreter.tokenizer;

import kirilenko.cli.exceptions.ParseException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Implementation of Tokenizer for simple CLI
 */
public final class TokenizerImpl implements Tokenizer {
    /**
     * CLI names allowed characters
     */
    public static final Set<Character> NAME_CHARACTERS = new HashSet<>();

    /**
     * CLI names finish characters
     */
    public static final Set<Character> WORD_BREAK_CHARACTERS = new HashSet<>();

    private final Logger logger = Logger.getLogger(TokenizerImpl.class.getName());

    static {
        for (Character ch : "abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ0123456789-_:/.".toCharArray()) {
            NAME_CHARACTERS.add(ch);
        }

        WORD_BREAK_CHARACTERS.addAll(Arrays.asList(' ', '"', '\n', '\''));
    }

    /**
     * {@inheritDoc}
     */
    public String parseToken(@NotNull Queue<Character> queue,
                             @NotNull Predicate<Character> isAllowed,
                             boolean removeQuotedCharacters) throws ParseException {
        StringBuilder builder = new StringBuilder();

        try {
            if (removeQuotedCharacters) {
                queue.remove();
                logger.info("TokenizerImpl: front quote removed successfully");
            }
            while (!queue.isEmpty() && isAllowed.test(queue.element())) {
                builder.append(queue.poll());
            }
            if (removeQuotedCharacters) {
                queue.remove();
                logger.info("TokenizerImpl: back quote removed successfully");
            }
        } catch (NoSuchElementException e) {
            logger.warning("TokenizerImpl: unexpected end of statement");
            throw new ParseException("Unexpected end of statement");
        }

        return builder.toString();
    }
}
