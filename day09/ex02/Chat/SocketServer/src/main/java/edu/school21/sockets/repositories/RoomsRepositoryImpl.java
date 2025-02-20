package edu.school21.sockets.repositories;


import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class RoomsRepositoryImpl implements RoomsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;
    @Autowired
    public RoomsRepositoryImpl(DataSource dataSource, ResourceLoader resourceLoader) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.resourceLoader = resourceLoader;
        init_table_database();
    }

    private void init_table_database() {
        Resource resource = resourceLoader.getResource("classpath:schema.sql");

        try {
            String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Error loading schema.sql file", e);
        }
    }

    @Override
    public Chatroom findById(Long id) {
        String query = "SELECT * FROM app.rooms WHERE id = ?";
        Chatroom room = jdbcTemplate.query(query,
                new Object[]{id},
                new int[]{Types.INTEGER},
                new BeanPropertyRowMapper<>(Chatroom.class)).stream().findAny().orElse(null);
        return room;
    }

    @Override
    public List<Chatroom> findAll() {
        String query = "SELECT * FROM app.rooms";
        List<Chatroom> rooms = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Chatroom.class));
        System.out.println("Rooms fetched from database: " + rooms);
        return rooms;
    }

    @Override
    public void save(Chatroom entity) {
        String query = "INSERT INTO app.rooms (name_room, owner) VALUES (?, ?)";
        int result = jdbcTemplate.update(query, entity.getNameRoom(), entity.getOwner());
        if (result == 0) {
            System.err.println("Room wasn't saved: " + entity);
        }
    }

    @Override
    public void update(Chatroom entity) {
        String query = "UPDATE app.rooms SET name_room = ?, owner = ? WHERE id = ?";
        int result = jdbcTemplate.update(query, entity.getNameRoom(),
                entity.getOwner(), entity.getId());

        if (result == 0) {
            System.err.println("Room wasn't updated: " + entity);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM app.rooms WHERE id = ?";
        int result = jdbcTemplate.update(query, id);

        if (result == 0) {
            System.err.println("Room not found with id: " + id);
        }
    }

    @Override
    public Optional<Chatroom> findByNameRoom(String nameRoom) {
        String query = "SELECT * FROM app.rooms WHERE name_room = ?";

        Chatroom room = jdbcTemplate.query(query,
                new Object[]{nameRoom},
                new int[]{Types.VARCHAR},
                new BeanPropertyRowMapper<>(Chatroom.class)).stream().findAny().orElse(null);
        return Optional.ofNullable(room);
    }
    @Override
    public void updateUsersInRoom(Chatroom room) {
        String deleteQuery = "DELETE FROM app.users_rooms WHERE room_id = ?";
        jdbcTemplate.update(deleteQuery, room.getId());

        String insertQuery = "INSERT INTO app.users_rooms (room_id, user_id) VALUES (?, ?)";
        for (User user : room.getUsers()) {
            if (user.getId() == null) {
                System.err.println("Ошибка: пытаемся вставить пользователя без ID! " + user.getUsername());
                continue;
            }
            jdbcTemplate.update(insertQuery, room.getId(), user.getId());
        }
    }
    public List<User> getUsersInRoom(Long roomId) {
        String query = "SELECT u.* FROM app.users u " +
                "JOIN app.users_rooms ur ON u.id = ur.user_id " +
                "WHERE ur.room_id = ?";
        return jdbcTemplate.query(query, new Object[]{roomId}, new BeanPropertyRowMapper<>(User.class));
    }


}