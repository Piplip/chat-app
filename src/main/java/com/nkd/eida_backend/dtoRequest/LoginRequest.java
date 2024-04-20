package com.nkd.eida_backend.dtoRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotNull(message = "Email can not be empty or null")
    private String email;
    @NotNull(message = "Password can not be empty or null")
    private String password;
}
