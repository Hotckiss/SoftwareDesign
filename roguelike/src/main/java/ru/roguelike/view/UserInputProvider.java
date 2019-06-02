package ru.roguelike.view;

/**
 * Interface to provide user input through external libraries
 */
public interface UserInputProvider {
    /**
     * Getter for character from the input
     * @return character from the input
     */
    Character getCharacter();

    /**
     * Check for enter input
     * @return true if enter was pressed false otherwise
     */
    boolean isEnter();

    /**
     * Check for backspace input
     * @return true if backspace was pressed false otherwise
     */
    boolean isBackspace();

    /**
     * Check for escape input
     * @return true if escape was pressed false otherwise
     */
    boolean isEscape();
}
