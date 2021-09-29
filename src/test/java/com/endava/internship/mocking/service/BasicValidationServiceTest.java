package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicValidationServiceTest {

    BasicValidationService validationService;

    @BeforeEach
    void init() {
        validationService = new BasicValidationService();
    }
    @Test
    public void validateAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(null));
        assertEquals(exception.getMessage(), "Amount must not be null");
        exception = assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(-5.7));
        assertEquals(exception.getMessage(), "Amount must be greater than 0");
    }

    //

    @Test
    public void validatePaymentId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validationService.validatePaymentId(null));
        assertEquals(exception.getMessage(), "Payment id must not be null");
        //System.out.println("Test print");
    }

    @Test
    public void validateUserId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validationService.validateUserId(null));
        assertEquals(exception.getMessage(), "User id must not be null");
    }

    @Test
    public void validateUser() {
        User user = new User(10, "John", Status.INACTIVE);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validationService.validateUser(user));
        assertEquals(exception.getMessage(), "User with id " + user.getId() + " not in ACTIVE status");
    }

    @Test
    public void validateMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validationService.validateMessage(null));
        assertEquals(exception.getMessage(), "Payment message must not be null");
    }
}
