package com.nkd.eida_backend.Service;

public interface EmailService {
    void sendRegistrationEmail(String name, String email, String token);

    void sendPasswordResetEmail(String firstName, String token, String email);
}
