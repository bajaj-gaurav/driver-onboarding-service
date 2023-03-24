package com.example.driveronboardingservice.validation;

import com.example.driveronboardingservice.dto.DriverProfileDto;
import com.example.driveronboardingservice.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DriverProfileValidation {

    //username: consisting of alphanumeric characters, period, underscore, percent sign, plus, and hyphen
    //domain name: consisting of alphanumeric characters, period, and hyphen
    private static final String EMAIL_REGEX = "^[\\w\\d._%+-]+@[\\w\\d.-]+\\.[A-Za-z]{2,}$";

    //at least 8 characters long and contains at least one digit, one lowercase letter, and one uppercase letter
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    //10 digits
    private static final String PHONE_REGEX = "^[0-9]{10}$";

    //upper case and lower case letters
    private static final String NAME_REGEX = "^[A-Za-z]{2,30}$";

    private Boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            return false;
        }
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private Boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            return false;
        }

        // Password must be at least 8 characters long and contain at least one digit,
        // one lowercase letter, and one uppercase letter

        Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();

    }

    private Boolean isPhoneNoValid(String phoneNo) {
        if (phoneNo.isEmpty()) {
            return false;
        }

        Pattern phonePattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = phonePattern.matcher(phoneNo);
        return matcher.matches();
    }

    private Boolean isNameValid(String name) {
        if (name.isEmpty()) {
            return false;
        }


        Pattern namePattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = namePattern.matcher(name);
        return matcher.matches();

    }

    public void isValidInput(DriverProfileDto driverProfileDto) throws InvalidInputException {

        if (!isEmailValid(driverProfileDto.getEmail())) {
            throw new InvalidInputException("The email is not valid");
        }
        if (!isPasswordValid(driverProfileDto.getPassword())) {
            throw new InvalidInputException("The password is not valid");
        }
        if (!isPhoneNoValid(driverProfileDto.getPhoneNo())) {
            throw new InvalidInputException("The phone number is not valid");
        }
        if (!isNameValid(driverProfileDto.getFirstName())) {
            throw new InvalidInputException("The firstname is not valid");
        }

        if (!isNameValid(driverProfileDto.getLastName())) {
            throw new InvalidInputException("The lastname is not valid");
        }

        if (driverProfileDto.getCity().isEmpty() || driverProfileDto.getState().isEmpty()) {
            throw new InvalidInputException("City or State name is not valid");
        }
    }
}
