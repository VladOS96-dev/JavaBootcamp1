package ex02;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class FileManager {
    private Path currentDirectory;


    public FileManager(String startDirectory) throws IOException {
        currentDirectory = Paths.get(startDirectory).toAbsolutePath();
        if (!Files.isDirectory(currentDirectory)) {
            throw new IOException("The specified path is not a directory: " + startDirectory);
        }
    }
    public Path getCurrentDirectory()
    {
        return  currentDirectory;
    }

    public void ls() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDirectory)) {
            for (Path entry : stream) {
                String type = Files.isDirectory(entry) ? "Folder" : "File";
                long size = Files.isDirectory(entry) ? 0 : Files.size(entry) / 1024;
                System.out.printf("%s %s %d KB%n", type, entry.getFileName(), size);
            }
        }
    }


    public void cd(String folderName) throws IOException {
        Path newPath = currentDirectory.resolve(folderName).normalize();
        if (Files.isDirectory(newPath)) {
            currentDirectory = newPath;
            System.out.println("Current directory: " + currentDirectory);
        } else {
            System.out.println("Folder not found: " + folderName);
        }
    }


    public void mv(String source, String destination) throws IOException {
        Path sourcePath = currentDirectory.resolve(source).normalize();
        Path destinationPath = currentDirectory.resolve(destination).normalize();

        if (Files.exists(sourcePath)) {

            if (Files.isDirectory(destinationPath)) {
                destinationPath = destinationPath.resolve(sourcePath.getFileName());
            }

            try {
                Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.printf("The file has been moved/renamed: %s -> %s%n", source, destination);
            } catch (IOException e) {
                System.out.println("Error moving file: " + e.getMessage());
            }

        } else {
            System.out.println("The file was not found: " + source);
        }
    }


}
