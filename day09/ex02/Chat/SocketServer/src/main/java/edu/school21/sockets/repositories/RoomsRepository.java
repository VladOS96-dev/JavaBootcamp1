package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;

import java.util.List;
import java.util.Optional;

public interface RoomsRepository extends CrudRepository<Chatroom> {
    Optional<Chatroom> findByNameRoom(String nameRoom);
    List<Chatroom> findAll();
    void updateUsersInRoom(Chatroom room);
    List<User> getUsersInRoom(Long roomId);
}