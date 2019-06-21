package ru.roguelike.logic;

import java.io.Serializable;

/**
 * Available game options
 */
public enum MenuOption implements Serializable {
    NEW_GAME, LOAD_GAME, ONLINE_GAME;

    /**
     * Menu option view model
     * @return option view model
     */
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
