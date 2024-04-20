package com.nkd.eida_backend.Utils;

import com.nkd.eida_backend.Exception.ApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailUtils {
    public static String getRegistrationMessage(String name, String host, String token){
        return "Hello " + name + ",\n\n" +
                "Thank you for registering with EIDA. Please click the link below to verify your email address.\n\n" +
                getVerificationUrl(host, token) + "\n\n" +
                "If you did not register with EIDA, please ignore this email.\n\n" +
                "Regards,\n" +
                "EIDA Team";
    }

    private static String getVerificationUrl(String host, String token) {
        return host + "/user/verify?token=" + token;
    }

    public static String getPasswordResetEmail(String name, String token){
        return "Hello " + name + ",\n\n" +
                "You have requested to reset your password. Please click the link below to reset your password.\n\n" +
                "http://localhost:8080/api/v1/auth/reset-password?token=" + token + "\n\n" +
                "If you did not request to reset your password, please ignore this email.\n\n" +
                "Regards,\n" +
                "EIDA Team";
    }

    public static void handleEmailException(Exception e) {
        log.error("Error sending email: {}", e.getMessage());
        throw new ApiException("Error sending email");
    }
}
