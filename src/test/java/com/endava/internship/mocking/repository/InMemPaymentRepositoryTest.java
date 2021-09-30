package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.InMemUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemPaymentRepositoryTest {

    InMemUserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository = new InMemUserRepository();
    }

    @Test
    void findById() {
        User expectedUser = new User(1, "John", Status.ACTIVE);

        assertEquals(userRepository.findById(1), Optional.of(expectedUser));
    }

    @Test
    void whenPutNullThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userRepository.findById(null));
        assertEquals(exception.getMessage(), "User id must not be null");
    }

    @Test
    void whenPutIdOfNotExistingUser() {
        assertEquals(userRepository.findById(0), Optional.empty());
    }
}
