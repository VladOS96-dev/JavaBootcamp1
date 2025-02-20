package edu.school21.chat.app;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.DatabaseManager;
import edu.school21.chat.repositories.UsersRepositoryJdbcImpl;


import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Program {
    public static void main(String[] args) {


        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/chatdb", "postgres", "postgres");
        DataSource dataSource = dbManager.getDataSource();
        Scanner scanner = new Scanner(System.in);
        updateData("schema.sql", dbManager);
        updateData("data.sql", dbManager);

        UsersRepositoryJdbcImpl usersRepository = new UsersRepositoryJdbcImpl(dataSource);


        List<User> users = usersRepository.findAll(0, 5);
        for (User user : users) {
            System.out.println(user);
        }

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
