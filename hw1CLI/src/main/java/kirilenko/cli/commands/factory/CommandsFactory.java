package kirilenko.cli.commands.factory;

import kirilenko.cli.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface of commands generator
 * To add new command to CLI you should add it creation in factory
 * and then it will be parsed correctly
 */
public interface CommandsFactory {
    /**
     * Method that generates command specified with name
     * with input arguments list
     * @param name command name
     * @param arguments command arguments
     * @return command specified by this name with input arguments
     */
    AbstractCommand build(@NotNull String name,
                          @NotNull List<String> arguments);
}
