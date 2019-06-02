package kirilenko.cli.exceptions;

/**
 * Trying to access unknown variable exception
 */
public class NoSuchVariableException extends CliException {
    public NoSuchVariableException(String message) {
        super(message);
    }
}
