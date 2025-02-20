package ex04;

import ex02.UsersList;
import ex00.User;
import ex00.Transaction;
import ex00.TransferCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionsService {
    private final UsersList users;

    public TransactionsService(UsersList users) {
        this.users = users;
    }


    public void addUser(User user) {
        users.addUser(user);
    }
    public UsersList getUserList() {
        return users;
    }


    public double getUserBalance(int userId) {
        User user = users.getUserById(userId);
        return user.getBalance();
    }


    public void performTransfer(int senderId, int receiverId, double amount) {
        if (amount <= 0) {
            throw new IllegalTransactionException("Transfer amount must be positive.");
        }

        User sender = users.getUserById(senderId);
        User receiver = users.getUserById(receiverId);

        if (sender.getBalance() < amount) {
            throw new IllegalTransactionException("Insufficient funds for the transfer.");
        }


        UUID transactionId = UUID.randomUUID();
        Transaction debitTransaction = new Transaction(receiver, sender, TransferCategory.DEBIT, amount);
        debitTransaction.setIdentifier(transactionId);

        Transaction creditTransaction = new Transaction(receiver, sender, TransferCategory.CREDIT, -amount);
        creditTransaction.setIdentifier(transactionId);

        sender.addTransaction(debitTransaction);
        receiver.addTransaction(creditTransaction);
    }


    public Transaction[] getUserTransactions(int userId) {
        User user = users.getUserById(userId);
        return user.getTransactions().transactionToArray();
    }


    public void removeTransactionById(int userId, UUID transactionId) {
        User user = users.getUserById(userId);
        user.getTransactions().removeTransactionByID(transactionId);
    }


    public Transaction[] validateTransactions() {
        List<Transaction> unpairedTransactions = new ArrayList<>();

        int userCount = users.getNumberOfUsers();
        for (int i = 0; i < userCount; i++) {
            User user = users.getUserByIndex(i);
            Transaction[] transactions = user.getTransactions().transactionToArray();

            for (Transaction transaction : transactions) {
                if (!isTransactionPaired(transaction, user)) {
                    unpairedTransactions.add(transaction);
                }
            }
        }

        return unpairedTransactions.toArray(new Transaction[0]);
    }

    private boolean isTransactionPaired(Transaction transaction, User currentUser) {
        int userCount = users.getNumberOfUsers();

        for (int i = 0; i < userCount; i++) {
            User otherUser = users.getUserByIndex(i);
            if (otherUser == currentUser) {
                continue;
            }

            Transaction[] otherTransactions = otherUser.getTransactions().transactionToArray();
            for (Transaction otherTransaction : otherTransactions) {
                if (transaction.getIdentifier().equals(otherTransaction.getIdentifier())) {
                    return true;
                }
            }
        }

        return false;
    }

}
