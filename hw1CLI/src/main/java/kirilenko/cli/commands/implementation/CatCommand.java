package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.FileIO;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.commands.AbstractCommand;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * Command that prints all contents of specified file
 */
public class CatCommand extends AbstractCommand {
    /**
     * Constructs new cat command with specified files list
     * @param arguments list of files, ignores all files except first
     */
    public CatCommand(List<String> arguments) {
        super(arguments);
    }
    private final Logger logger = Logger.getLogger(CatCommand.class.getName());

    /**
     * Returns all lines of specified file
     * File name is the last argument, all other arguments are ignored
     * If cat have no arguments like "echo gradlew | cat"
     * it will simply print input value (in this example result is "gradlew")
     * @param input command input data
     * @return command result that specified above
     */
    @Override
    public CommandResult execute(List<String> input) {
        if (arguments.isEmpty()) {
            logger.info("Cat of piped argument");
            return new CommandResult(input);
        }

        String fileName = arguments.get(0);
        try (InputStream file = new FileInputStream(fileName)) {
            return new CommandResult(FileIO.readLines(file));
        } catch (IOException e) {
            logger.warning("Unable to read file for cat");
            return CommandResult.ABORT;
        }
    }
}
