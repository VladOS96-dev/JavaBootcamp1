package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;

import java.util.List;
import java.util.Optional;

public interface RoomsService {
    boolean createRoom(String roomName, User owner);
    List<Chatroom> getAllRooms();
    Optional<Chatroom> getRoomByName(String roomName); // Новый метод
    boolean joinRoom(Chatroom room, User user);
    boolean leaveRoom(Chatroom room, User user);
    void sendMessageToRoom(Chatroom room, Message message);
    List<Message> getLastMessages(Chatroom room);
    List<Message> getMessagesForRoom(Long roomId);
}
