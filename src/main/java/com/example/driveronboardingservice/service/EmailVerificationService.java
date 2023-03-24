package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.entity.EmailVerification;
import com.example.driveronboardingservice.exception.EmailNotificationException;
import com.example.driveronboardingservice.infrastructure.output.MailServiceClient;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import com.example.driveronboardingservice.repository.EmailVerificationRepository;
import com.example.driveronboardingservice.util.EmailMessageUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailVerificationService {

    private static final String ADMIN_MAIL_ID = "gauravtestbajaj@gmail.com";
    @Autowired
    private EmailMessageUtility emailMessageUtility;

    @Autowired
    private DriverProfileRepository driverProfileRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MailServiceClient mailServiceClient;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DriverOnboardingStatusLogService driverOnboardingStatusLogService;


    /**
     * Function to verify email address for a valid user.
     * @param driverProfile
     * @param otp
     * @return
     * @throws EmailNotificationException
     */
    public String verifyEmailUserValid(DriverProfile driverProfile, String otp) throws EmailNotificationException {
        String username = driverProfile.getEmail();
        String retrievedOtp = "";
        EmailVerification emailVerificationEntity = emailVerificationRepository.findByDriverProfile(driverProfile);
        if (emailVerificationEntity != null) {
            retrievedOtp = emailVerificationEntity.getOtp();
        } else {
            log.error("Email couldn't get verified for user: {} ", username);
            throw new EmailNotificationException("Email couldn't get verified for user " + username);
        }

        if (otp.equals(retrievedOtp)) {
            log.info("Otp received matched with the stored otp for user: {}", username);
            driverProfileRepository.changeEnabledStatus(driverProfile.getId(), true);
        } else {
            log.error("Email couldn't get verified for user: {} ", username);
            throw new EmailNotificationException("Email couldn't get verified for user " + username);
        }

        DriverProfile updatedDriverProfile = driverProfileRepository.findByEmail(username);
        if (updatedDriverProfile.getEnabled()) {
            log.info("Successfully changed the status of user:{} to enabled", username);
            driverOnboardingStatusLogService.storeOnboardingStatusLog(updatedDriverProfile);
            cacheService.delete(username);
            return "Verified email for user: " + username;
        } else {
            log.error("Email couldn't get verified for user: {} ", username);
            throw new EmailNotificationException("Email couldn't get verified for user " + username);
        }
    }

    /**
     * Function to verify email address.
     * @param username
     * @param otp
     * @return
     * @throws UsernameNotFoundException
     * @throws EmailNotificationException
     * @throws MessagingException
     */
    public String verifyEmail(String username, String otp) throws UsernameNotFoundException, EmailNotificationException, MessagingException {
        DriverProfile driverProfile = driverProfileRepository.findByEmail(username);
        if (driverProfile == null) {
            log.error("The user:{} has not started the registration process", username);
            throw new UsernameNotFoundException("The user has not started the registration process");
        }
        try {
            return verifyEmailUserValid(driverProfile, otp);
        } catch (EmailNotificationException e) {
            //notifyUserAndAdmin(username, e.getMessage());
            mailServiceClient.sendEmail(username, emailMessageUtility.createMessageForDriverEmailVerificationFailure());
            mailServiceClient.sendEmail(ADMIN_MAIL_ID, e.getMessage());
            log.info("Sent the mails to user and admin for the email verification failure for user: {}", username);
            throw new EmailNotificationException(e.getMessage());
        }
    }

    /**
     * Function to notify admin and user if email verification fails due to technical issues.
     * @param username
     * @param adminMessage
     */
    private void notifyUserAndAdmin(String username, String adminMessage) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                mailServiceClient.sendEmail(username, emailMessageUtility.createMessageForDriverEmailVerificationFailure());
            } catch (MessagingException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
        executor.submit(() -> {
            try {
                mailServiceClient.sendEmail(ADMIN_MAIL_ID, adminMessage);
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
        });
        executor.shutdown();
    }


    /**
     * Function to send email for email address verification.
     * @param username
     * @throws MessagingException
     * @throws EmailNotificationException
     */
    public void sendEmailForVerification(String username) throws MessagingException, EmailNotificationException {
        log.info("Received request for sending notification for email verification for user: {}", username);
        DriverProfile driverProfile = driverProfileRepository.findByEmail(username);
        if (driverProfile == null) {
            log.error("The user: {} has not started the registration process", username);
            throw new UsernameNotFoundException("The user has not started the registration process");
        }
        EmailVerification emailVerification = new EmailVerification();

        String otp = emailMessageUtility.generateOneTimePassword();
        log.info("Generated the otp for user email verification: {}", username);
        emailVerification.setOtp(otp);
        emailVerification.setDriverProfile(driverProfile);
        String bodyContent = emailMessageUtility.createMessageForEmailVerification(username, otp);
        mailServiceClient.sendEmail(username, bodyContent);
        EmailVerification savedEmailVerification = emailVerificationRepository.save(emailVerification);
        if (savedEmailVerification.getId() == null) {
            log.error("Couldn't save the otp. Please retry the registration process. user: {}", username);
            throw new EmailNotificationException("Couldn't save the otp. Please retry the registration process");
        }
    }
}
