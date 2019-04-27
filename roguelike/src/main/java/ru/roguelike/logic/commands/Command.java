package ru.roguelike.logic.commands;

import java.io.IOException;

/**
 * Represents a response to user's action.
 */
public interface Command {
    /**
     * Executes the command
     *
     * @throws IOException if it occurs
     */
    void execute() throws IOException;
}
