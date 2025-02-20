package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;

import java.util.List;

public interface UsersService {
    boolean signUp(User user);
    boolean signIn(String username, String plainPassword);

    User getUserByUsername(String username);
}
