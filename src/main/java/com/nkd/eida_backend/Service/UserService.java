package com.nkd.eida_backend.Service;

import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.LoginType;
import com.nkd.eida_backend.dtoRequest.UserRequest;

import java.util.List;

public interface UserService {
    void createUser(UserRequest userRequest);
    UserEntity getUserByEmail(String email);
    void verifyUserAccount(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    String handleLogin(String email, String password);
    List<UserEntity> findFriends(String query);
}
