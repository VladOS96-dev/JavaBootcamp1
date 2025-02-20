package edu.school21.sockets.server;


import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final UsersService usersService;
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    private User currentUser;

    public ClientHandler(Socket clientSocket, UsersService usersService, Server server) {
        this.clientSocket = clientSocket;
        this.usersService = usersService;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("Hello from Server!");

            while (true) {
                out.println("Type 'signUp' to register, 'signIn' to login, or 'exit' to quit:");
                String command = in.readLine();

                if ("signUp".equalsIgnoreCase(command)) {
                    handleSignUp();
                } else if ("signIn".equalsIgnoreCase(command)) {
                    if (handleSignIn()) {
                        handleMessaging();
                    }
                } else if ("exit".equalsIgnoreCase(command)) {
                    out.println("Goodbye!");
                    break;
                } else {
                    out.println("Unknown command.");
                }
            }
        } catch (java.net.SocketException e) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleSignUp() throws IOException {
        out.println("Enter username:");
        String username = in.readLine();

        out.println("Enter password:");
        String password = in.readLine();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            out.println("Error: Username and password cannot be empty.");
            return;
        }

        User user = new User(username, password);
        if (usersService.signUp(user)) {
            out.println("Registration successful! You can now sign in.");
        } else {
            out.println("Error: User already exists.");
        }
    }

    private boolean handleSignIn() throws IOException {
        out.println("Enter username:");
        String username = in.readLine();

        out.println("Enter password:");
        String password = in.readLine();

        User user = new User(username, password);
        if (usersService.signIn(user)) {
            this.currentUser = user;
            out.println("Start messaging");
            return true;
        } else {
            out.println("Error: Invalid credentials.");
            return false;
        }
    }

    private void handleMessaging() throws IOException {
        String message;
        while ((message = in.readLine()) != null) {
            if ("exit".equalsIgnoreCase(message)) {
                out.println("You have left the chat.");
                break;
            }
            String formattedMessage = currentUser.getUsername() + ": " + message;
            usersService.createMessage(formattedMessage);
            server.broadcastMessage(formattedMessage, this);
        }
        closeConnection();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnection() {
        try {
            server.removeClient(this);
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}