package ex05;

import ex02.UsersArrayList;
import ex04.TransactionsService;

public class Program {
    public static void main(String[] args) {
        boolean isDevMode = false;
        for (String arg : args) {
            if (arg.equals("--profile=dev")) {
                isDevMode = true;
            }
        }

        UsersArrayList userList = new UsersArrayList();
        TransactionsService service = new TransactionsService(userList);
        Menu menu = new Menu(service, isDevMode);
        menu.start();
    }
}
