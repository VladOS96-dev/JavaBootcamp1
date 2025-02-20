package edu.school21.sockets.server;

import edu.school21.sockets.models.Chatroom;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.services.RoomsService;
import edu.school21.sockets.services.UsersService;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final UsersService usersService;
    private final RoomsService roomsService;
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    @Getter
    private User currentUser;
    private Optional<Chatroom> currentRoom;
    private boolean authenticated = false;
    private boolean inRoom = false;

    public ClientHandler(Socket clientSocket, UsersService usersService, RoomsService roomsService, Server server) {
        this.clientSocket = clientSocket;
        this.usersService = usersService;
        this.roomsService = roomsService;
        this.server = server;
    }

    public void sendMessage(String message) {
        JSONData jsonData;
        if (message.startsWith("{")) {
            jsonData = JSONUtility.fromJSON(message);
        } else {
            jsonData = new JSONData();
            jsonData.addField("message", message);
        }
        jsonData.setTime(java.time.LocalDateTime.now());
        out.println(JSONUtility.toJSON(jsonData).toJSONString());
    }


    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            sendMessage("Hello from Server!");
            logger.info("New client connected: " + clientSocket.getRemoteSocketAddress());

            while (!authenticated) {
                sendMessage("Welcome!\n1. Sign In\n2. Sign Up\n3. Exit");
                String input = in.readLine();
                if (input == null) break;

                JSONData request = JSONUtility.fromJSON(input);
                if (request == null) {
                    sendMessage("Invalid request.");
                    continue;
                }

                String command = (String) request.getField("command");
                if (command == null) {
                    sendMessage("Invalid request, no command found.");
                    continue;
                }
                if ("exit".equalsIgnoreCase(command)) {
                    sendMessage("Goodbye!");
                    return;
                }
                switch (command.toLowerCase()) {
                    case "signup":
                        handleSignUp();
                        break;
                    case "signin":
                        if (handleSignIn()) {
                            authenticated = true;
                            handleChatRoom();
                        }
                        break;
                    case "exit":
                        sendMessage("Goodbye!");
                        return;
                    default:
                        sendMessage("Unknown command.");
                }
            }
        } catch (IOException e) {
            logger.error("IOException in ClientHandler: " + e.getMessage(), e);
        } finally {
            if (currentUser != null) {
                server.removeActiveUser(currentUser.getUsername());
            }
            closeConnection();
        }
    }

    private void handleSignUp() throws IOException {
        JSONData jsonData=JSONUtility.fromJSON(in.readLine().trim());
        if(jsonData==null)
        {
            return;
        }

        User user = new User(jsonData.getField("username").toString(), jsonData.getField("password").toString());
        if (usersService.signUp(user)) {
            sendMessage("Registration successful! You can now sign in.");
            logger.info("New user signed up: " + user.getUsername());
        } else {
            sendMessage("Error: User already exists.");
            logger.warn("Attempt to sign up with existing username: " + user.getUsername());
        }
    }

    private boolean handleSignIn() throws IOException {
        JSONData input = JSONUtility.fromJSON(in.readLine().trim());
        if (input == null) return false;

        String usernameInput = input.getField("username").toString();
        String passwordInput = input.getField("password").toString();

        if (server.isUserLoggedIn(usernameInput)) {
            sendMessage("Error: User already signed in.");
            logger.warn("Attempt to sign in from multiple clients: " + usernameInput);
            return false;
        }

        User user = usersService.getUserByUsername(usernameInput);
        if (user == null || !usersService.signIn(usernameInput, passwordInput)) {
            sendMessage("Error: Invalid credentials.");
            return false;
        }

        this.currentUser = user;
        server.addActiveUser(usernameInput);
        sendMessage("Sign-in successful!");
        return true;
    }

    private void handleChatRoom() throws IOException {
        sendMessage("Choose an option:\n4. Create room\n5. Choose room\n3. Exit");
        JSONData input=JSONUtility.fromJSON(in.readLine().trim());
        if (input == null) return;
        logger.info("Received raw input from client: " + input);
        switch (input.getField("command").toString()) {
            case "createRoom":
                createRoom();
                break;
            case "chooseRoom":
                chooseRoom();
                break;
            case "exit":
                sendMessage("Goodbye!");
                break;
            default:
                sendMessage("Invalid command.");
                logger.warn("Invalid room selection command from user: " + currentUser.getUsername());
        }
    }
    private void createRoom() throws IOException {
        JSONData roomData = JSONUtility.fromJSON(in.readLine());
        logger.info("Receive create room user " + currentUser.getUsername());
        if (roomData == null) return;

        String roomName = (String) roomData.getField("name_room");

        if (roomsService.createRoom(roomName, currentUser)) {
            sendMessage("Room created successfully!");
            logger.info("Room created by user: " + currentUser.getUsername() + ", Room name: " + roomName);


            this.currentRoom = roomsService.getRoomByName(roomName);
            roomsService.joinRoom(currentRoom.get(), currentUser);
            sendMessage("You have entered the room: " + currentRoom.get().getNameRoom());

            inRoom = true;
            handleMessaging();
        } else {
            sendMessage("Error: Room already exists.");
            logger.warn("Attempt to create room with existing name: " + roomName);
        }
    }


    private void chooseRoom() throws IOException {
        List<Chatroom> rooms = roomsService.getAllRooms();
        if (rooms.isEmpty()) {
            sendMessage("No rooms available.");
            return;
        }

        StringBuilder roomList = new StringBuilder("Rooms:\n");
        for (int i = 0; i < rooms.size(); i++) {
            roomList.append(i + 1).append(". ").append(rooms.get(i).getNameRoom()).append("\n");
        }
        roomList.append((rooms.size() + 1)).append(". Exit");
        sendMessage(roomList.toString());

        JSONData roomChoiceData = JSONUtility.fromJSON(in.readLine());
        if (roomChoiceData == null) return;

        String choiceStr = (String) roomChoiceData.getField("room_id");
        int choice;
        try {
            choice = Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            sendMessage("Invalid input. Please enter a number.");
            return;
        }

        if (choice == rooms.size() + 1) {
            sendMessage("Exiting room selection.");
            return;
        }

        if (choice < 1 || choice > rooms.size()) {
            sendMessage("Invalid choice. Please try again.");
            return;
        }

        this.currentRoom = Optional.ofNullable(rooms.get(choice - 1));
        roomsService.joinRoom(currentRoom.get(), currentUser);
        sendMessage("You have entered the room: " + currentRoom.get().getNameRoom());

        List<Message> lastMessages = roomsService.getMessagesForRoom(currentRoom.get().getId());
        for (Message message : lastMessages) {
            sendMessage(message.getUser().getUsername() + ": " + message.getMessage() + " (" + message.getTime() + ")");
        }

        inRoom = true;
        handleMessaging();
    }

    private void handleMessaging() throws IOException {
        sendMessage("Enter your message (or 'exit' to leave the room):");
        String message;
        while ((message = in.readLine()) != null) {
            if(Objects.requireNonNull(JSONUtility.fromJSON(message)).getField("command")!=null)
            {
                if ("exit".equalsIgnoreCase(JSONUtility.fromJSON(message).getField("command").toString())) {
                    sendMessage("You left the room.");
                    roomsService.leaveRoom(currentRoom.get(), currentUser);
                    currentRoom = Optional.empty();
                    inRoom = false;
                    return;
                }
            }


            if (!inRoom) {
                sendMessage("You must be in a room to send messages.");
                continue;
            }

            JSONData jsonData = JSONUtility.fromJSON(message);
            if (jsonData == null || jsonData.getField("message") == null) {
                sendMessage("Invalid message format.");
                continue;
            }

            String userMessage = jsonData.getField("message").toString();
            jsonData.addField("username", currentUser.getUsername());
            jsonData.setTime(java.time.LocalDateTime.now());

            roomsService.sendMessageToRoom(currentRoom.get(), new Message(userMessage, currentUser, currentRoom.get()));
            server.broadcastMessage(JSONUtility.toJSON(jsonData).toJSONString(), this);
            logger.info("Message sent by user " + currentUser.getUsername() + ": " + userMessage);
        }
        closeConnection();
    }


    private void closeConnection() {
        logger.info("Closing connection with client: " + clientSocket.getRemoteSocketAddress());
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage());
        }
    }
}
