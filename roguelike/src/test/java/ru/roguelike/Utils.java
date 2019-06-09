package ru.roguelike;

import java.io.*;

/**
 * Utilities for testing class
 */
public class Utils {
    /**
     * Method to read file as one string
     * @param fileName path to file
     * @return string representation of file
     * @throws IOException if any I/O error occurred
     */
    public static String readFileAsString(String fileName) throws IOException {
        InputStream is = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null){
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();

        is.close();
        buf.close();

        return fileAsString;
    }
}
