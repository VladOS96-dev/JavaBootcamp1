package edu.school21.repositories;

public class NotSavedSubEntityException extends RuntimeException {
    public NotSavedSubEntityException(String message) {
        super(message);
    }
}
