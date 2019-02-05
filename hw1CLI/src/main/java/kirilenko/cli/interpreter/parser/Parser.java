package kirilenko.cli.interpreter.parser;

import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents language parser after all variables are substituted
 */
public interface Parser {

    /**
     * Parse commands and builds implementation order for commands
     * @return AbstractCommand start command for execution
     * @throws CliException if any error during parsing occurred
     */
    AbstractCommand parse(@NotNull String statement) throws CliException;
}
