package edu.school21.sockets.server;

import edu.school21.sockets.services.RoomsService;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final int port;
    private final UsersService usersService;
    private final RoomsService roomsService;
    private final Set<ClientHandler> clients = new HashSet<>();
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();
    public Server(int port, UsersService usersService, RoomsService roomsService) {
        this.port = port;
        this.usersService = usersService;
        this.roomsService = roomsService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, usersService, roomsService, this);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }


    public synchronized boolean isUserLoggedIn(String username) {
        return activeUsers.contains(username);
    }

    public synchronized void addActiveUser(String username) {
        activeUsers.add(username);
    }

    public synchronized void removeActiveUser(String username) {
        activeUsers.remove(username);
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        if (client.getCurrentUser() != null) {
            removeActiveUser(client.getCurrentUser().getUsername()); // Удаление из активных
        }
    }
}