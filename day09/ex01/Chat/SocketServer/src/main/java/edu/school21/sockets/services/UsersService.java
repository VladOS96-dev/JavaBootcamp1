package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface UsersService {
    boolean signUp(User user);
    boolean signIn(User user);
    void createMessage(String message);
}
