package kirilenko.cli.interpreter.parser;

import com.google.common.collect.Lists;
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
import java.util.logging.Logger;

/**
 * Class that implements parsing for CLI
 */
public class ParserImpl implements Parser {
    private Tokenizer tokenizer = new TokenizerImpl();
    private CommandsFactory commandsFactory = new CommandsFactoryImpl();
    private Logger logger = Logger.getLogger(ParserImpl.class.getName());

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
            if (queue.element().equals(' ') || queue.element().equals('\n')) {
                queue.remove();
                continue;
            }

            if (name == null) {
                name = tokenizer.parseToken(queue, TokenizerImpl.NAME_CHARACTERS::contains, false);
                if (name.isEmpty()) {
                    logger.warning("Token has empty name");
                    throw new ParseException("Token has empty name");
                }
                continue;
            }

            if (queue.element().equals('=')) {
                if (!arguments.isEmpty()) {
                    logger.warning("Assignment mustn't have any arguments");
                    throw new ParseException("Assignment mustn't have any arguments");
                }

                queue.remove();
                StringBuilder right = new StringBuilder();
                queue.forEach(right::append);
                pipedCommands.add(new AssignmentCommand(Collections.singletonList(right.toString()), name));
                logger.info("ParserImpl: assignment completed");
                return buildTree(pipedCommands);
            }

            if (queue.element().equals('"') || queue.element().equals('\'')) {
                Character until = queue.element();
                logger.info("ParserImpl: parse token in quotes start");
                arguments.add(tokenizer.parseToken(queue, character -> !until.equals(character), true));
                logger.info("ParserImpl: parse token in quotes completed");
                continue;
            }

            if (queue.element().equals('|')) {
                logger.info("ParserImpl: add piped command");
                pipedCommands.add(commandsFactory.build(name, arguments));
                arguments = new ArrayList<>();
                name = null;
                queue.poll();
                continue;
            }

            arguments.add(tokenizer.parseToken(queue, character -> !TokenizerImpl.WORD_BREAK_CHARACTERS.contains(character), false));
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
