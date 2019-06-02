package kirilenko.cli.interpreter.tokenizer;

import kirilenko.cli.exceptions.ParseException;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.function.Predicate;

/**
 * Interface that represents tokens extractors from statement
 */
public interface Tokenizer {
    /**
     * Method that extracts token from statement
     * @param queue statement characters queue
     * @param isAllowed predicate that indicates whether current symbol should be in token
     * @param removeQuotedCharacters if true removes quoted characters around token
     * @return parsed language token
     * @throws ParseException if token can not be parsed correctly
     */
    String parseToken(@NotNull Queue<Character> queue,
                      @NotNull Predicate<Character> isAllowed,
                      boolean removeQuotedCharacters) throws ParseException;
}
