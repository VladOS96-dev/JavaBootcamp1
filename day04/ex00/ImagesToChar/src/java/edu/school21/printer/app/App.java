package ex00.ImagesToChar.src.java.edu.school21.printer.app;

import ex00.ImagesToChar.src.java.edu.school21.printer.logic.Logic;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java App <image_path> <white_char> <black_char>");
            return;
        }

        String imagePath = args[0];
        char whiteChar = args[1].charAt(0);
        char blackChar = args[2].charAt(0);

        try {
            Logic logic = new Logic(imagePath, whiteChar, blackChar);
            char[][] imageArray = logic.convertImageToCharArray();
            printImage(imageArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printImage(char[][] imageArray) {
        for (char[] row : imageArray) {
            for (char pixel : row) {
                System.out.print(pixel);
            }
            System.out.println();
        }
    }
}
