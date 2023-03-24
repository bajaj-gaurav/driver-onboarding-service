package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.dto.DriverAdditionalInfoDto;
import com.example.driveronboardingservice.dto.mapper.DriverAdditionalInfoMapper;
import com.example.driveronboardingservice.entity.DriverAdditionalInfo;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.exception.ActionNotAllowedException;
import com.example.driveronboardingservice.exception.DatabaseException;
import com.example.driveronboardingservice.infrastructure.output.MailServiceClient;
import com.example.driveronboardingservice.model.DriverOnboardingStatus;
import com.example.driveronboardingservice.repository.DriverAdditionalInfoRepository;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import com.example.driveronboardingservice.repository.EmailVerificationRepository;
import com.example.driveronboardingservice.util.EmailMessageUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static com.example.driveronboardingservice.model.DriverOnboardingStatus.BACKGROUND_CHECK_IN_PROGRESS;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private DriverProfileRepository driverProfileRepository;

    @Autowired
    private DriverAdditionalInfoRepository driverAdditionalInfoRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DriverOnboardingStatusLogService driverOnboardingStatusLogService;

    @Autowired
    private DriverAdditionalInfoMapper driverAdditionalInfoMapper;

    @Autowired
    private EmailMessageUtility emailMessageUtility;

    @Autowired
    private MailServiceClient mailServiceClient;


    /**
     * Function to save additional details if all the checks go through.
     * The onboarding status is changed to Background Check In Progress if all the documents are uploaded.
     * @param username
     * @param additionalInfo
     * @return
     * @throws DatabaseException
     * @throws ActionNotAllowedException
     * @throws MessagingException
     */
    public String saveDriverProfileAdditionalInfo(String username, DriverAdditionalInfoDto additionalInfo) throws DatabaseException, ActionNotAllowedException, MessagingException {
        DriverProfile driverProfileInfo = driverProfileRepository.findByEmail(username);

        if (driverProfileInfo.getOnboardingStatus().equals(BACKGROUND_CHECK_IN_PROGRESS.name())) {
            log.error("The additional information has already been submitted");
            throw new ActionNotAllowedException("The additional information has already been submitted");
        }

        if (driverAdditionalInfoRepository.findByDriverProfile(driverProfileInfo) == null) {
            DriverAdditionalInfo driverAdditionalInfo = driverAdditionalInfoMapper.mapDTOtoEntity(additionalInfo);
            driverAdditionalInfo.setDriverProfile(driverProfileInfo);
            DriverAdditionalInfo savedDriverAdditionalInfo = driverAdditionalInfoRepository.save(driverAdditionalInfo);
            if (savedDriverAdditionalInfo.getId() == null) {
                log.error("Additional details couldn't be saved for user: {}", username);
                throw new DatabaseException("Additional details couldn't be saved. Please try again.");
            }
        } else {
            log.error("The additional information has already been submitted");
            throw new ActionNotAllowedException("The information has already been submitted");
        }

        //change the status of the driver
        log.info("Checking if all the relevant document has been submitted for user: {}", username);
        if (driverProfileRepository.checkDocumentUpload(driverProfileInfo.getId())) {
            log.info("Found all the required document for user: {}", username);
            driverProfileRepository.changeAction(driverProfileInfo.getId(), BACKGROUND_CHECK_IN_PROGRESS.name());
            DriverProfile updatedDriverProfile = driverProfileRepository.findByEmail(username);
            if (updatedDriverProfile.getOnboardingStatus().equals(BACKGROUND_CHECK_IN_PROGRESS.name())) {
                driverOnboardingStatusLogService.storeOnboardingStatusLog(updatedDriverProfile);
                mailServiceClient.sendEmail(updatedDriverProfile.getEmail(), emailMessageUtility.createMessageForOnboardingStatusChange());
                cacheService.delete(username);
                return "BackGround Check Started";
            } else {
                log.error("The form didn't get submitted for user: {}", username);
                throw new DatabaseException("The form didn't get submitted. Please try again in a while");
            }
        } else {
            log.error("User:{} tried submitting without uploading all the relevant documents", username);
            throw new ActionNotAllowedException("Please upload all the required documents before submitting");
        }
    }
}
