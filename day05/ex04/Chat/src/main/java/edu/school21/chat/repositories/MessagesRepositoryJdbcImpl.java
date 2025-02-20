package edu.school21.chat.repositories;

import edu.school21.chat.exception.NotSavedSubEntityException;
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

    @Override
    public void save(Message message) {

        String authorQuery = "SELECT u.id FROM chat.user u WHERE u.id = ?";
        boolean authorExists = false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(authorQuery)) {
            stmt.setLong(1, message.getAuthor().getUserID());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    authorExists = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования автора", e);
        }

        if (!authorExists) {
            throw new NotSavedSubEntityException("Автор с ID " + message.getAuthor().getUserID() + " не найден в базе данных");
        }


        String roomQuery = "SELECT c.id FROM chat.chatroom c WHERE c.id = ?";
        boolean roomExists = false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(roomQuery)) {
            stmt.setLong(1, message.getRoom().getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    roomExists = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования комнаты", e);
        }

        if (!roomExists) {
            throw new NotSavedSubEntityException("Комната с ID " + message.getRoom().getId() + " не найдена в базе данных");
        }


        String insertMessageQuery = "insert into chat.message(author,room,text) values\n" +
                "((select id from chat.user where login=?),(select id from chat.chatroom where room_name=?),?)ON CONFLICT DO NOTHING;";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertMessageQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, message.getAuthor().getLogin());
            stmt.setString(2, message.getRoom().getNameRoom());
            stmt.setString(3, message.getText());


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Не удалось вставить сообщение");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении сообщения", e);
        }
    }

    @Override
    public void update(Message message) {
        String messageExistsQuery = "SELECT m.id FROM chat.message m WHERE m.id = ?";
        boolean messageExists = false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(messageExistsQuery)) {
            stmt.setLong(1, message.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    messageExists = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования сообщения", e);
        }

        if (!messageExists) {

            save(message);
        } else {

            String updateMessageQuery = "UPDATE chat.message SET text = ?, date_time = ?, author = ?, room = ? WHERE id = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(updateMessageQuery)) {
                stmt.setString(1, message.getText());
                stmt.setTimestamp(2, message.getDateTime() != null ? Timestamp.valueOf(message.getDateTime()) : null);
                stmt.setLong(3, message.getAuthor().getUserID());
                stmt.setLong(4, message.getRoom().getId());
                stmt.setLong(5, message.getId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Не удалось обновить сообщение");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при обновлении сообщения", e);
            }
        }
    }
}
