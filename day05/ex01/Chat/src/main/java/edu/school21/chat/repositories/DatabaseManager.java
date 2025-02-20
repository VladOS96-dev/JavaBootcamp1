package edu.school21.chat.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.Message;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseManager {
    private final HikariDataSource dataSource;
    private final MessagesRepository messagesRepository;

    public DatabaseManager(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        this.dataSource = new HikariDataSource(config);
        this.messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);
    }

    public Optional<Message> findMessageById(Long id) {
        return messagesRepository.findById(id);
    }
    public Connection getConnection()throws SQLException
    {
        return dataSource.getConnection();
    }
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}