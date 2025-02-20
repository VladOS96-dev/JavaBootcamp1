package edu.school21.sockets.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private final String host;
    private final int port;
    private Socket socket;
    private Scanner reader;
    private PrintWriter writer;
    private Scanner scanner;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            reader = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            while (reader.hasNextLine()) {
                String serverMessage = reader.nextLine();
                System.out.println(serverMessage);

                if ("Successful!".equalsIgnoreCase(serverMessage) || "Goodbye!".equalsIgnoreCase(serverMessage)) {
                    break;
                }

                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    writer.println(input);

                    if ("exit".equalsIgnoreCase(input)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void stop() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (scanner != null) scanner.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}