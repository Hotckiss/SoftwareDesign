package ru.roguelike.view;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of user input
 */
public class UserInputProviderImpl implements UserInputProvider {
    private final KeyStroke keyStroke;

    /**
     * Creates new provider from key input of lanterna
     * @param keyStroke lanterna input reader
     */
    public UserInputProviderImpl(@NotNull KeyStroke keyStroke) {
        this.keyStroke = keyStroke;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getCharacter() {
        return keyStroke.getCharacter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnter() {
        return keyStroke.getKeyType() == KeyType.Enter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBackspace() {
        return keyStroke.getKeyType() == KeyType.Backspace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEscape() {
        return keyStroke.getKeyType() == KeyType.Escape;
    }
}
