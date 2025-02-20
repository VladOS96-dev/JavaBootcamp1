package ex01.ImagesToChar.src.java.edu.school21.printer.app;

import ex01.ImagesToChar.src.java.edu.school21.printer.logic.Logic;

import java.io.IOException;
import java.io.InputStream;
public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar images-to-chars-printer.jar <white_char> <black_char>");
            return;
        }

        char whiteChar = args[0].charAt(0);
        char blackChar = args[1].charAt(0);

        try  {
            

            Logic logic = new Logic("/resources/image.bmp", whiteChar, blackChar);
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
