package kirilenko.cli.utils;

import kirilenko.cli.CLILogger;
import kirilenko.cli.exceptions.NoSuchVariableException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Global environment representation class
 */
public final class Environment {
    private static final Map<String, String> VARIABLES_STORAGE = new HashMap<>();

    /**
     * Adds new variable to environment
     * or modifies existing value
     * @param name variable identifier
     * @param value variable new value
     */
    public void setVariable(@NotNull String name,
                                   @NotNull String value) {
        CLILogger.INSTANCE.log_info("Environment: update variable with name: " + name + " new value is: " + value);
        VARIABLES_STORAGE.put(name, value);
    }

    /**
     * Extracts variable value by name from environment
     * @param name variable identifier
     * @return variable value
     * @throws NoSuchVariableException if variable with input name doesn't exist
     */
    public String getVariable(@NotNull String name) throws NoSuchVariableException {
        if (!VARIABLES_STORAGE.containsKey(name)) {
            CLILogger.INSTANCE.log_error("No such variable in environment: " + name);
            throw new NoSuchVariableException(name);
        }

        CLILogger.INSTANCE.log_info("Environment: extracting variable with name: " + name);
        return VARIABLES_STORAGE.get(name);
    }

    /**
     * Clears environment variables
     */
    public void clear() {
        VARIABLES_STORAGE.clear();
    }
}
