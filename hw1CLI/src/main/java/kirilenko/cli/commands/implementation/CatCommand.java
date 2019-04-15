package kirilenko.cli.commands.implementation;

import kirilenko.cli.CLILogger;
import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.utils.Environment;
import kirilenko.cli.utils.FileIO;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    /**
     * Returns all lines of specified file
     * If two or more files was specified, error will be occurred
     * If cat have no arguments like "echo gradlew | cat"
     * it will simply print input value (in this example result is "gradlew")
     * @param input command input data
     * @return command result that specified above
     */
    @Override
    public CommandResult execute(List<String> input, @NotNull Environment environment) throws CliException {
        if (arguments.size() > 1) {
            CLILogger.INSTANCE.log_error("Too many arguments in cat command");
            throw new CliException("Too many arguments in cat command");
        }

        if (arguments.isEmpty()) {
            CLILogger.INSTANCE.log_info("Cat of piped argument");
            return new CommandResult(input);
        }

        String fileName = arguments.get(0);
        try (InputStream file = new FileInputStream(Environment.getFile(fileName))) {
            return new CommandResult(FileIO.readLines(file));
        } catch (IOException e) {
            CLILogger.INSTANCE.log_error("Unable to read file for cat");
            throw new CliException("Unable to read file for cat");
        }
    }
}
