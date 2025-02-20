package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;

import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0 || !args[0].startsWith("--server-port=")) {
            System.out.println("Error: Server port is not specified. Use --server-port=PORT_NUMBER");
            return;
        }
        int port = Integer.parseInt(args[0].split("=")[1]);
        Client client = new Client("localhost", port);
        client.start();
    }
}