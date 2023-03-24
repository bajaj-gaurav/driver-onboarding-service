package com.example.driveronboardingservice.constants;

import com.example.driveronboardingservice.model.RolesAvailable;

import java.util.Set;

public class AppConstants {

    public final static String DRIVER_ROLE = RolesAvailable.ROLE_DRIVER.name();
    public final static String ADMIN_ROLE = RolesAvailable.ROLE_ADMIN.name();
    public final static int MAX_FILE_SIZE_LIMIT = 5000000;
    public static final Set<String> VALID_FILE_EXTENSIONS = Set.of("png", "jpg", "jpeg", "pdf", "zip");
    public static final String NOTIFICATION_SUBJECT = "Message for you from DOS Riding App";
    public static final String NOTIFICATION_SENDER = "ccare@dos.com";
}
