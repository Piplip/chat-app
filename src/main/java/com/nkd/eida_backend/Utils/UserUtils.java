package com.nkd.eida_backend.Utils;

import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.Role;
import com.nkd.eida_backend.Enumeration.UserStatus;

import java.time.LocalDateTime;

public class UserUtils {

    public static UserEntity createUserEntity(String firstName, String lastName, String email){
        return UserEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .imageUrl("https://static-00.iconduck.com/assets.00/user-icon-2048x2048-ihoxz4vq.png")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .lastLogin(LocalDateTime.now())
                .role(Role.USER)
                .userStatus(UserStatus.OFFLINE)
                .build();
    }
}
