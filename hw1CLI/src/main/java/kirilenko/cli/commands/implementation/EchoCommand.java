package kirilenko.cli.commands.implementation;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command that simply prints it's arguments
 */
public class EchoCommand extends AbstractCommand {
    /**
     * Constructs new echo command
     * @param arguments arguments to print
     */
    public EchoCommand(List<String> arguments) {
        super(arguments);
    }

    /**
     * Returns all list of arguments as a result
     * @param input command input data
     * @return command result with all passed arguments
     */
    @Override
    public CommandResult execute(List<String> input, @NotNull Environment environment) {
        if (arguments.isEmpty()) {
            return new CommandResult(Collections.emptyList());
        }

        return new CommandResult(new ArrayList<>(Collections.singletonList(String.join(" ", arguments))));
    }
}
