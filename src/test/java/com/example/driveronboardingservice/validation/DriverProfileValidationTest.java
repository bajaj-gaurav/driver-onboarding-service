package com.example.driveronboardingservice.validation;

import com.example.driveronboardingservice.dto.DriverProfileDto;
import com.example.driveronboardingservice.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DriverProfileValidationTest {

    @Autowired
    private DriverProfileValidation driverProfileValidation;

    private DriverProfileDto driverProfileDto;

    @BeforeEach
    public void setUp() {
        driverProfileDto = new DriverProfileDto();
        driverProfileDto.setEmail("abcdef@gmail.com");
        driverProfileDto.setFirstName("abc");
        driverProfileDto.setPassword("Abcdef12345");
        driverProfileDto.setLastName("def");
        driverProfileDto.setCity("Mumbai");
        driverProfileDto.setState("Maharashtra");
        driverProfileDto.setPhoneNo("9876543210");
    }


    @Test
    public void testSuccessIsValidInput() {
        assertDoesNotThrow(() -> driverProfileValidation.isValidInput(driverProfileDto));
    }

    @Test
    public void testFailureInvalidFirstNameIsValidInput() {
        driverProfileDto.setFirstName("abc123");

        assertThrows(InvalidInputException.class, () -> driverProfileValidation.isValidInput(driverProfileDto), "The firstname is not valid");

    }

    @Test
    public void testFailureInvalidLastNameIsValidInput() {
        driverProfileDto.setLastName("def123");

        assertThrows(InvalidInputException.class, () -> driverProfileValidation.isValidInput(driverProfileDto), "The lastname is not valid");
    }

    @Test
    public void testFailureInvalidEmailIsValidInput() {
        driverProfileDto.setEmail("abcgmail.com");

        assertThrows(InvalidInputException.class, () -> driverProfileValidation.isValidInput(driverProfileDto), "The email is not valid");
    }

    @Test
    public void testFailureInvalidPasswordIsValidInput() {
        driverProfileDto.setPassword("abc123");

        assertThrows(InvalidInputException.class, () -> driverProfileValidation.isValidInput(driverProfileDto), "The password is not valid");
    }

    @Test
    public void testFailureInvalidPhoneNoIsValidInput() {
        driverProfileDto.setPhoneNo("987654321");

        assertThrows(InvalidInputException.class, () -> driverProfileValidation.isValidInput(driverProfileDto), "The phone number is not valid");
    }
}