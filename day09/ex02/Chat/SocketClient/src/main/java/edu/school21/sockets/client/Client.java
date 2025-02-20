package edu.school21.sockets.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;
    private Socket socket;
    private Scanner scanner;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            scanner = new Scanner(System.in);



            CommandHandler commandHandler = new CommandHandler(socket, scanner);
            commandHandler.handleCommands();

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void stop() {
        try {
            if (scanner != null) scanner.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}
