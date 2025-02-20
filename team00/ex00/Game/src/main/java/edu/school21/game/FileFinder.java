package edu.school21.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFinder {
    private static final Path currentDir = Paths.get(System.getProperty("user.dir"));

    public static Path findFile(String fileName) {
        Path absolutePath = null;
        try {
            absolutePath = Files.walk(currentDir)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst()
                    .orElse(null);

            if (absolutePath == null) {
                System.err.println("File not found: /resources/" + fileName);
                System.exit(-1);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while searching for file: " + fileName, e);
        }
        return absolutePath;
    }
}
