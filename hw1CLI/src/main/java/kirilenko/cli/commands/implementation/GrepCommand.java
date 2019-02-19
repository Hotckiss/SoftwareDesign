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
    private boolean i;

    /**
     * Option that makes search by words in lines if true
     */
    @Option(name="-w", aliases="--words", usage="Search only words")
    private boolean w;

    /**
     * Option that forces to print next N lines after successful match
     */
    @Option(name="-A", aliases="--lines after success", usage="Force print n lines after successful match")
    private int A;

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
    public CommandResult execute(List<String> input) throws CliException {
        final CmdLineParser parser = new CmdLineParser(this);
        try {
            CLILogger.INSTANCE.log_info("Parse GREP options from args");
            parser.parseArgument(arguments);
        }
        catch (CmdLineException ex) {
            CLILogger.INSTANCE.log_error("Unable to parse command-line options");
            throw new CliException(ex.getMessage());
        }

        List<String> lines;

        // input file was specified
        if (extraArgs.size() == 2) {
            CLILogger.INSTANCE.log_info("Read input from file: " + extraArgs.get(1));
            String fileName = extraArgs.get(1);
            try (InputStream inputStream = new FileInputStream(Environment.getFile(fileName))) {
                lines = FileIO.readLines(inputStream);
            } catch(IOException ex) {
                throw new CliException(ex.getMessage());
            }
        }  else {
            CLILogger.INSTANCE.log_info("Read input from pipe: " + input);
            lines = input;
        }

        List<String> result = new ArrayList<>();
        int patternFlags = i ? Pattern.CASE_INSENSITIVE : 0;
        Pattern pattern = Pattern.compile(extraArgs.get(0), patternFlags);

        int shouldPrintLinesCount = 0;

        for (String line : lines) {
            boolean matched = w ? matchesByWord(line, pattern) : pattern.matcher(line).find();

            if (matched || shouldPrintLinesCount > 0) {
                shouldPrintLinesCount = matched ? A : (shouldPrintLinesCount - 1);
                result.add(line);
            }
        }

        return new CommandResult(result);
    }

    /**
     * Search matching in line by words
     * @param line current line for search
     * @param regex pattern
     * @return true if matching was successful false otherwise
     */
    private boolean matchesByWord(@NotNull String line,
                                  @NotNull Pattern regex) {
        String[] words = line.split("\\W+");
        for (String word : words) {
            if (regex.matcher(word).matches()) {
                return true;
            }
        }

        return false;
    }
}
