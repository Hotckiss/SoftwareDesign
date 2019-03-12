package ru.roguelike.logic;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

public interface Movable {
    Move move(KeyStroke keyStroke, GameModel model) throws IOException;
}
