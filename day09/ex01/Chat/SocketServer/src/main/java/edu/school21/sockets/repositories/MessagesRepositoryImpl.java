package edu.school21.sockets.repositories;


import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
            Message message = new Message();
            message.setMessage(rs.getString("message"));
            message.setTime(rs.getTimestamp("time").toLocalDateTime());
            return message;
        }
    }

    @Override
    public Message findById(Long id) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        String selectAllQuery = "SELECT * FROM ex01.message";
        return jdbcTemplate.query(selectAllQuery, new MessageRowMapper());
    }

    @Override
    public void save(Message entity) {
        String insertQuery = "INSERT INTO ex01.message (message, time) VALUES (?, CURRENT_TIMESTAMP)";
        int rowsAffected = jdbcTemplate.update(insertQuery, entity.getMessage());

        if (rowsAffected == 0) {
            System.err.println("Message wasn't saved: " + entity);
        }
    }

    @Override
    public void update(Message entity) {
    }

    @Override
    public void delete(Long id) {
    }
}