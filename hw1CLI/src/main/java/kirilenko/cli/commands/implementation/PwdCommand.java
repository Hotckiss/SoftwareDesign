package kirilenko.cli.commands.implementation;

import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.commands.AbstractCommand;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Command that prints current directory
 */
public class PwdCommand extends AbstractCommand {
    /**
     * Creates new pwd command with input arguments
     * @param arguments command arguments
     */
    public PwdCommand(List<String> arguments) {
        super(arguments);
    }

    /**
     * Generates command result with current directory string
     * @param input command input data
     * @return command result with current directory
     */
    @Override
    public CommandResult execute(List<String> input) {
        String path = Paths.get("").toAbsolutePath().toString();

        return new CommandResult(Collections.singletonList(path));
    }
}
