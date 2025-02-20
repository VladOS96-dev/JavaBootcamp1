package edu.school21.sockets.models;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Data
@NoArgsConstructor
public class Message {
    private Long id;
    private String message;
    private LocalDateTime time;
    private User user;
    private Chatroom room;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public Message(String message,User user,Chatroom room) {
        this.message = message;
        this.time = LocalDateTime.now();
        this.user=user;
        this.room=room;
    }
}