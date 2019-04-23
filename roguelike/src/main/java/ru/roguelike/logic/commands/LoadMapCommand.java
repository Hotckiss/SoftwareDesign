package ru.roguelike.logic.commands;

import ru.roguelike.view.ConsoleView;

public class LoadMapCommand implements Command {
    private ConsoleView view;

    public LoadMapCommand(ConsoleView view) {
        this.view = view;
    }

    @Override
    public void execute() {

    }
}
