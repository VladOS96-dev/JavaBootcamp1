package school21.spring.service.application;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
public class Main {

        public static void main(String[] args) {
            ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

            createUsersTable(context);
            clearUsersTable(context);
            UsersRepository usersRepositoryJdbc = context.getBean("usersRepositoryJdbc", UsersRepository.class);
            testRepository(usersRepositoryJdbc, "JDBC Statement");
            clearUsersTable(context);
            UsersRepository usersRepositoryJdbcTemplate = context.getBean("usersRepositoryJdbcTemplate", UsersRepository.class);
            testRepository(usersRepositoryJdbcTemplate, "JdbcTemplate");
        }

        private static void createUsersTable(ApplicationContext context) {
            try (Connection connection = context.getBean("driverManagerDataSource", javax.sql.DataSource.class).getConnection();
                 Statement statement = connection.createStatement()) {

                String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                        "id SERIAL PRIMARY KEY, " +
                        "email VARCHAR(255) UNIQUE NOT NULL" +
                        ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("Table 'users' created successfully or already exists.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error while creating table.");
            }
        }

    private static void clearUsersTable(ApplicationContext context) {
        try (Connection connection = context.getBean("driverManagerDataSource", DataSource.class).getConnection();
             Statement statement = connection.createStatement()) {

            String deleteSQL = "DELETE FROM users";
            statement.executeUpdate(deleteSQL);
            System.out.println("Table 'users' cleared successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while clearing table.");
        }
    }
        private static void testRepository(UsersRepository repository, String repoName) {
            System.out.println("\nTesting repository: " + repoName);


            User user1 = new User(null, "user1@example.com");
            User user2 = new User(null, "user2@example.com");

            repository.save(user1);
            repository.save(user2);

            System.out.println("Saved users:");
            System.out.println(repository.findAll());


            repository.findById(user1.getId()).ifPresent(user ->
                    System.out.println("Found by ID: " + user));


            repository.findByEmail("user2@example.com").ifPresent(user ->
                    System.out.println("Found by email: " + user));


            user1.setEmail("updated@example.com");
            repository.update(user1);
            System.out.println("Updated user: " + repository.findById(user1.getId()).orElse(null));


            repository.delete(user2.getId());
            System.out.println("After deletion:");
            System.out.println(repository.findAll());
        }
}
