package kirilenko.cli.commands.implementation;

import kirilenko.cli.utils.FileIO;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.commands.AbstractCommand;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

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
    private final Logger logger = Logger.getLogger(WcCommand.class.getName());

    /**
     * Runs counting lines, words and bytes
     * @param input command input data
     * @return command result with three values of counters
     */
    @Override
    public CommandResult execute(List<String> input) {
        List<String> lines = input;
        if (!arguments.isEmpty()) {
            try (InputStream file = new FileInputStream(arguments.get(0))) {
                lines = FileIO.readLines(file);
            } catch (IOException e) {
                logger.warning("Wc: unable to read target file");
                return CommandResult.ABORT;
            }
        }

        // split of empty string is non-empty
        if (lines.isEmpty()) {
            return new CommandResult(Arrays.asList("0", "0", "0"));
        }

        List<String> result = new ArrayList<>();
        // guarantees to interpret any line break character as single byte, easy to test.
        String joinedLinesString = String.join(" ", lines);
        result.add((Integer.valueOf(lines.size())).toString());
        result.add((Integer.valueOf(joinedLinesString.trim().split("\\s+").length)).toString());
        result.add((Integer.valueOf(joinedLinesString.getBytes().length)).toString());

        return new CommandResult(result);
    }
}
