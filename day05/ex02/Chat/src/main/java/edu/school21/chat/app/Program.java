package edu.school21.chat.app;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.DatabaseManager;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;


public class Program {
    public static void main(String[] args) {


        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/chatdb", "postgres", "postgres");
        updateData("schema.sql", dbManager);
        updateData("data.sql", dbManager);

        User author = new User(3L, "worker2", "vervdv", new ArrayList(), new ArrayList());
        User creator  = new User(1L, "admin", "123456", new ArrayList(), new ArrayList());
        Chatroom room = new Chatroom(1L, "room1", creator, new ArrayList());
        Message message = new Message(null, author, room, "Hello Peer!", LocalDateTime.now());
        dbManager.saveMessage(message);
        System.out.println(message.getId());
        dbManager.close();
    }
    private static void updateData(String file, DatabaseManager dbManager) {
        try (Connection con = dbManager.getConnection();
             Statement st = con.createStatement()) {
            InputStream input = Program.class.getClassLoader().getResourceAsStream(file);
            Scanner scanner = new Scanner(input).useDelimiter(";");

            while (scanner.hasNext()) {
                st.executeUpdate(scanner.next().trim());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
