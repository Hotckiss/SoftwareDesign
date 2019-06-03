package ru.roguelike.logic;

public enum MenuOption {
    NEW_GAME, LOAD_GAME, ONLINE_GAME;

    public String getAlias() {
        switch (this) {
            case NEW_GAME:
                return "Start new game";
            case LOAD_GAME:
                return "Load game";
            case ONLINE_GAME:
                return "Start online game";
        }

        return "Start new game";
    }
}
