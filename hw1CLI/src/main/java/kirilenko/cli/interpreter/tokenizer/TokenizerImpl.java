package kirilenko.cli.interpreter.tokenizer;

import kirilenko.cli.CLILogger;
import kirilenko.cli.exceptions.ParseException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * Implementation of Tokenizer for simple CLI
 */
public final class TokenizerImpl implements Tokenizer {
    /**
     * CLI names allowed characters
     */
    public static final Predicate<Character> isNameCharacter = (character -> Character.isLetterOrDigit(character) || "-:_/.".indexOf(character) != -1);

    /**
     * CLI names finish characters
     */
    public static final Predicate<Character> isNotWordBreak = (character -> !Arrays.asList(' ', '"', '\n', '\'').contains(character));

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
                CLILogger.INSTANCE.log_info("TokenizerImpl: front quote removed successfully");
            }
            while (!queue.isEmpty() && isAllowed.test(queue.element())) {
                builder.append(queue.poll());
            }
            if (removeQuotedCharacters) {
                queue.remove();
                CLILogger.INSTANCE.log_info("TokenizerImpl: back quote removed successfully");
            }
        } catch (NoSuchElementException e) {
            CLILogger.INSTANCE.log_error("TokenizerImpl: unexpected end of statement");
            throw new ParseException("Unexpected end of statement");
        }

        return builder.toString();
    }
}
