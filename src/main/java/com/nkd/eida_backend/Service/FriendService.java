package com.nkd.eida_backend.Service;

import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.NotificationType;

import java.util.List;

public interface FriendService {
    void createFriendship(UserEntity sender, UserEntity receiver);
    void handleFriendRequest(UserEntity sender, UserEntity receiver, NotificationType type);
    List<UserEntity> getUserFriends(String email);
}
