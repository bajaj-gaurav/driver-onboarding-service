package com.example.driveronboardingservice.util;

import com.example.driveronboardingservice.infrastructure.output.MailServiceClient;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageUtility {

    @Autowired
    private MailServiceClient mailServiceClient;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String generateOneTimePassword() {
        String OTP = RandomString.make(8);
        String encodedOTP = passwordEncoder.encode(OTP);

        return encodedOTP;
    }


    public String createMessageForEmailVerification(String username, String otp) {
        String url = "http://localhost:8080/verify/email?username=" + username + "&otp=" + otp + " ";
        String content = "<p>Hello </p> <p>For security reason, you're required to verify you email address. " +
                "Please click on the below link. </p><br>" +
                "<a href=" + url + ">Verification Link</a><br>";

        return content;
    }

    public String createMessageForDriverEmailVerificationFailure() {
        String content = "The email verification has failed due to some technical issue. " +
                "Our customer representative will connect to you shortly";
        return content;
    }

    public String createMessageForOnboardingStatusChange() {
        String content = "Thank you for submitting all the documents and information. " +
                "Our background verification team is working on it. We will reach out to you shortly";
        return content;
    }

}
