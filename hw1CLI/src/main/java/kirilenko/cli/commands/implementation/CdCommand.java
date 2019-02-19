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
        if (input.size() > 1) {
            throw new CliException("cd: too many arguments");
        }

        if (!input.isEmpty()) {
            try {
                Environment.setCurrentDirectory(input.get(0));
            } catch (NoSuchDirectoryException exception) {
                throw new CliException("cd: " + input.get(0) + ": no such directory");
            }
        }

        return new CommandResult(Collections.emptyList());
    }
}
