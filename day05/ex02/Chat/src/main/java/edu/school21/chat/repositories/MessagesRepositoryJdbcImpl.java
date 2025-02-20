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
        String validateAuthorQuery = "SELECT id, login, password FROM chat.user WHERE id = ?";
        String validateRoomQuery = "SELECT id, room_name, owner FROM chat.chatroom WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            // Проверка автора
            try (PreparedStatement authorStmt = conn.prepareStatement(validateAuthorQuery)) {
                authorStmt.setLong(1, message.getAuthor().getUserID());
                try (ResultSet rs = authorStmt.executeQuery()) {
                    if (!rs.next() ||
                            !rs.getString("login").equals(message.getAuthor().getLogin()) ||
                            !rs.getString("password").equals(message.getAuthor().getPassword())) {
                        throw new NotSavedSubEntityException("Автор не существует или данные (логин/пароль) не совпадают");
                    }
                }
            }

            // Проверка комнаты
            try (PreparedStatement roomStmt = conn.prepareStatement(validateRoomQuery)) {
                roomStmt.setLong(1, message.getRoom().getId());
                try (ResultSet rs = roomStmt.executeQuery()) {
                    if (!rs.next() ||
                            !rs.getString("room_name").equals(message.getRoom().getNameRoom()) ||
                            rs.getLong("owner") != message.getRoom().getOwner().getUserID()) {
                        throw new NotSavedSubEntityException("Комната не существует или данные (имя комнаты/владелец) не совпадают");
                    }
                }
            }

            // Вставка сообщения
            String insertMessageQuery =
                    "INSERT INTO chat.message (author, room, text, date_time) " +
                            "VALUES (?, ?, ?, ?) " +
                            "RETURNING id;";

            try (PreparedStatement stmt = conn.prepareStatement(insertMessageQuery)) {
                stmt.setLong(1, message.getAuthor().getUserID());
                stmt.setLong(2, message.getRoom().getId());
                stmt.setString(3, message.getText());
                stmt.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        message.setId(rs.getLong(1));
                    } else {
                        throw new NotSavedSubEntityException("Не удалось вставить сообщение");
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении сообщения", e);
        }
    }

}
