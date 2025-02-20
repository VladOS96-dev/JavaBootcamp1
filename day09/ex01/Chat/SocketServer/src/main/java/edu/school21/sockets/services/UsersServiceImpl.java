package edu.school21.sockets.services;


import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessagesRepository;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsersServiceImpl implements UsersService {
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final MessagesRepository messagesRepository;

    @Autowired
    public UsersServiceImpl(PasswordEncoder passwordEncoder, UsersRepository usersRepository, MessagesRepository messagesRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.messagesRepository = messagesRepository;
    }

    @Override
    public boolean signUp(User user) {
        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        return true;
    }

    @Override
    public boolean signIn(User user) {
        Optional<User> existingUser = usersRepository.findByUsername(user.getUsername());
        return existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword());
    }

    @Override
    public void createMessage(String message) {
        messagesRepository.save(new Message(message));
    }
}