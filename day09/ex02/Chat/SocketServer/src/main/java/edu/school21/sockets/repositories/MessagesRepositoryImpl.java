package edu.school21.sockets.repositories;


import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


@Repository
public class MessagesRepositoryImpl implements MessagesRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    @Autowired
    public MessagesRepositoryImpl(JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
        initializeSchema();
    }

    private void initializeSchema() {
        Resource resource = resourceLoader.getResource("classpath:schema.sql");

        try {
            String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Error loading schema.sql file", e);
        }
    }

    private static class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));

            Chatroom room = new Chatroom();
            room.setId(rs.getLong("room_id"));
            room.setNameRoom(rs.getString("name_room"));

            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setMessage(rs.getString("message"));
            message.setTime(rs.getTimestamp("time").toLocalDateTime());
            message.setUser(user);
            message.setRoom(room);

            return message;
        }
    }

    @Override
    public Message findById(Long id) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        String selectAllQuery = "SELECT * FROM app.message";
        return jdbcTemplate.query(selectAllQuery, new MessageRowMapper());
    }

    @Override
    public void save(Message entity) {
        String insertQuery = "INSERT INTO app.message (message, time, user_id, room_id) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertQuery, entity.getMessage(), entity.getUser().getId(), entity.getRoom().getId());

        if (rowsAffected == 0) {
            System.err.println("Message wasn't saved: " + entity);
        }
    }
    @Override
    public List<Message> findMessagesByRoomId(Long roomId) {
        String query = """
        SELECT m.id, m.message, m.time, 
               u.id AS user_id, u.username, 
               r.id AS room_id, r.name_room 
        FROM app.message m
        JOIN app.users u ON m.user_id = u.id
        JOIN app.rooms r ON m.room_id = r.id
        WHERE m.room_id = ?
        ORDER BY m.time DESC
        LIMIT 30
    """;
        return jdbcTemplate.query(query, new Object[]{roomId}, new MessageRowMapper());
    }
    @Override
    public void update(Message entity) {
    }

    @Override
    public void delete(Long id) {
    }
}
