package kirilenko.cli.commands.implementation;

import kirilenko.cli.CLILogger;
import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.utils.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command to count lines, words and bytes in input
 * Line break character always interpreted as one byte
 */
public class WcCommand extends AbstractCommand {
    /**
     * Creates new wc command
     * @param arguments command arguments
     */
    public WcCommand(List<String> arguments) {
        super(arguments);
    }

    /**
     * Runs counting lines, words and bytes
     * @param input command input data
     * @return command result with three values of counters
     */
    @Override
    public CommandResult execute(List<String> input) throws CliException {
        List<String> lines = input;
        long fileSize;

        if (!arguments.isEmpty()) {
            File inputFile = new File(arguments.get(0));
            try (InputStream file = new FileInputStream(inputFile)) {
                fileSize = inputFile.length();
                lines = FileIO.readLines(file);
            } catch (IOException e) {
                CLILogger.INSTANCE.log_error("Wc: unable to read target file");
                throw new CliException("Wc: unable to read target file");
            }
        } else {
            fileSize = lines.stream().mapToLong(line -> line.getBytes().length).sum();
        }

        // split of empty string is non-empty
        if (lines.isEmpty()) {
            return new CommandResult(Arrays.asList("0", "0", "0"));
        }

        List<String> result = new ArrayList<>();
        // guarantees to interpret any line break character as single byte, easy to test.
        String joinedLinesString = String.join(" ", lines);
        result.add(Integer.toString(lines.size()));
        result.add(Integer.toString(joinedLinesString.trim().split("\\s+").length));
        result.add(Long.toString(fileSize));

        return new CommandResult(result);
    }
}
