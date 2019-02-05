package kirilenko.cli.interpreter.substitutor;

import kirilenko.cli.exceptions.NoSuchVariableException;
import kirilenko.cli.exceptions.ParseException;
import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents substitution of variables in different languages
 */
public interface Substitutor {
    /**
     * Substitute variables in statement
     * @param statement input statement
     * @return statement with substituted variables
     */
    String substitute(@NotNull String statement) throws ParseException, NoSuchVariableException;
}
