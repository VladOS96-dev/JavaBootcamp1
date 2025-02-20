package edu.school21.chat.repositories;

import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.models.Chatroom;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final DataSource dataSource;

    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Message> findById(Long id) {
        String query = "SELECT m.id, m.text, m.date_time, " +
                "u.id AS user_id, u.login, u.password, " +
                "c.id AS room_id, c.room_name " +
                "FROM chat.message m " +
                "JOIN chat.user u ON m.author = u.id " +
                "JOIN chat.chatroom c ON m.room = c.id " +
                "WHERE m.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User author = new User(
                            rs.getLong("user_id"),
                            rs.getString("login"),
                            rs.getString("password"),
                            null, null
                    );
                    Chatroom room = new Chatroom(
                            rs.getLong("room_id"),
                            rs.getString("room_name"),
                            null, null
                    );
                    Message message = new Message(
                            rs.getLong("id"),
                            author,
                            room,
                            rs.getString("text"),
                            rs.getTimestamp("date_time").toLocalDateTime()
                    );
                    return Optional.of(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
