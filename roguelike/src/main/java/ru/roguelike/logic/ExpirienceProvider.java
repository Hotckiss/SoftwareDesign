package ru.roguelike.logic;

/**
 * Interface to manage player and mobs expirience
 */
public interface ExpirienceProvider {
    /**
     * Method to get mob or player experience
     * @return current experience
     */
    int getExperience();

    /**
     * Method that generates participant level using current experience
     * @return current level
     */
    int getLevel();
}
