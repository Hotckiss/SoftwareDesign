package kirilenko.cli.commands.implementation;

import kirilenko.cli.CLILogger;
import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.utils.Environment;
import kirilenko.cli.utils.FileIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for unsupported commands that may be executed outside
 */
public class ExternalCommand extends AbstractCommand {
    /**
     * Creates new external command with specified name and arguments
     * @param commandName external command name
     * @param arguments external command arguments
     */
    public ExternalCommand(String commandName, List<String> arguments) {
        super(ExternalCommand.makeArguments(commandName, arguments));
    }

    /**
     * Executes external command in sub-process
     * @param input command input data
     * @return external command execution result
     * @throws CliException if error in sub-process occurred
     */
    @Override
    public CommandResult execute(List<String> input) throws CliException {
        try {
            String[] args = arguments.toArray(new String[0]);
            Process commandProcess = Runtime.getRuntime().exec(args, null, Environment.getCurrentDirectory().toFile());
            FileIO.writeLines(input, commandProcess.getOutputStream());
            commandProcess.getOutputStream().close();
            return new CommandResult(FileIO.readLines(commandProcess.getInputStream()));
        } catch (IOException e) {
            CLILogger.INSTANCE.log_error("Error in external sub-process");
            return CommandResult.ABORT;
        }
    }

    /**
     * Concat command with it's arguments
     * @param commandName external command name
     * @param args external command arguments
     * @return merged command name with arguments
     */
    private static List<String> makeArguments(String commandName, List<String> args) {
        List<String> result = new ArrayList<>();
        result.add(commandName);
        result.addAll(args);

        return result;
    }
}
