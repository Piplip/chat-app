package com.nkd.eida_backend.dtoRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Email can not be empty or null")
    private String email;
    @NotNull(message = "Password can not be empty or null")
    private String password;
    private String bio;
    private String phoneNumber;
}
