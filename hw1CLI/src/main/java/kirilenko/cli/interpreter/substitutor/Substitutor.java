package kirilenko.cli.interpreter.substitutor;

import kirilenko.cli.exceptions.NoSuchVariableException;
import kirilenko.cli.exceptions.ParseException;
import kirilenko.cli.utils.Environment;
import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents substitution of variables in different languages
 */
public interface Substitutor {
    /**
     * Substitute variables in statement
     * @param statement input statement
     * @param environment current CLI environment
     * @return statement with substituted variables
     */
    String substitute(@NotNull String statement, @NotNull Environment environment) throws ParseException, NoSuchVariableException;
}
