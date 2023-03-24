package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.dto.DriverDetailsDto;
import com.example.driveronboardingservice.dto.DriverProfileDto;
import com.example.driveronboardingservice.dto.mapper.DriverDetailDtoMapper;
import com.example.driveronboardingservice.dto.mapper.DriverProfileMapper;
import com.example.driveronboardingservice.entity.DriverAdditionalInfo;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.exception.*;
import com.example.driveronboardingservice.repository.DriverAdditionalInfoRepository;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import com.example.driveronboardingservice.validation.DriverProfileValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Objects;

import static com.example.driveronboardingservice.constants.AppConstants.DRIVER_ROLE;

@Service
@Slf4j
public class DriverProfileService {

    @Autowired
    private DriverProfileRepository driverProfileRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DriverProfileValidation driverProfileValidation;

    @Autowired
    private DriverAdditionalInfoRepository driverAdditionalInfoRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DriverProfileMapper driverProfileMapper;


    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private DriverDetailDtoMapper driverDetailDtoMapper;


    /**
     * Function to save the registration details of the user if all validations are passed.
     * @param driverProfileDto
     * @return
     * @throws InvalidInputException
     * @throws DriverAlreadyExistException
     * @throws RoleNotFoundException
     * @throws EmailNotificationException
     * @throws MessagingException
     * @throws DriverRegistrationException
     */
    public String saveProfile(DriverProfileDto driverProfileDto) throws InvalidInputException, DriverAlreadyExistException, RoleNotFoundException, EmailNotificationException, MessagingException, DriverRegistrationException {

        driverProfileValidation.isValidInput(driverProfileDto);

        if (isDriverExists(driverProfileDto.getEmail())) {
            throw new DriverAlreadyExistException("Account already registered/initiated");
        }

        log.info("Registration details validated for user: {}", driverProfileDto.getEmail());

        DriverProfile driverProfile = driverProfileMapper.mapDTOtoEntity(driverProfileDto);
        log.info("Driver profile created for user: {}", driverProfile.getEmail());
        driverProfile.setRole(roleService.getDriverRole(DRIVER_ROLE));
        DriverProfile savedDriverProfile = null;
        savedDriverProfile = driverProfileRepository.save(driverProfile);
        if (savedDriverProfile.getId() != null) {
            emailVerificationService.sendEmailForVerification(driverProfileDto.getEmail());
            log.info("Registration for User {} {} is successfully initiated. ", savedDriverProfile.getFirstName(), savedDriverProfile.getLastName());
            return "Registration for User:" + savedDriverProfile.getFirstName() + " is successfully initiated. Please check your inbox. Please verify the mail sent.";
        } else {
            throw new DriverRegistrationException("The user didn't get registered. Please try again in a while");
        }
    }

    /**
     * Function to check if the driver already exist in the database.
     * @param email
     * @return
     */
    private boolean isDriverExists(String email) {
        return driverProfileRepository.findByEmail(email) != null;
    }

    /**
     * Function to get all the profile details of the driver.
     * @param username
     * @return
     * @throws DatabaseException
     */
    public DriverDetailsDto getDriverProfileDetails(String username) throws DatabaseException {

        DriverDetailsDto driverDetailsDtoFromCache = cacheService.get(username);
        if (driverDetailsDtoFromCache != null) {
            return driverDetailsDtoFromCache;
        }

        DriverDetailsDto driverDetailsDto = new DriverDetailsDto();
        DriverProfile driverProfile = driverProfileRepository.findByEmail(username);

        if (driverProfile == null) {
            log.error("Couldn't fetch the record for user: {}", username);
            throw new DatabaseException("Couldn't fetch the record for user: " + username);
        }

        driverDetailDtoMapper.mapDriverProfileToDriverDetailDto(driverDetailsDto, driverProfile);

        DriverAdditionalInfo driverAdditionalInfo = driverAdditionalInfoRepository.findByDriverProfile(driverProfile);
        if (driverAdditionalInfo != null) {
            driverDetailDtoMapper.mapDriverAdditionalInfoToDriverDetailDto(driverDetailsDto, driverAdditionalInfo);
        }

        cacheService.put(username, driverDetailsDto);
        driverDetailsDtoFromCache = cacheService.get(username);
        return Objects.requireNonNullElse(driverDetailsDtoFromCache, driverDetailsDto);
    }
}
