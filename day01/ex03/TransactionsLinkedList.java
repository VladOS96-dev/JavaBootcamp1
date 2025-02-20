package ex03;

import ex00.Transaction;
import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {
    private static class Node {
        Transaction transaction;
        Node next;

        Node(Transaction transaction) {
            this.transaction = transaction;
            this.next = null;
        }
    }

    private Node head;

    public TransactionsLinkedList() {
        this.head = null;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        Node newNode = new Node(transaction);
        newNode.next = head;
        head = newNode;
    }

    @Override
    public void removeTransactionByID(UUID id) {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current.transaction.getIdentifier().equals(id)) {
                if (previous == null) { // Удаление головы
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
        throw new TransactionNotFoundException("Transaction with ID " + id + " not found.");
    }

    @Override
    public Transaction[] transactionToArray() {
        int size = 0;
        Node current = head;
        while (current != null) {
            size++;
            current = current.next;
        }

        Transaction[] array = new Transaction[size];
        current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.transaction;
            current = current.next;
        }
        return array;
    }
}
