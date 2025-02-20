package ex02;
import ex00.User;

public class Program {
    public static void main(String[] args) {
        UsersList usersList = new UsersArrayList();


        for (int i = 0; i < 15; i++) {
            usersList.addUser(new User("User" + i, 100 + i));
        }


        System.out.println("Count user: " + usersList.getNumberOfUsers());
        System.out.println("User with ID 5: " + usersList.getUserById(5).getUserName());
        System.out.println("User with index 2: " + usersList.getUserByIndex(2).getUserName());


        try {
            usersList.getUserById(20);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            usersList.getUserByIndex(20);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
