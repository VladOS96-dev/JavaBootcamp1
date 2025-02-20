package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class CommandHandler {
    private final Socket socket;
    private final Scanner scanner;
    private final MessageSender messageSender;
    private final BufferedReader reader;
    private boolean authenticated = false;
    private boolean inRoom = false;
    public CommandHandler(Socket socket, Scanner scanner) throws IOException {
        this.socket = socket;
        this.scanner = scanner;
        this.messageSender = new MessageSender(socket);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void handleCommands() throws IOException {
        Thread listenerThread = new Thread(this::listenForServerMessages);
        listenerThread.start();

        while (true) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(input)) {
                    exitApplication();
                    return;
                }
                if (!authenticated) {
                    switch (input) {
                        case "1":
                            handleAuthentication("signIn");
                            break;
                        case "2":
                            handleAuthentication("signUp");
                            break;
                        case "3", "exit":
                            exitApplication();
                            return;
                        default:
                            safePrint("Please sign in or sign up first.");
                            break;
                    }
                    continue;
                }

                if (!inRoom) {
                    switch (input) {
                        case "4":
                            handleRoomCreation();
                            inRoom = true;
                            break;
                        case "5":
                            handleJoinRoom();
                            inRoom = true;
                            break;
                        case "3", "exit":
                            exitApplication();
                            return;
                        default:
                            safePrint("You must join or create a room first.");
                            break;
                    }
                    continue;
                }


                JSONData jsonMessage = new JSONData();
                jsonMessage.addField("message", input);
                messageSender.sendMessage(jsonMessage);
            }
        }
    }

    private void listenForServerMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                JSONData jsonData = JSONUtility.fromJSON(message);

                if (jsonData != null && jsonData.getField("message") != null && jsonData.getField("username") != null) {
                    String username = jsonData.getField("username").toString();
                    String userMessage = jsonData.getField("message").toString();

                    if (userMessage.startsWith("{") && userMessage.endsWith("}")) {
                        JSONData nestedJson = JSONUtility.fromJSON(userMessage);
                        if (nestedJson != null && nestedJson.getField("message") != null) {
                            userMessage = nestedJson.getField("message").toString();
                        }
                    }

                    safePrint(username + ": " + userMessage);
                } else if (jsonData != null && jsonData.getField("message") != null) {
                    String serverMessage = jsonData.getField("message").toString();
                    if (serverMessage.startsWith("You have entered the room:")) {
                        inRoom = true;
                        System.out.println("succes");
                    }
                    if (serverMessage.startsWith("Sign-in successful!")) {
                        authenticated = true;
                    }
                    safePrint(serverMessage);
                } else {
                    safePrint("NON-JSON MESSAGE: " + message);
                }
            }
        } catch (IOException e) {
            safePrint("Disconnected from server.");
        }
    }

    private synchronized void safePrint(String message) {
        synchronized (System.out) {
            System.out.println(message);
        }
    }

    private void handleAuthentication(String command) throws IOException {
        JSONData commandJson = new JSONData();
        commandJson.addField("command", command);
        messageSender.sendMessage(commandJson);

        safePrint("Enter username:");
        String username = scanner.nextLine().trim();
        JSONData dataJson = new JSONData();
        dataJson.addField("username", username);

        safePrint("Enter password:");
        String password = scanner.nextLine().trim();
        dataJson.addField("password", password);
        messageSender.sendMessage(dataJson);

    }

    private void handleRoomCreation() {
        JSONData createRoomCommand = new JSONData();
        createRoomCommand.addField("command", "createRoom");
        messageSender.sendMessage(createRoomCommand);

        safePrint("Enter room name:");
        String roomName = scanner.nextLine().trim();
        JSONData roomData = new JSONData();
        roomData.addField("name_room", roomName);
        messageSender.sendMessage(roomData);
    }

    private void handleJoinRoom() {
        JSONData joinRoomCommand = new JSONData();
        joinRoomCommand.addField("command", "chooseRoom");
        messageSender.sendMessage(joinRoomCommand);

        safePrint("Enter room number:");
        String roomId = scanner.nextLine().trim();
        try {
            Integer.parseInt(roomId);
        } catch (NumberFormatException e) {
            safePrint("Invalid input. Please enter a number.");
            return;
        }
        JSONData roomData = new JSONData();
        roomData.addField("room_id", roomId);
        messageSender.sendMessage(roomData);
        inRoom = true;
    }

    private void exitApplication() {
        JSONData jsonMessage = new JSONData();
        jsonMessage.addField("command", "exit");
        messageSender.sendMessage(jsonMessage);
        safePrint("Goodbye!");
        closeConnection();
    }

    private void closeConnection() {
        try {
            socket.close();
            scanner.close();
            reader.close();
        } catch (IOException e) {
            safePrint("Error closing connection: " + e.getMessage());
        }
    }
}