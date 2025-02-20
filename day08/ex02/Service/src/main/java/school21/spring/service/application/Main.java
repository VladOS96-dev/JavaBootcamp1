package school21.spring.service.application;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import school21.spring.service.config.ApplicationConfig;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;
import school21.spring.service.services.UsersService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        DataSource dataSource = context.getBean("hikariDataSource", DataSource.class);
        createTable(dataSource);

        UsersService usersService = context.getBean(UsersService.class);
        UsersRepository usersRepositoryJdbc = context.getBean("jdbcRepository", UsersRepository.class);
        UsersRepository usersRepositoryJdbcTemplate = context.getBean("usersRepositoryJdbcTemplateImpl", UsersRepository.class);

        List<String> emails = List.of("user1@example.com", "user2@example.com", "user3@example.com");

        for (String email : emails) {
            String password = usersService.signUp(email);
            System.out.println("Создан пользователь " + email + " с паролем: " + password);
        }

        System.out.println("\nСписок пользователей через JDBC:");
        usersRepositoryJdbc.findAll().forEach(System.out::println);

        System.out.println("\nСписок пользователей через JdbcTemplate:");
        usersRepositoryJdbcTemplate.findAll().forEach(System.out::println);

        long userIdToFind = 1;
        String emailToFind = "user2@example.com";

        Optional<User> userJdbc = usersRepositoryJdbc.findById(userIdToFind);
        Optional<User> userJdbcTemplate = usersRepositoryJdbcTemplate.findById(userIdToFind);

        System.out.println("\nПоиск по ID через JDBC: " + userJdbc.orElse(null));
        System.out.println("Поиск по ID через JdbcTemplate: " + userJdbcTemplate.orElse(null));

        Optional<User> userByEmailJdbc = usersRepositoryJdbc.findByEmail(emailToFind);
        Optional<User> userByEmailJdbcTemplate = usersRepositoryJdbcTemplate.findByEmail(emailToFind);

        System.out.println("\nПоиск по email через JDBC: " + userByEmailJdbc.orElse(null));
        System.out.println("Поиск по email через JdbcTemplate: " + userByEmailJdbcTemplate.orElse(null));

        context.close();
    }

    private static void createTable(DataSource dataSource) {
        String dropTableSQL = "DROP TABLE IF EXISTS accounts.users CASCADE;";
        String createSchemaSQL="CREATE SCHEMA IF NOT EXISTS accounts;";
        String createTableSQL = "CREATE TABLE accounts.users ("
                + "id SERIAL PRIMARY KEY, "
                + "email VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL"
                + ")";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(dropTableSQL);
            System.out.println("Старая таблица 'users' удалена.");
            statement.execute(createSchemaSQL);
            statement.execute(createTableSQL);
            System.out.println("Создана новая таблица 'users'.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при пересоздании таблицы: " + e.getMessage(), e);
        }
    }
}
