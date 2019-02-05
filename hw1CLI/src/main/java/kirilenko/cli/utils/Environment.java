package kirilenko.cli.utils;

import kirilenko.cli.exceptions.NoSuchVariableException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Global environment representation class
 */
public final class Environment {
    private final static Map<String, String> VARIABLES_STORAGE = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Environment.class.getName());

    /**
     * Adds new variable to environment
     * or modifies existing value
     * @param name variable identifier
     * @param value variable new value
     */
    public static void setVariable(@NotNull String name,
                                   @NotNull String value) {
        logger.info("Environment: update variable with name: " + name + " new value is: " + value);
        VARIABLES_STORAGE.put(name, value);
    }

    /**
     * Extracts variable value by name from environment
     * @param name variable identifier
     * @return variable value
     * @throws NoSuchVariableException if variable with input name doesn't exist
     */
    public static String getVariable(@NotNull String name) throws NoSuchVariableException {
        if (!VARIABLES_STORAGE.containsKey(name)) {
            logger.warning("No such variable in environment: " + name);
            throw new NoSuchVariableException(name);
        }

        logger.info("Environment: extracting variable with name: " + name);
        return VARIABLES_STORAGE.get(name);
    }

    /**
     * Clears environment variables
     */
    public static void clear() {
        VARIABLES_STORAGE.clear();
    }
}
