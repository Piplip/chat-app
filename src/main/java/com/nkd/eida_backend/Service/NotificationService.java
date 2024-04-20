package com.nkd.eida_backend.Service;

import com.nkd.eida_backend.Entity.NotificationEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.dtoRequest.MessageRequest;

import java.util.List;

public interface NotificationService {
    NotificationEntity saveNotification(UserEntity sender, UserEntity receiver, MessageRequest messageRequest);
    List<NotificationEntity> getUserNotifications(String email);
}
