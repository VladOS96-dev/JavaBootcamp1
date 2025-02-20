package edu.school21.sockets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chatroom {
    private Long id;
    private String nameRoom;
    private String owner;
    private List<User> users= new ArrayList<>();;
    private List<Message> messages= new ArrayList<>();;

    public Chatroom(String nameRoom, String owner) {
        this.nameRoom = nameRoom;
        this.owner = owner;
    }
}