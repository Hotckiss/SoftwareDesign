package kirilenko.cli.interpreter.substitutor;

import kirilenko.cli.exceptions.NoSuchVariableException;
import kirilenko.cli.exceptions.ParseException;
import kirilenko.cli.interpreter.tokenizer.Tokenizer;
import kirilenko.cli.interpreter.tokenizer.TokenizerImpl;
import kirilenko.cli.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * Class that implements variables substitution for CLI
 */
public class SubstitutorImpl implements Substitutor {
    private Tokenizer tokenizer = new TokenizerImpl();
    private final Logger logger = Logger.getLogger(SubstitutorImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public String substitute(@NotNull String statement) throws ParseException, NoSuchVariableException {
        Queue<Character> queue = new LinkedList<>();

        for (Character c : statement.toCharArray()) {
            queue.add(c);
        }

        boolean inDoubleQuotes = false;
        StringBuilder substituted = new StringBuilder();

        while (!queue.isEmpty()) {
            if (queue.element().equals('\\') && queue.size() > 1) {
                logger.info("SubstitutorImpl: handle special character as symbol");
                queue.remove();
                substituted.append(queue.poll());
            }

            if (queue.element().equals('$')) {
                queue.remove();
                logger.info("SubstitutorImpl: substitution start");
                substituted.append(Environment.getVariable(tokenizer.parseToken(queue, TokenizerImpl.NAME_CHARACTERS::contains, false)));
                logger.info("SubstitutorImpl: substitution completed");
                continue;
            }

            if (queue.element().equals('\'') && !inDoubleQuotes) {
                Character until = '\'';
                substituted.append('\'');
                logger.info("SubstitutorImpl: parse token in quotes without substitution start");
                substituted.append(tokenizer.parseToken(queue, character -> !until.equals(character), true));
                logger.info("SubstitutorImpl: parse token in quotes without substitution completed");
                substituted.append('\'');
                continue;
            }

            if (queue.element().equals('"')) {
                inDoubleQuotes = !inDoubleQuotes;
                logger.info("SubstitutorImpl: now statement " + (inDoubleQuotes ? "in" : "out of") + " double quotes");
            }

            substituted.append(queue.poll());
        }

        return substituted.toString();
    }
}
