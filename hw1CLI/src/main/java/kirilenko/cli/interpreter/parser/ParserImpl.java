package kirilenko.cli.interpreter.parser;

import com.google.common.collect.Lists;
import kirilenko.cli.CLILogger;
import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.factory.CommandsFactory;
import kirilenko.cli.commands.factory.CommandsFactoryImpl;
import kirilenko.cli.commands.implementation.AssignmentCommand;
import kirilenko.cli.commands.implementation.ExitCommand;
import kirilenko.cli.commands.implementation.PipelineCommand;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.exceptions.ParseException;
import kirilenko.cli.interpreter.tokenizer.Tokenizer;
import kirilenko.cli.interpreter.tokenizer.TokenizerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class that implements parsing for CLI
 */
public class ParserImpl implements Parser {
    private Tokenizer tokenizer = new TokenizerImpl();
    private CommandsFactory commandsFactory = new CommandsFactoryImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCommand parse(@NotNull String statement) throws CliException {
        Queue<Character> queue = new LinkedList<>();

        for (Character c : statement.toCharArray()) {
            queue.add(c);
        }

        List<AbstractCommand> pipedCommands = new ArrayList<>();
        String name = null;
        List<String> arguments = new ArrayList<>();

        while (!queue.isEmpty()) {
            if (queue.element().equals(' ') || queue.element().equals('\n') || queue.element().equals('\r')) {
                queue.remove();
                continue;
            }

            if (name == null) {
                name = tokenizer.parseToken(queue, TokenizerImpl.isNameCharacter, false);
                if (name.isEmpty()) {
                    CLILogger.INSTANCE.log_error("Token has empty name");
                    throw new ParseException("Token has empty name");
                }
                continue;
            }

            if (queue.element().equals('=')) {
                if (!arguments.isEmpty()) {
                    CLILogger.INSTANCE.log_error("Assignment mustn't have any arguments");
                    throw new ParseException("Assignment mustn't have any arguments");
                }

                queue.remove();
                StringBuilder right = new StringBuilder();
                queue.forEach(right::append);
                pipedCommands.add(new AssignmentCommand(Collections.singletonList(right.toString()), name));
                CLILogger.INSTANCE.log_info("ParserImpl: assignment completed");
                return buildTree(pipedCommands);
            }

            if (queue.element().equals('"') || queue.element().equals('\'')) {
                Character until = queue.element();
                CLILogger.INSTANCE.log_info("ParserImpl: parse token in quotes start");
                arguments.add(tokenizer.parseToken(queue, character -> !until.equals(character), true));
                CLILogger.INSTANCE.log_info("ParserImpl: parse token in quotes completed");
                continue;
            }

            if (queue.element().equals('|')) {
                CLILogger.INSTANCE.log_info("ParserImpl: add piped command");
                pipedCommands.add(commandsFactory.build(name, arguments));
                arguments = new ArrayList<>();
                name = null;
                queue.poll();
                continue;
            }

            arguments.add(tokenizer.parseToken(queue, TokenizerImpl.isNotWordBreak, false));
        }

        if (name == null) {
            throw new ParseException("Token is empty");
        }

        pipedCommands.add(commandsFactory.build(name, arguments));

        return buildTree(pipedCommands);
    }

    /**
     * Builds command execution tree
     * @param commands commands for building tree
     * @return root command for execution
     */
    private AbstractCommand buildTree(@NotNull List<AbstractCommand> commands) {
        return Lists.reverse(commands).stream()
                .reduce((left, right) -> new PipelineCommand(Collections.emptyList(), right, left))
                .orElse(new ExitCommand(Collections.emptyList()));
    }
}
