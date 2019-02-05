package kirilenko.cli.commands;

import kirilenko.cli.exceptions.CliException;

import java.util.List;

/**
 * Class that represents utils command properties.
 * Any command must have list of arguments that can be empty
 * Also command should have method to execute, which will return some result
 * or ABORTED if command completed with errors
 */
public abstract class AbstractCommand {
    /**
     * List of command arguments
     */
    protected final List<String> arguments;

    /**
     * Constructs new command with specific arguments
     * @param arguments list of command arguments
     */
    public AbstractCommand(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Abstract method that is specific for each command
     * @param input command input data
     * @return command execution result
     * @throws CliException if any error occurred
     */
    public abstract CommandResult execute(List<String> input) throws CliException;
}
