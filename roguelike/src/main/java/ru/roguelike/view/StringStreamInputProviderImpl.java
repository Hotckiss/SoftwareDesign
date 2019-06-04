package ru.roguelike.view;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of user input stub
 */
public class StringStreamInputProviderImpl implements UserInputProvider {
    private final String stream;

    /**
     * Creates new provider from string
     * @param stream char seq
     */
    public StringStreamInputProviderImpl(@NotNull String stream) {
        this.stream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getCharacter() {
        return stream.charAt(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnter() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBackspace() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEscape() {
        return false;
    }
}