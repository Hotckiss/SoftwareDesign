package ru.roguelike.view;

/**
 * Indicates game to load map from file or generate
 */
public class DrawingResult {
    private boolean loadMapFromFile;
    private String fileNameForMap;

    /**
     * Constructs new drawing result
     * @param loadMapFromFile indicates to load map from file
     * @param fileNameForMap file with map
     */
    public DrawingResult(boolean loadMapFromFile, String fileNameForMap) {
        this.loadMapFromFile = loadMapFromFile;
        this.fileNameForMap = fileNameForMap;
    }

    /**
     * Getter for loading map from file flag
     * @return true if should load map from file false otherwise
     */
    public boolean isLoadMapFromFile() {
        return loadMapFromFile;
    }

    /**
     * Getter for loading map from file filename
     * @return filename of map
     */
    public String getFileNameForMap() {
        return fileNameForMap;
    }
}
