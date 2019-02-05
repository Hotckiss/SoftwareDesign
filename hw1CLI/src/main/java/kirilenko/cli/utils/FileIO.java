package kirilenko.cli.utils;

import kirilenko.cli.exceptions.FileIOException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Class for reading lines from file and writing lines to output
 */
public final class FileIO {
    private static final Logger logger = Logger.getLogger(FileIO.class.getName());

    /**
     * Reads all lines of specified file
     * @param input input file stream
     * @return file lines list
     */
    public static List<String> readLines(@NotNull InputStream input) {
        logger.info("FileIO: try to read lines from file");
        return new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.toList());
    }

    /**
     * Writes all input lines to output
     * @param stringStream input lines to write
     * @param output output stream to  write
     * @throws FileIOException if error during writing occurred
     */
    public static void writeLines(@NotNull List<String> stringStream,
                                  @NotNull OutputStream output) throws FileIOException {
        Writer writer = new OutputStreamWriter(output);

        stringStream.forEach(
                str -> {
                    try {
                        logger.info("FileIO: writing new line...");
                        writer.write(str + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        logger.warning("FileIO: error during writing line: " + str);
                        throw new FileIOException(e.getMessage());
                    }
                });
    }
}
