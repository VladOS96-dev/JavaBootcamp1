package ex03;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
public class Program {
    private static final String FILE_LIST = "files_urls.txt";

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--threadsCount=")) {
            System.out.println("Usage: java Program --threadsCount=<number>");
            return;
        }

        int threadsCount;
        try {
            threadsCount = Integer.parseInt(args[0].substring("--threadsCount=".length()));
            if (threadsCount <= 0) {
                throw new IllegalArgumentException("Threads count must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid threads count.");
            return;
        }


        List<String[]> fileList = readFileList(FILE_LIST);
        if (fileList.isEmpty()) {
            System.out.println("No files to download.");
            return;
        }


        BlockingQueue<String[]> taskQueue = new LinkedBlockingQueue<>(fileList);


        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            executor.submit(new FileDownloadTask(taskQueue));
        }

        executor.shutdown();
    }

    private static List<String[]> readFileList(String filename) {
        List<String[]> fileList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                if (parts.length == 2) {
                    fileList.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file list: " + e.getMessage());
        }
        return fileList;
    }
}