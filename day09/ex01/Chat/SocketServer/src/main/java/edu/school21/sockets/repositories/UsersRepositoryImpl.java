package edu.school21.sockets.repositories;


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
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;
    @Autowired
    public UsersRepositoryImpl(JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader=resourceLoader;
        initializeSchema();
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"));

    private void initializeSchema() {
        Resource resource = resourceLoader.getResource("classpath:schema.sql");

        try {
            String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));

            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Error loading schema.sql file", e);
        }

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM ex01.users WHERE username = ?", userRowMapper, username)
                .stream().findFirst();
    }

    @Override
    public User findById(Long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM ex01.users WHERE id = ?", userRowMapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM ex01.users", userRowMapper);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO ex01.users (username, password) VALUES (?, ?)",
                user.getUsername(), user.getPassword());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE ex01.users SET username = ?, password = ? WHERE id = ?",
                user.getUsername(), user.getPassword(), user.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM ex01.users WHERE id = ?", id);
    }
}