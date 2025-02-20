package ex04;

import ex00.Transaction;
import ex00.User;
import ex02.UsersArrayList;

import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        UsersArrayList userList = new UsersArrayList();
        TransactionsService service = new TransactionsService(userList);

        User alice = new User("Alice", 1000);
        User bob = new User("Bob", 500);

        service.addUser(alice);
        service.addUser(bob);

        System.out.println("Initial balances:");
        System.out.println("Alice: " + service.getUserBalance(alice.getIdentifier()));
        System.out.println("Bob: " + service.getUserBalance(bob.getIdentifier()));

        // Выполняем перевод
        service.performTransfer(alice.getIdentifier(), bob.getIdentifier(), 200);

        System.out.println("\nBalances after transfer:");
        System.out.println("Alice: " + service.getUserBalance(alice.getIdentifier()));
        System.out.println("Bob: " + service.getUserBalance(bob.getIdentifier()));

        System.out.println("\nAlice's Transactions:");
        for (Transaction transaction : service.getUserTransactions(alice.getIdentifier())) {
            transaction.printInfo();
        }

        System.out.println("\nBob's Transactions:");
        for (Transaction transaction : service.getUserTransactions(bob.getIdentifier())) {
            transaction.printInfo();
        }

        // Получаем транзакции Боба и удаляем одну
        System.out.println("\nRemoving Bob's transaction...");
        Transaction[] bobTransactions = service.getUserTransactions(bob.getIdentifier());
        if (bobTransactions.length > 0) {
            UUID transactionIdToRemove = bobTransactions[0].getIdentifier();
            service.removeTransactionById(bob.getIdentifier(), transactionIdToRemove);

            System.out.println("Transaction removed. Bob's remaining transactions:");
            for (Transaction transaction : service.getUserTransactions(bob.getIdentifier())) {
                transaction.printInfo();
            }
        } else {
            System.out.println("Bob has no transactions to remove.");
        }

        System.out.println("\nValidating transactions...");
        Transaction[] unpairedTransactions = service.validateTransactions();
        if (unpairedTransactions.length == 0) {
            System.out.println("All transactions are paired.");
        } else {
            System.out.println("Unpaired transactions found:");
            for (Transaction transaction : unpairedTransactions) {
                transaction.printInfo();
            }
        }
    }
}
