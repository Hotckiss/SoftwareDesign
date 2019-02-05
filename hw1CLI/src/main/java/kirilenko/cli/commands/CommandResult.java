package kirilenko.cli.commands;

import java.util.Collections;
import java.util.List;

/**
 * Class that represents command execution result
 */
public final class CommandResult {
    /**
     * Special command result with empty output for specific commands like assignment
     */
    public static final CommandResult EMPTY = new CommandResult(false, false, Collections.emptyList());

    /**
     * Command result with execution error
     */
    public static final CommandResult ABORT = new CommandResult(true, false, Collections.emptyList());

    /**
     * Command result which has signal to terminate CLI
     */
    public static final CommandResult EXIT = new CommandResult(false, true, Collections.emptyList());

    /**
     * Flag that indicates whether command had errors during execution
     */
    private boolean aborted;

    /**
     * Flag that indicates exiting CLI after command execution
     */
    private boolean exit;

    /**
     * Command output result
     */
    private List<String> output;

    /**
     * Creates idle command result with specified output
     * @param output command output result
     */
    public CommandResult(List<String> output) {
        this(false, false, output);
    }

    /**
     * Getter for error flag of result
     * @return true if command was aborted false otherwise
     */
    public boolean isAborted() {
        return aborted;
    }

    /**
     * Getter for output of command result
     * @return output list of command
     */
    public List<String> getOutput() {
        return output;
    }

    /**
     * Getter for exit flag of result
     * @return true if command should finish execution of CLI false otherwise
     */
    public boolean isExit() {
        return exit;
    }

    private CommandResult(boolean aborted, boolean exit, List<String> output) {
        this.aborted = aborted;
        this.exit = exit;
        this.output = output;
    }
}
