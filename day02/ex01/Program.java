package ex01;

import java.io.IOException;

public class Program {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Program <file1> <file2>");
            return;
        }

        try {
            WordsAnalizer wordsAnalizer = new WordsAnalizer();
            wordsAnalizer.readFiles(args[0], args[1]);

            double result = wordsAnalizer.similarityComparison(args[0], args[1]);
            System.out.printf("Similarity = %.3f%n", result);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
