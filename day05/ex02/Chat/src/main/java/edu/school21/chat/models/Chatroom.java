package edu.school21.chat.models;

import java.util.List;
import java.util.Objects;

public class Chatroom {
    private Long id;
    private String nameRoom;
    private User owner;
    private List<Message> messagesRoom;
    public Chatroom(Long id,String nameRoom,User owner,List<Message>messagesRoom)
    {
        this.id=id;
        this.nameRoom=nameRoom;
        this.owner=owner;
        this.messagesRoom=messagesRoom;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public List<Message> getMessagesRoom() {
        return messagesRoom;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessagesRoom(List<Message> messagesRoom) {
        this.messagesRoom = messagesRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chatroom chatroom = (Chatroom) o;
        return Objects.equals(id, chatroom.id) && Objects.equals(nameRoom, chatroom.nameRoom) && Objects.equals(owner, chatroom.owner) && Objects.equals(messagesRoom, chatroom.messagesRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameRoom, owner, messagesRoom);
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", nameRoom='" + nameRoom + '\'' +
                ", owner=" + owner +
                ", messagesRoom=" + messagesRoom +
                '}';
    }
}
