package ru.roguelike.logic.commands;

import ru.roguelike.view.ConsoleView;

public class ShowHelpScreenCommand implements Command {
    private final ConsoleView view;

    public ShowHelpScreenCommand(ConsoleView view) {
        this.view = view;
    }

    @Override
    public void execute() {
        view.clear();
        view.drawHelpScreen();
    }
}
