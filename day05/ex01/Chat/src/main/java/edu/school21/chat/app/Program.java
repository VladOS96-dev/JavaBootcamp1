package edu.school21.chat.app;

import edu.school21.chat.models.Message;
import edu.school21.chat.repositories.DatabaseManager;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/chatdb", "postgres", "postgres");

        Scanner scanner = new Scanner(System.in);
        updateData("schema.sql", dbManager);
        updateData("data.sql", dbManager);

        while (true) {
            System.out.println("Enter a message ID");

            try {

                Long id = scanner.nextLong();
                scanner.nextLine();
                Optional<Message> message = dbManager.findMessageById(id);

                if (message.isPresent()) {
                    System.out.println(message.get());
                } else {
                    System.out.println("Message not found");
                }
                System.out.println("Exit?:yes:no");
                String str = scanner.nextLine();
                if (str.equals("yes")) {
                    dbManager.close();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

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
