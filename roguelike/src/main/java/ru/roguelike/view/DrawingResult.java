package ru.roguelike.view;

public class DrawingResult {
    private boolean loadMapFromFile;
    private String fileNameForMap;

    public DrawingResult(boolean loadMapFromFile, String fileNameForMap) {
        this.loadMapFromFile = loadMapFromFile;
        this.fileNameForMap = fileNameForMap;
    }

    public boolean isLoadMapFromFile() {
        return loadMapFromFile;
    }

    public String getFileNameForMap() {
        return fileNameForMap;
    }
}
