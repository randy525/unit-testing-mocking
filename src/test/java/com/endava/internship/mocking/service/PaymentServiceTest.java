package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    ValidationService validationServiceMock ;
    UserRepository userRepositoryMock;
    PaymentRepository paymentRepositoryMock;
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
        validationServiceMock = mock(ValidationService.class);
        userRepositoryMock = mock(UserRepository.class);
        paymentRepositoryMock = mock(PaymentRepository.class);
        paymentService = new PaymentService(userRepositoryMock, paymentRepositoryMock, validationServiceMock);

    }

    @Test
    void testCreatePaymentForExistentUser() {
        User expectedUser = new User(0, "John", Status.ACTIVE);

        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.of(expectedUser));

        ArgumentCaptor<Payment> argumentCaptor = ArgumentCaptor.forClass(Payment.class);

        paymentService.createPayment(0, 150.5);

        verify(validationServiceMock).validateAmount(150.5);
        verify(validationServiceMock).validateUserId(0);
        verify(validationServiceMock).validateUser(expectedUser);

        verify(userRepositoryMock).findById(0);
        verify(paymentRepositoryMock).save(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue().getUserId(), expectedUser.getId());
        assertEquals(argumentCaptor.getValue().getAmount(), 150.5);
        assertTrue(argumentCaptor.getValue().getMessage().contains(expectedUser.getName()));

        verifyNoMoreInteractions(validationServiceMock);
        verifyNoMoreInteractions(paymentRepositoryMock);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    void editMessageInteractionWithCollaboratorsCheck() {
        final UUID paymentID = UUID.randomUUID();
        final String newString = "Any String";
        paymentService.editPaymentMessage(paymentID, newString);

        verify(validationServiceMock).validatePaymentId(paymentID);
        verify(validationServiceMock).validateMessage(newString);

        verify(paymentRepositoryMock).editMessage(paymentID, newString);

        verifyNoMoreInteractions(validationServiceMock);
        verifyNoMoreInteractions(paymentRepositoryMock);
    }

    @Test
    void getAllByAmountExceeding() {
        final double amount = 13.37;

        List<Payment> paymentsList = Arrays.asList(
                new Payment(1, 15.0, "text"),
                new Payment(2, 5.0, "text"),
                new Payment(3, 10.0, "text"),
                new Payment(4, 17.0, "text"),
                new Payment(5, 14.0, "text")
        );

        List<Payment> expectedList = Arrays.asList(
                paymentsList.get(0),
                paymentsList.get(3),
                paymentsList.get(4)
        );

        when(paymentRepositoryMock.findAll()).thenReturn(paymentsList);

        assertEquals(paymentService.getAllByAmountExceeding(amount), expectedList);

        verify(paymentRepositoryMock).findAll();

        verifyNoMoreInteractions(paymentRepositoryMock);
    }
}
