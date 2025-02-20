package ex05;

import ex00.Transaction;
import ex00.User;
import ex02.UsersArrayList;
import ex04.TransactionsService;

import java.util.Scanner;
import java.util.UUID;

public class Menu {
    private final TransactionsService service;
    private final boolean isDevMode;

    public Menu(TransactionsService service, boolean isDevMode) {
        this.service = service;
        this.isDevMode = isDevMode;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("-> ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 7) {
                    System.out.println("Exiting the program...");
                    break;
                }
                handleChoice(choice, scanner);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("---------------------------------------------------------");
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");
        if (isDevMode) {
            System.out.println("5. DEV – remove a transfer by ID");
            System.out.println("6. DEV – check transfer validity");
        }
        System.out.println("7. Finish execution");
    }

    private void handleChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1 -> addUser(scanner);
            case 2 -> viewUserBalance(scanner);
            case 3 -> performTransfer(scanner);
            case 4 -> viewUserTransactions(scanner);
            case 5 -> {
                if (isDevMode) removeTransactionById(scanner);
                else throw new IllegalStateException("Option not available in production mode.");
            }
            case 6 -> {
                if (isDevMode) validateTransactions();
                else throw new IllegalStateException("Option not available in production mode.");
            }
            default -> throw new IllegalArgumentException("Invalid menu option. Try again.");
        }
    }

    private void addUser(Scanner scanner) {
        System.out.println("Enter a user name and a balance");
        String[] input = scanner.nextLine().split(" ");
        if (input.length != 2) throw new IllegalArgumentException("Invalid input. Expected: <name> <balance>");
        String name = input[0];
        int balance = Integer.parseInt(input[1]);
        User user = new User(name, balance);
        service.addUser(user);
        System.out.println("User with id = " + user.getIdentifier() + " is added");
    }

    private void viewUserBalance(Scanner scanner) {
        System.out.println("Enter a user ID");
        int userId = Integer.parseInt(scanner.nextLine());
        User user = service.getUserList().getUserById(userId); // Добавляем доступ к UsersList
        double balance = service.getUserBalance(userId);
        System.out.println(user.getUserName() + " - " + balance);
    }


    private void performTransfer(Scanner scanner) {
        System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
        String[] input = scanner.nextLine().split(" ");
        if (input.length != 3) throw new IllegalArgumentException("Invalid input. Expected: <senderId> <recipientId> <amount>");
        int senderId = Integer.parseInt(input[0]);
        int recipientId = Integer.parseInt(input[1]);
        int amount = Integer.parseInt(input[2]);
        service.performTransfer(senderId, recipientId, amount);
        System.out.println("The transfer is completed");
    }

    private void viewUserTransactions(Scanner scanner) {
        System.out.println("Enter a user ID");
        int userId = Integer.parseInt(scanner.nextLine());
        Transaction[] transactions = service.getUserTransactions(userId);
        for (Transaction transaction : transactions) {
            transaction.printInfo();
        }
    }

    private void removeTransactionById(Scanner scanner) {
        System.out.println("Enter a user ID and a transfer ID");
        String[] input = scanner.nextLine().split(" ");
        if (input.length != 2) throw new IllegalArgumentException("Invalid input. Expected: <userId> <transferId>");
        int userId = Integer.parseInt(input[0]);
        UUID transactionId = UUID.fromString(input[1]);
        service.removeTransactionById(userId, transactionId);
        System.out.println("Transfer " + transactionId + " removed");
    }

    private void validateTransactions() {
        System.out.println("Check results:");
        Transaction[] unpairedTransactions = service.validateTransactions();
        if (unpairedTransactions.length == 0) {
            System.out.println("All transactions are paired.");
        } else {
            for (Transaction transaction : unpairedTransactions) {
                System.out.println("Unpaired transaction:");
                transaction.printInfo();
            }
        }
    }
}
