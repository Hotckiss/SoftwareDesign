package kirilenko.cli.commands.implementation;

import kirilenko.cli.CLILogger;
import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Special command that sets new variables in CLI
 */
public final class AssignmentCommand extends AbstractCommand {
    /**
     * Any variable must have name
     */
    private final String variableName;

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
    public CommandResult execute(List<String> input) throws CliException {
        if (arguments.size() != 1) {
            CLILogger.INSTANCE.log_info("Assign of multiple values, aborted");
            throw new CliException("Assign of multiple values, aborted");
        }

        Environment.setVariable(variableName, arguments.get(0));
        return CommandResult.EMPTY;
    }
}
