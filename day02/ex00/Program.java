package ex00;

import java.io.*;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        FileSignatures fileSignatures = new FileSignatures();
        Scanner scanner = new Scanner(System.in);

        try {

            fileSignatures.loadSignatures("signatures.txt");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
                while (true) {
                    System.out.print("-> ");
                    String filepath = scanner.nextLine();


                    if (filepath.equals("42")) break;

                    try {
                        String result = fileSignatures.identifyFile(filepath);


                        if (!result.equals("UNDEFINED")) {
                            writer.write(result);
                            System.out.println(result);
                            writer.newLine();
                        }
                        else {
                            System.out.println("UNDEFINED");
                        }
                        System.out.println("PROCESSED");
                    } catch (IOException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
