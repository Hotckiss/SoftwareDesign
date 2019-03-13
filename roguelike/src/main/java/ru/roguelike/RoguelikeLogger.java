package ru.roguelike;

import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to log info into special file
 */
public final class RoguelikeLogger {
    /**
     * Logger instance
     */
    public static final RoguelikeLogger INSTANCE = new RoguelikeLogger();
    private final Logger logger = Logger.getLogger("CLI logger");

    private RoguelikeLogger() {
        try {
            logger.setUseParentHandlers(false);
            FileHandler handler = new FileHandler(Paths.get("").toAbsolutePath().toString() + "/RoguelikeLog.log");
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch (Exception ex) {
            System.err.println("Can't specify log file");
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
