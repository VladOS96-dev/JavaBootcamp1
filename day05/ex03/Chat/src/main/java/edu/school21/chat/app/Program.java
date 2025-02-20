package edu.school21.chat.app;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.DatabaseManager;


import java.time.LocalDateTime;
import java.util.ArrayList;


public class Program {
    public static void main(String[] args) {


        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/chatdb", "postgres", "postgres");


        User author = new User(3L, "worker2", "vervdv", new ArrayList(), new ArrayList());
        User creator  = new User(1L, "admin", "123456", new ArrayList(), new ArrayList());
        Chatroom room = new Chatroom(1L, "room1", creator, new ArrayList());
        Message message = new Message(11L, author, room, "Update Hello!", null);
        dbManager.updateMessage(message);
        System.out.println(message.getId());
        dbManager.close();
    }

}
