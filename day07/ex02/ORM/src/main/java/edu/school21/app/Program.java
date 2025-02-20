package edu.school21.app;

import edu.school21.models.Car;
import edu.school21.models.User;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import java.sql.SQLException;

public class Program {
    private static final String PACKAGE_NAME = "edu.school21.models.";
    private EmbeddedDatabase dataSource;
    private OrmManager manager;

    public static void main(String[] args) throws SQLException, ReflectiveOperationException {
            Program program = new Program();
            program.dataSource = new EmbeddedDatabaseBuilder().build();
            program.manager = new OrmManager(PACKAGE_NAME, program.dataSource);
            program.manager.init();
            program.testSave();
            program.testFind();
            program.testUpdate();
            program.dataSource.shutdown();
    }

    private void testSave() throws SQLException, IllegalAccessException {
        System.out.println("[SAVE TEST]");
        User user1 = new User(1L, "Alex", "alex@mail.com", 25);
        User user2 = new User(2L, "Maria", "maria@mail.com", 30);
        manager.save(user1);
        manager.save(user2);

        Car car1 = new Car("Toyota", "Camry", 2020);
        Car car2 = new Car("Ford", "Mustang", 2022);
        manager.save(car1);
        manager.save(car2);
    }

    private void testFind() throws SQLException, ReflectiveOperationException {
        System.out.println("[FIND TEST]");
        User user = manager.findById(1L, User.class);
        System.out.println(user);

        Car car = manager.findById(2L, Car.class);
        System.out.println(car);
    }

    private void testUpdate() throws SQLException, IllegalAccessException, ReflectiveOperationException {
        System.out.println("[UPDATE TEST]");

        User user = manager.findById(2L, User.class);
        System.out.println("Before update: " + user);
        user.setFirstName("Updated Maria");
        manager.update(user);
        System.out.println("After update: " + manager.findById(2L, User.class));

        Car car = manager.findById(1L, Car.class);
        System.out.println("Before update: " + car);
        car.setBrand("Updated Toyota");
        manager.update(car);
        System.out.println("After update: " + manager.findById(1L, Car.class));
    }
}
