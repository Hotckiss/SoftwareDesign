package kirilenko.cli.commands.implementation;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Command that exit from CLI
 */
public class ExitCommand extends AbstractCommand {
    /**
     * Creates new exit command
     * @param arguments list of arguments that will be ignored now
     */
    public ExitCommand(List<String> arguments) {
        super(arguments);
    }

    /**
     * Returns result with exit signal
     * @param input command input data that will be ignored
     * @return exit command result
     */
    @Override
    public CommandResult execute(List<String> input, @NotNull Environment environment) {
        return CommandResult.EXIT;
    }
}
