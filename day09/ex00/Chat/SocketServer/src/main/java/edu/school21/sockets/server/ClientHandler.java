package edu.school21.sockets.server;


import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;

import java.io.*;
import java.net.Socket;
class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final UsersService usersService;

    public ClientHandler(Socket clientSocket, UsersService usersService) {
        this.clientSocket = clientSocket;
        this.usersService = usersService;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Hello from Server!");

            String command;
            while ((command = in.readLine()) != null) {
                if ("signUp".equalsIgnoreCase(command)) {
                    handleSignUp(in, out);
                } else if ("exit".equalsIgnoreCase(command)) {
                    out.println("Goodbye!");
                    break;
                } else {
                    out.println("Unknown command. Type 'signUp' to register or 'exit' to quit.");
                }
            }
        } catch (IOException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void handleSignUp(BufferedReader in, PrintWriter out) throws IOException {
        while (true) {
            out.println("Enter username:");
            String username = in.readLine();

            out.println("Enter password:");
            String password = in.readLine();

            if (username == null || password == null || username.isBlank() || password.isBlank()) {
                out.println("Error: Username and password cannot be empty.");
                continue;
            }

            User user = new User(username, password);
            if (usersService.signUp(user)) {
                out.println("Successful!");
                break;
            } else {
                out.println("Error: User already exists! Try again or type 'exit' to quit.");
            }
        }
    }

    private void closeSocket() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}