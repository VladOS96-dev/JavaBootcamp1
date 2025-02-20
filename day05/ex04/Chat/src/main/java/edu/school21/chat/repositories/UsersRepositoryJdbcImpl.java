package edu.school21.chat.repositories;

import edu.school21.chat.models.User;
import edu.school21.chat.models.Chatroom;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        String query = "WITH user_chatrooms AS ( " +
                "    SELECT u.id AS user_id, cr.id AS chatroom_id, cr.room_name, cr.owner " +
                "    FROM chat.user u " +
                "    LEFT JOIN chat.user_chatroom ur ON u.id = ur.user_id " +
                "    LEFT JOIN chat.chatroom cr ON ur.chat_id = cr.id " +
                "), " +
                "created_chatrooms AS ( " +
                "    SELECT u.id AS user_id, cr.id AS chat_id, cr.room_name " +
                "    FROM chat.user u " +
                "    JOIN chat.chatroom cr ON u.id = cr.owner " +
                ") " +
                "SELECT u.id AS user_id, u.login, u.password, " +
                "       ARRAY_AGG(DISTINCT created.room_name) AS created_rooms, " +
                "       ARRAY_AGG(DISTINCT participated.room_name) AS participated_rooms " +
                "FROM chat.user u " +
                "LEFT JOIN created_chatrooms created ON u.id = created.user_id " +
                "LEFT JOIN user_chatrooms participated ON u.id = participated.user_id " +
                "GROUP BY u.id, u.login, u.password " +
                "LIMIT ? OFFSET ?";

        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, size);
            statement.setInt(2, page * size);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long userId = resultSet.getLong("user_id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");

                // Списки созданных и участвующих комнат
                List<Chatroom> createdRooms = new ArrayList<>();
                List<Chatroom> participatedRooms = new ArrayList<>();

                Array createdRoomsArray = resultSet.getArray("created_rooms");
                if (createdRoomsArray != null) {
                    String[] createdRoomNames = (String[]) createdRoomsArray.getArray();
                    for (String roomName : createdRoomNames) {
                        createdRooms.add(new Chatroom(null, roomName, null, new ArrayList<>()));
                    }
                }

                Array participatedRoomsArray = resultSet.getArray("participated_rooms");
                if (participatedRoomsArray != null) {
                    String[] participatedRoomNames = (String[]) participatedRoomsArray.getArray();
                    for (String roomName : participatedRoomNames) {
                        participatedRooms.add(new Chatroom(null, roomName, null, new ArrayList<>()));
                    }
                }

                users.add(new User(userId, login, password, createdRooms, participatedRooms));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
