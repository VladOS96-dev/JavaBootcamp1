package edu.school21.services;
import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {
    private UsersRepository usersRepository;
    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        usersService = new UsersServiceImpl(usersRepository);
    }

    @Test
    void authenticate_CorrectLoginAndPassword() {
        User user = new User(1L, "testUser", "password123", false);
        when(usersRepository.findByLogin("testUser")).thenReturn(user);

        boolean result = usersService.authenticate("testUser", "password123");

        assertTrue(result);
        verify(usersRepository, times(1)).update(user);
    }

    @Test
    void authenticate_WrongLogin() {
        when(usersRepository.findByLogin("wrongUser")).thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> usersService.authenticate("wrongUser", "password123"));
    }

    @Test
    void authenticate_WrongPassword() {
        User user = new User(1L, "testUser", "correctPassword", false);
        when(usersRepository.findByLogin("testUser")).thenReturn(user);

        boolean result = usersService.authenticate("testUser", "wrongPassword");

        assertFalse(result);
        verify(usersRepository, never()).update(any());
    }

    @Test
    void authenticate_AlreadyAuthenticated() {
        User user = new User(1L, "testUser", "password123", true);
        when(usersRepository.findByLogin("testUser")).thenReturn(user);

        assertThrows(AlreadyAuthenticatedException.class, () -> usersService.authenticate("testUser", "password123"));
        verify(usersRepository, never()).update(any());
    }
}