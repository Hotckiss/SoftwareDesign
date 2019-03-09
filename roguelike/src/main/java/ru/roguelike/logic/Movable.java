package ru.roguelike.logic;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public interface Movable {
    Move move(Screen screen, GameModel model) throws IOException;
}
