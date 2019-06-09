package ru.roguelike.logic.commands;

import ru.roguelike.view.ConsoleView;

/**
 * Shows help screen.
 */
public class ShowHelpScreenCommand implements Command {
    private final ConsoleView view;

    /**
     * Constructs new command to show help screen
     * @param view view to output help screen
     */
    public ShowHelpScreenCommand(ConsoleView view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        view.clear();
        view.drawHelpScreen();
    }
}
