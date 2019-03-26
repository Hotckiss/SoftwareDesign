package kirilenko.cli.commands.implementation;

import kirilenko.cli.CLILogger;
import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.utils.Environment;
import kirilenko.cli.utils.FileIO;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Command that search for pattern matches in text
 */
public class GrepCommand extends AbstractCommand {
    /**
     * Option that makes search case insensitive if true
     */
    @Option(name="-i", aliases="--ignore case", usage="Ignore case in search")
    private boolean ignoreCaseFlag;

    /**
     * Option that makes search by whole words in lines if true
     */
    @Option(name="-w", aliases="--words", usage="Search only words")
    private boolean wholeWords;

    /**
     * Option that forces to print next N lines after successful match
     */
    @Option(name="-A", aliases="--lines after success", usage="Force print n lines after successful match")
    private int forcePrintLinesNumber;

    /**
     * Arguments that contains pattern and optional file name
     */
    @Argument
    private List<String> extraArgs;

    /**
     * Constructs new grep command with input arguments
     * @param arguments command arguments
     */
    public GrepCommand(List<String> arguments) {
        super(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(List<String> input, @NotNull Environment environment) throws CliException {
        final CmdLineParser parser = new CmdLineParser(this);
        try {
            CLILogger.INSTANCE.log_info("Parse GREP options from args");
            parser.parseArgument(arguments);
        }
        catch (CmdLineException ex) {
            CLILogger.INSTANCE.log_error("Unable to parse command-line options");
            throw new CliException(ex.getMessage());
        }

        if (forcePrintLinesNumber < 0) {
            throw new IllegalArgumentException("incorrect parameter value: negetive number of lines");
        }

        List<String> lines;

        // input file was specified
        if (extraArgs.size() == 2) {
            CLILogger.INSTANCE.log_info("Read input from file: " + extraArgs.get(1));
            try (InputStream inputStream = new FileInputStream(extraArgs.get(1))) {
                lines = FileIO.readLines(inputStream);
            } catch(IOException ex) {
                throw new CliException(ex.getMessage());
            }
        }  else {
            CLILogger.INSTANCE.log_info("Read input from pipe: " + input);
            lines = input;
        }

        List<String> result = new ArrayList<>();
        int patternFlags = ignoreCaseFlag ? Pattern.CASE_INSENSITIVE : 0;
        String patternString = extraArgs.get(0);

        if (wholeWords) {
            patternString = "\\b" + patternString + "\\b";
        }

        Pattern pattern = Pattern.compile(patternString, patternFlags);

        int shouldPrintLinesCount = 0;

        for (String line : lines) {
            boolean matched = pattern.matcher(line).find();

            if (matched || shouldPrintLinesCount > 0) {
                shouldPrintLinesCount = matched ? forcePrintLinesNumber : (shouldPrintLinesCount - 1);
                result.add(line);
            }
        }

        return new CommandResult(result);
    }
}
