package ex00;
import ex01.UserIdsGenerator;
import ex03.TransactionsLinkedList;
import ex03.TransactionsList;

public class User {
    private final int identifier;
    private double balance;
    private String userName;
    private TransactionsList transactions;

    public User(String userName, double balance) {
        this.userName = userName;
        this.balance = balance;
        this.identifier = UserIdsGenerator.getInstance().generateId();
        this.transactions = new TransactionsLinkedList();
    }

    public double getBalance() {
        return balance;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getUserName() {
        return userName;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TransactionsList getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.addTransaction(transaction);
    }

    public void printInfo() {
        System.out.println("User name: " + userName);
        System.out.println("User id: " + identifier);
        System.out.println("Balance: " + balance);
    }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + userName + '\'' +
                ", Identifier=" + identifier +
                ", Balance=" + balance +
                '}';
    }
}
