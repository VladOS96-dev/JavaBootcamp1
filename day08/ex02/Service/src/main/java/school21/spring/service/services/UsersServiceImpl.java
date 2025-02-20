package school21.spring.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;

import java.util.UUID;

@Component
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplateImpl") UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public String signUp(String email) {
        String tempPassword = UUID.randomUUID().toString();
        User user = new User(email, tempPassword);
        usersRepository.save(user);
        return tempPassword;
    }
}