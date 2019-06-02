package kirilenko.cli.exceptions;

/**
 * Exception in I/O with files
 */
public class FileIOException extends RuntimeException {
    public FileIOException(String message) {
        super(message);
    }
}
