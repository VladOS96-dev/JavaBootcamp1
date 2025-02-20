package school21.spring.service.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.config.TestApplicationConfig;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UsersServiceImplTest {

    private static DataSource dataSource;
    private static UsersService usersServiceJdbc;
    private static UsersService usersServiceJdbcTemplate;

    @BeforeAll
    static void beforeAll() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
        dataSource = context.getBean("dataSource", DataSource.class);
        usersServiceJdbc = context.getBean("usersServiceJdbc", UsersService.class);
        usersServiceJdbcTemplate = context.getBean("usersServiceJdbcTemplate", UsersService.class);
    }

    @BeforeEach
    public void setupDatabase() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DROP SCHEMA IF EXISTS accounts CASCADE;");
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS accounts;");

            statement.executeUpdate("CREATE TABLE accounts.users ("
                    + "id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, "
                    + "email VARCHAR(255) UNIQUE NOT NULL, "
                    + "password VARCHAR(255) NOT NULL"
                    + ")");
        } catch (SQLException e) {
            System.err.println("Ошибка при настройке базы данных: " + e.getMessage());
        }
    }

    private boolean isUserInDatabase(String email, String expectedPassword) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT password FROM accounts.users WHERE email = ?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword != null && !storedPassword.isEmpty();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке пользователя в базе данных: " + e.getMessage());
        }
        return false;
    }

    @ParameterizedTest
    @ValueSource(strings = {"alice@example.com", "bob@mail.org", "charlie@test.net"})
    public void testSignUpWithJdbc(String email) {
        String password = usersServiceJdbc.signUp(email);
        assertNotNull(password, "Пароль не должен быть null");
        assertFalse(password.isEmpty(), "Пароль не должен быть пустым");
        assertTrue(isUserInDatabase(email, password), "Пользователь должен быть сохранен в базе данных");
    }

    @ParameterizedTest
    @ValueSource(strings = {"david@example.com", "eva@mail.org", "frank@test.net"})
    public void testSignUpWithJdbcTemplate(String email) {
        String password = usersServiceJdbcTemplate.signUp(email);
        assertNotNull(password, "Пароль не должен быть null");
        assertFalse(password.isEmpty(), "Пароль не должен быть пустым");
        assertTrue(isUserInDatabase(email, password), "Пользователь должен быть сохранен в базе данных");
    }
}