package ex03;
import ex00.Transaction;
import java.util.UUID;
public interface TransactionsList {
public void addTransaction(Transaction transaction);
public  void removeTransactionByID(UUID id);
public  Transaction [] transactionToArray();
}
