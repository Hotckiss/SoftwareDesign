package kirilenko.cli.utils;

import kirilenko.cli.exceptions.NoSuchDirectoryException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for emulation changing of process directory.
 */
public class FileSystem {
    private Path currentDirectory = Paths.get(".").toAbsolutePath().normalize();

    /**
     * Returns file resolved against current directory path.
     */
    public File getFile(String name) {
        return currentDirectory.resolve(name).toFile();
    }

    public void setCurrentDirectory(String directory) throws NoSuchDirectoryException {
        Path newDirectory = currentDirectory.resolve(directory);
        if (!Files.exists(newDirectory) || !Files.isDirectory(newDirectory)) {
            throw new NoSuchDirectoryException();
        }
        currentDirectory = newDirectory;
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }
}
