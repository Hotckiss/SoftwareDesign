package kirilenko.cli.commands.implementation;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.exceptions.NoSuchDirectoryException;
import kirilenko.cli.utils.Environment;

import java.util.Collections;
import java.util.List;

/**
 * Command that changes current directory.
 */
public class CdCommand extends AbstractCommand {
    /**
     * {@link AbstractCommand#AbstractCommand(List)}
     */
    public CdCommand(List<String> arguments) {
        super(arguments);
    }

    /**
     * Changes current directory.
     *
     * @param input folder to change current directory to
     * @throws CliException if input length isn't one or if input[0] isn't a directory
     */
    @Override
    public CommandResult execute(List<String> input) throws CliException {
        if (arguments.size() > 1) {
            throw new CliException("cd: too many arguments");
        }

        String directory;
        if (arguments.isEmpty()) {
            directory = System.getProperty("user.home");
        } else {
            directory = arguments.get(0);
        }
        try {
            Environment.setCurrentDirectory(directory);
        } catch (NoSuchDirectoryException exception) {
            throw new CliException("cd: " + directory + ": no such directory");
        }

        return new CommandResult(Collections.emptyList());
    }
}
