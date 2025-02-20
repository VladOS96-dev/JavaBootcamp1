package ex01;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class WordsAnalizer {
    public void readFiles(String filePath1, String filePath2) throws IOException {
        final long MAX_FILE_SIZE = 10 * 1024 * 1024;
        File file1 = new File(filePath1);
        if (file1.length() > MAX_FILE_SIZE) {
            throw new IOException("File " + filePath1 + " exceeds the maximum allowed size of 10MB.");
        }

        File file2 = new File(filePath2);
        if (file2.length() > MAX_FILE_SIZE) {
            throw new IOException("File " + filePath2 + " exceeds the maximum allowed size of 10MB.");
        }
        String outputFilePath = "tmp_files.txt";

        File tempFile = new File(outputFilePath);
        try (
                BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
                BufferedReader reader2 = new BufferedReader(new FileReader(filePath2));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))
        ) {
            String line;

            while ((line = reader1.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                if (!line.trim().isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            while ((line = reader2.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                if (!line.trim().isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }


        sortWordsInFile(outputFilePath, "dictionary.txt");
        if (tempFile.exists()) {
            boolean isDeleted = tempFile.delete();
            if (!isDeleted) {
                System.err.println("Open not tmp file: " + outputFilePath);
            }
        }
    }

    public void sortWordsInFile(String inputFile, String outputFile) throws IOException {
        Files.write(Paths.get(outputFile),
                Files.lines(Paths.get(inputFile))
                        .flatMap(line -> Arrays.stream(line.split("\\s+")))
                        .map(String::toLowerCase)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList()));
    }

    public double similarityComparison(String filePath1, String filePath2) throws IOException {
        Vector<Integer> vectorWords1 = createVectorWords(filePath1);
        Vector<Integer> vectorWords2 = createVectorWords(filePath2);

        double numeratorAB = multiplyVector(vectorWords1, vectorWords2);
        double denominator = denominator(vectorWords1, vectorWords2);

        return numeratorAB / denominator;
    }

    private Vector<Integer> createVectorWords(String filePath) throws IOException {
        Vector<Integer> vector = new Vector<>();


        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    int count = countCoincidenceWordInFile(filePath, word);
                    vector.add(count);
                }
            }
        }

        return vector;
    }

    private int countCoincidenceWordInFile(String filePath, String word) throws IOException {
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                String[] words = line.split("\\s+");
                for (String _word : words) {
                    if (word.equals(_word)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private double multiplyVector(Vector<Integer> A, Vector<Integer> B) {
        double result = 0;
        for (int i = 0; i < A.size(); i++) {
            result += A.get(i) * B.get(i);
        }
        return result;
    }

    private double denominator(Vector<Integer> A, Vector<Integer> B) {
        double resultA = 0, resultB = 0;

        for (int i = 0; i < A.size(); i++) {
            resultA += A.get(i) * A.get(i);
        }
        resultA = Math.sqrt(resultA);

        for (int i = 0; i < B.size(); i++) {
            resultB += B.get(i) * B.get(i);
        }
        resultB = Math.sqrt(resultB);

        return resultA * resultB;
    }
}

