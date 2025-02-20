package school21.spring.service.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import school21.spring.service.models.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;

    public UsersRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("email"));

    @Override
    public Optional<User> findById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(query, userRowMapper, id).stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, userRowMapper);
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO users (email) VALUES (?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(query, Long.class, user.getEmail());
        user.setId(id);
    }

    @Override
    public void update(User user) {
        String updateSQL = "UPDATE users SET email = ? WHERE id = ?";
        jdbcTemplate.update(updateSQL, user.getEmail(), user.getId());
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(query, userRowMapper, email).stream().findFirst();
    }
}
