package edu.school21.sockets.server;


import edu.school21.sockets.models.User;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {
    private final int port;
    private final UsersService usersService;

    public Server(int port, UsersService usersService) {
        this.port = port;
        this.usersService = usersService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, usersService).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
