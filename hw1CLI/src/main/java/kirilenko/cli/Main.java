package kirilenko.cli;

import kirilenko.cli.commands.AbstractCommand;
import kirilenko.cli.commands.CommandResult;
import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.exceptions.FileIOException;
import kirilenko.cli.interpreter.parser.Parser;
import kirilenko.cli.interpreter.parser.ParserImpl;
import kirilenko.cli.interpreter.substitutor.Substitutor;
import kirilenko.cli.interpreter.substitutor.SubstitutorImpl;
import kirilenko.cli.utils.Environment;
import kirilenko.cli.utils.FileIO;

import java.io.OutputStream;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * CLI main class
 */
public class Main {
    private static final Substitutor substitutor = new SubstitutorImpl();
    private static final Parser parser = new ParserImpl();
    private static final Environment CLIEnvironment = new Environment();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String line = scanner.nextLine();
                if (execute(line, System.out)) {
                    break;
                }
            } catch (FileIOException e) {
                CLILogger.INSTANCE.log_error("CLI command was aborted with I/O error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                CLILogger.INSTANCE.log_error("CLI command was aborted with variable name: " + e.getMessage());
                scanner = new Scanner(System.in);
            } catch (Exception e) {
                CLILogger.INSTANCE.log_error("CLI command was aborted with error: " + e.getMessage());
            }
        }
    }

    private static boolean execute(String input, OutputStream output) throws CliException {
        AbstractCommand start = parser.parse(substitutor.substitute(input, CLIEnvironment));
        CommandResult result = start.execute(Collections.emptyList(), CLIEnvironment);
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
