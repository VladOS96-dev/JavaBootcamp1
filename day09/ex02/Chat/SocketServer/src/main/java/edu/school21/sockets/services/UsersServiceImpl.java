package edu.school21.sockets.services;


import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessagesRepository;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public User getUserByUsername(String username) {
        return usersRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean signIn(String username, String plainPassword) {
        Optional<User> storedUser = usersRepository.findByUsername(username);

        if (storedUser.isPresent()) {
            User user = storedUser.get();
            return passwordEncoder.matches(plainPassword, user.getPassword());
        }
        return false;
    }


}