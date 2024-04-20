package com.nkd.eida_backend.Service.Impl;

import com.nkd.eida_backend.Entity.NotificationEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Repository.NotificationRepository;
import com.nkd.eida_backend.Service.NotificationService;
import com.nkd.eida_backend.Utils.NotificationUtils;
import com.nkd.eida_backend.dtoRequest.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationEntity saveNotification(UserEntity sender, UserEntity receiver, MessageRequest messageRequest) {
        NotificationEntity notification = NotificationUtils.createNotification(sender, receiver, messageRequest);
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public List<NotificationEntity> getUserNotifications(String email) {
        return notificationRepository.findAllByReceiverEmail(email).orElse(null);
    }
}
