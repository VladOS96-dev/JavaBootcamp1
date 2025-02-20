package ex01;

public class UserIdsGenerator {
    private static UserIdsGenerator instance;
    private int id = 0;


    private UserIdsGenerator() {
    }


    public static synchronized UserIdsGenerator getInstance() {
        if (instance == null) {
            instance = new UserIdsGenerator();
        }
        return instance;
    }


    public synchronized int generateId() {
        return id++;
    }
}
