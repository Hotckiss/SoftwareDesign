package kirilenko.cli.commands.implementation;

import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Special command for evaluate sequence of commands
 */
public final class PipelineCommand extends AbstractCommand {
    /**
     * Commands sequence thar produces arguments for right commands sequence
     */
    private final AbstractCommand left;
    /**
     * Commands sequence that inputs left commands sequence result as argument
     */
    private final AbstractCommand right;

    /**
     * Creates new pipeline command specified with left and right sequences of commands
     * @param arguments command arguments
     * @param leftCommand left commands sequence
     * @param rightCommand right commands sequence
     */
    public PipelineCommand(List<String> arguments,
                           @NotNull AbstractCommand leftCommand,
                           @NotNull AbstractCommand rightCommand) {
        super(arguments);
        left = leftCommand;
        right = rightCommand;
    }

    /**
     * Executes left commands sequence to produce arguments
     * and then runs right sequence with that arguments
     * @param input command input data
     * @return completed execution result
     * @throws CliException if any execution error occurred
     */
    @Override
    public CommandResult execute(List<String> input) throws CliException {
        CommandResult first = left.execute(input);
        if (first.isAborted() || first.isExit()) {
            return first;
        }

        return right.execute(first.getOutput());
    }
}
