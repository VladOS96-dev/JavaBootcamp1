package edu.school21.sockets.services;


import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessagesRepository;
import edu.school21.sockets.repositories.RoomsRepository;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class RoomsServiceImpl implements RoomsService {
    private final RoomsRepository roomsRepository;
    private final MessagesRepository messagesRepository;

    @Autowired
    public RoomsServiceImpl(RoomsRepository roomsRepository, MessagesRepository messagesRepository) {
        this.roomsRepository = roomsRepository;
        this.messagesRepository = messagesRepository;
    }

    @Override
    public boolean createRoom(String roomName, User owner) {
        if (roomsRepository.findByNameRoom(roomName).isPresent()) {
            return false;
        }
        Chatroom newRoom = new Chatroom(roomName, owner.getUsername());
        roomsRepository.save(newRoom);
        return true;
    }

    @Override
    public List<Chatroom> getAllRooms() {
        return roomsRepository.findAll();
    }

    @Override
    public Optional<Chatroom> getRoomByName(String roomName) {
        return roomsRepository.findByNameRoom(roomName);
    }

    @Override
    public boolean joinRoom(Chatroom room, User user) {
        if (user.getId() == null) {
            System.err.println("Ошибка: ID пользователя не может быть NULL.");
            return false;
        }

        if (!room.getUsers().contains(user)) {
            room.getUsers().add(user);
            roomsRepository.updateUsersInRoom(room);
        }
        return true;
    }


    @Override
    public boolean leaveRoom(Chatroom room, User user) {
        room.getUsers().remove(user);
        return true;
    }

    @Override
    public void sendMessageToRoom(Chatroom room, Message message) {
        room.getMessages().add(message);
        messagesRepository.save(message);

    }

    @Override
    public List<Message> getLastMessages(Chatroom room) {
        return room.getMessages().subList(Math.max(room.getMessages().size() - 30, 0), room.getMessages().size());
    }
    @Override
    public List<Message> getMessagesForRoom(Long roomId) {
        return messagesRepository.findMessagesByRoomId(roomId);
    }
}
