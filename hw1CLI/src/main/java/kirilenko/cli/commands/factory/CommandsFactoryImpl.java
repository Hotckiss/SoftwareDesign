package kirilenko.cli.commands.factory;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.implementation.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of commands factory.
 * To add new command, specify it's name and construct command instance
 */
public final class CommandsFactoryImpl implements CommandsFactory {
    private final Logger logger = Logger.getLogger(CommandsFactoryImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    public AbstractCommand build(@NotNull String name,
                                 @NotNull List<String> arguments) {
        logger.info("CommandsFactoryImpl: try to create " + name.toUpperCase() + " command with arguments: " + arguments.toString());
        switch (name) {
            case "echo":
                return new EchoCommand(arguments);
            case "wc":
                return new WcCommand(arguments);
            case "pwd":
                return new PwdCommand(arguments);
            case "cat":
                return new CatCommand(arguments);
            case "exit":
                return new ExitCommand(arguments);
            default:
                return new ExternalCommand(name, arguments);
        }

    }
}
