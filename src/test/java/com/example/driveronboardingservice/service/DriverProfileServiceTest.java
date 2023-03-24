package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.dto.DriverDetailsDto;
import com.example.driveronboardingservice.dto.DriverProfileDto;
import com.example.driveronboardingservice.dto.mapper.DriverDetailDtoMapper;
import com.example.driveronboardingservice.dto.mapper.DriverProfileMapper;
import com.example.driveronboardingservice.entity.DriverAdditionalInfo;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.entity.Role;
import com.example.driveronboardingservice.exception.DatabaseException;
import com.example.driveronboardingservice.exception.DriverAlreadyExistException;
import com.example.driveronboardingservice.exception.DriverRegistrationException;
import com.example.driveronboardingservice.exception.InvalidInputException;
import com.example.driveronboardingservice.repository.DriverAdditionalInfoRepository;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import com.example.driveronboardingservice.validation.DriverProfileValidation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class DriverProfileServiceTest {

    @MockBean
    private DriverAdditionalInfoRepository driverAdditionalInfoRepository;
    @MockBean
    private DriverDetailDtoMapper driverDetailDtoMapper;
    @Autowired
    private DriverProfileService driverProfileService;
    @MockBean
    private DriverProfileRepository driverProfileRepository;
    @MockBean
    private DriverProfileValidation driverProfileValidation;
    @MockBean
    private CacheService cacheService;
    @MockBean
    private DriverProfileMapper driverProfileMapper;
    @MockBean
    private RoleService roleService;
    @MockBean
    private EmailVerificationService emailVerificationService;

    @Test
    public void testSuccessSaveProfile() throws Exception {

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setId(1L);
        driverProfile.setFirstName("samiksha");

        doNothing().when(driverProfileValidation).isValidInput(any());

        when(driverProfileRepository.findByEmail(any())).thenReturn(null);

        when(driverProfileMapper.mapDTOtoEntity(any())).thenReturn(driverProfile);

        when(roleService.getDriverRole(any())).thenReturn(new Role());

        when(driverProfileRepository.save(any())).thenReturn(driverProfile);

        doNothing().when(emailVerificationService).sendEmailForVerification(any());


        assertEquals("Registration for User:samiksha is successfully initiated. Please check your inbox. Please verify the mail sent.", driverProfileService.saveProfile(new DriverProfileDto()));
    }

    @Test
    public void testFailureUserAlreadyExistsSaveProfile() throws InvalidInputException {

        doNothing().when(driverProfileValidation).isValidInput(any());

        when(driverProfileRepository.findByEmail(any())).thenReturn(new DriverProfile());

        assertThrows(DriverAlreadyExistException.class, () -> driverProfileService.saveProfile(new DriverProfileDto()), "Account already registered/initiated");

    }

    @Test
    public void testFailureUserNotGettingRegisteredSaveProfile() throws Exception {

        DriverProfile driverProfile = new DriverProfile();

        doNothing().when(driverProfileValidation).isValidInput(any());

        when(driverProfileRepository.findByEmail(any())).thenReturn(null);

        when(driverProfileMapper.mapDTOtoEntity(any())).thenReturn(driverProfile);

        when(roleService.getDriverRole(any())).thenReturn(new Role());

        when(driverProfileRepository.save(any())).thenReturn(driverProfile);

        assertThrows(DriverRegistrationException.class, () -> driverProfileService.saveProfile(new DriverProfileDto()), "The user didn't get registered. Please try again in a while");

    }

    @Test
    public void testSuccessGetDetailsFromCacheGetDriverProfileDetails() throws DatabaseException {
        when(cacheService.get(any())).thenReturn(new DriverDetailsDto());

        assertEquals(new DriverDetailsDto(), driverProfileService.getDriverProfileDetails("samiksha@gmail.com"));
    }

    @Test
    public void testSuccessGetDetailsFromDatabaseGetDriverProfileDetails() throws DatabaseException {
        when(cacheService.get(any())).thenReturn(null);

        when(driverProfileRepository.findByEmail(any())).thenReturn(new DriverProfile());

        doNothing().when(driverDetailDtoMapper).mapDriverProfileToDriverDetailDto(any(), any());

        when(driverAdditionalInfoRepository.findByDriverProfile(any())).thenReturn(new DriverAdditionalInfo());

        doNothing().when(driverDetailDtoMapper).mapDriverAdditionalInfoToDriverDetailDto(any(), any());

        doNothing().when(cacheService).put(any(), any());

        when(cacheService.get(any())).thenReturn(new DriverDetailsDto());

        assertEquals(new DriverDetailsDto(), driverProfileService.getDriverProfileDetails("samiksha@gmail.com"));
    }

    @Test
    public void testFailureUnableToConnectDatabaseGetDriverProfileDetails() throws DatabaseException {
        when(cacheService.get(any())).thenReturn(null);

        when(driverProfileRepository.findByEmail(any())).thenReturn(null);

        assertThrows(DatabaseException.class, () -> driverProfileService.getDriverProfileDetails("samiksha@gmail.com"), "Couldn't fetch the record for user: samiksha");
    }

}