package kirilenko.cli.commands.implementation;

import kirilenko.cli.interpreter.parser.ParserImpl;
import kirilenko.cli.utils.Environment;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

/**
 * Special command that sets new variables in CLI
 */
public final class AssignmentCommand extends AbstractCommand {
    /**
     * Any variable must have name
     */
    private final String variableName;
    private final Logger logger = Logger.getLogger(AssignmentCommand.class.getName());

    /**
     * Constructs assignment command with specified variable name
     * @param arguments statement that will evaluate to variable value
     * @param variableName variable name
     */
    public AssignmentCommand(List<String> arguments,
                             @NotNull String variableName) {
        super(arguments);
        this.variableName = variableName;
    }

    /**
     * Assign value to variable name in environment
     * Ignores input because assignment should not have it
     * @param input command input data
     * @return result of command
     */
    @Override
    public CommandResult execute(List<String> input) {
        if (arguments.size() != 1) {
            logger.info("Assign of multiple values, aborted ");
            return CommandResult.ABORT;
        }

        Environment.setVariable(variableName, arguments.get(0));
        return CommandResult.EMPTY;
    }
}
