package ex02;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        if (args.length < 1 || !args[0].startsWith("--current-folder=")) {
            System.out.println("Specify the initial directory: --current-folder=PATH_TO_FOLDER");
            return;
        }

        String startFolder = args[0].substring("--current-folder=".length());
        try {
            FileManager fileManager = new FileManager(startFolder);
            System.out.println(fileManager.getCurrentDirectory());


            while (true) {
                System.out.print("-> ");
                String command = scanner.nextLine();
                Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
                List<String> parts = new ArrayList<>();
                while (matcher.find()) {
                    parts.add(matcher.group(1).replace("\"", "")); // Убираем кавычки
                }

                if (parts.isEmpty()) {
                    continue;
                }
                switch (parts.get(0)) {
                    case "ls":
                        fileManager.ls();
                        break;
                    case "cd":
                        if (parts.size() < 2) {
                            System.out.println("Specify the folder to go to: cd FOLDER");
                        } else {
                            fileManager.cd(parts.get(1));
                        }
                        break;
                    case "mv":
                        if (parts.size() < 3) {
                            System.out.println("Specify the source and destination path: mv SOURCE DESTINATION");
                        } else {
                            fileManager.mv(parts.get(1), parts.get(2));
                        }
                        break;
                    case "exit":
                        System.out.println("Exit the program.");
                        return;
                    default:
                        System.out.println("Unknown command. Available commands: ls, cd, mv, exit.");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
