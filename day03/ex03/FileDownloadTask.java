package ex03;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class  FileDownloadTask implements Runnable {
    private final BlockingQueue<String[]> taskQueue;

    public FileDownloadTask(BlockingQueue<String[]> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String[] task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task == null) {
                    break;
                }

                String fileId = task[0];
                String fileUrl = task[1];
                System.out.println(Thread.currentThread().getName() + " start download file number " + fileId);

                downloadFile(fileId, fileUrl);

                System.out.println(Thread.currentThread().getName() + " finish download file number " + fileId);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void downloadFile(String fileId, String fileUrl) {
        try (InputStream in = new URL(fileUrl).openStream()) {
            Path targetPath = Paths.get(fileId + "_" + Paths.get(new URL(fileUrl).getPath()).getFileName());
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error downloading file " + fileId + ": " + e.getMessage());
        }
    }
}