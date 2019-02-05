package kirilenko.cli;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.exceptions.FileIOException;
import kirilenko.cli.interpreter.parser.Parser;
import kirilenko.cli.interpreter.parser.ParserImpl;
import kirilenko.cli.interpreter.substitutor.Substitutor;
import kirilenko.cli.interpreter.substitutor.SubstitutorImpl;
import kirilenko.cli.utils.FileIO;

import java.io.OutputStream;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * CLI main class
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String line = scanner.nextLine();
                if (execute(line, System.out)) {
                    break;
                }
            } catch (CliException e) {
                logger.warning("CLI was aborted with error: " + e.getMessage());
                System.err.println(e.getMessage());
            } catch (FileIOException e) {
                logger.warning("CLI was aborted with I/O error: " + e.getMessage());
                e.getCause().printStackTrace();
            } catch (NoSuchElementException e) {
                logger.warning("CLI was aborted with variable name: " + e.getMessage());
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                logger.warning("CLI was aborted with unknown error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static boolean execute(String input, OutputStream output) throws CliException {
        Substitutor substitutor = new SubstitutorImpl();
        Parser parser = new ParserImpl();
        AbstractCommand start = parser.parse(substitutor.substitute(input));
        CommandResult result = start.execute(Collections.emptyList());
        if (result.isExit()) {
            return true;
        }
        if (result.isAborted()) {
            return false;
        }
        FileIO.writeLines(result.getOutput(), output);
        return false;
    }
}
