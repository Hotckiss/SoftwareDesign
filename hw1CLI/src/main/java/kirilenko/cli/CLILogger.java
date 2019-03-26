package kirilenko.cli;

import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to log info onto special file
 */
public final class CLILogger {
    /**
     * Logger instance
     */
    public static final CLILogger INSTANCE = new CLILogger();

    private final Logger logger = Logger.getLogger("CLI logger");

    private CLILogger() {
        try {
            logger.setUseParentHandlers(false);
            FileHandler handler = new FileHandler(Paths.get("").toAbsolutePath().toString() + "/CLILog.log");
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch (Exception ex) {
            System.err.println("Can not specify log file");
        }
    }

    /**
     * Log not error information
     * @param info message
     */
    public void log_info(String info) {
        logger.info(info);
    }

    /**
     * Log error information
     * @param error message
     */
    public void log_error(String error) {
        logger.warning(error);
    }
}
