package ex03;

import ex00.Transaction;
import ex00.User;
import ex00.TransferCategory;

public class Program {
    public static void main(String[] args) {
        User sender = new User("Alice", 1000);
        User recipient = new User("Bob", 500);

        Transaction transaction1 = new Transaction(recipient, sender, TransferCategory.DEBIT, 200);
        Transaction transaction2 = new Transaction(sender, recipient, TransferCategory.CREDIT, 150);

        sender.addTransaction(transaction1);
        sender.addTransaction(transaction2);

        System.out.println("Transactions for sender:");
        for (Transaction transaction : sender.getTransactions().transactionToArray()) {
            transaction.printInfo();
        }

        System.out.println("Removing transaction...");
        sender.getTransactions().removeTransactionByID(transaction1.getIdentifier());

        System.out.println("Transactions after removal:");
        for (Transaction transaction : sender.getTransactions().transactionToArray()) {
            transaction.printInfo();
        }
    }
}
