package com.example.driveronboardingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class DriverProfileDto {


    @JsonProperty(value = "first_name", required = true)
    private String firstName;

    @JsonProperty(value = "last_name", required = true)
    private String lastName;

    @JsonProperty(required = true)
    private String email;

    @JsonProperty(required = true)
    private String password;

    @JsonProperty(value = "phone_no", required = true)
    private String phoneNo;

    @JsonProperty(required = true)
    private String city;

    @JsonProperty(required = true)
    private String state;
}
