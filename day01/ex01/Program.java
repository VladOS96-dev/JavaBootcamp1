package ex01;
import ex00.User;
public class Program {
    public static void main(String[] args) {
        User user = new User("Sergey", 100);
        User user1 = new User("Sergey", 100);
        User user2 = new User("Sergey", 100);
        User user3 = new User("Sergey", 100);
        User user4 = new User("Sergey", 100);
        User user5 = new User("Sergey", 100);
        user.printInfo();
        user1.printInfo();
        user2.printInfo();
        user3.printInfo();
        user4.printInfo();
        user5.printInfo();
    }
}
