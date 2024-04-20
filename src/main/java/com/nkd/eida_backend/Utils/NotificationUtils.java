package com.nkd.eida_backend.Utils;

import com.nkd.eida_backend.Entity.NotificationEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.NotificationType;
import com.nkd.eida_backend.dtoRequest.MessageRequest;

import java.time.LocalDateTime;

public class NotificationUtils {
    public static NotificationEntity createNotification(UserEntity sender, UserEntity receiver, MessageRequest messageRequest) {
        return NotificationEntity.builder()
                .senderName(sender.getLastName() + " " + sender.getFirstName())
                .senderEmail(sender.getEmail())
                .senderImageUrl(sender.getImageUrl())
                .content(generateContent(sender, receiver, messageRequest.getType()))
                .notificationType(messageRequest.getType())
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .receiver(receiver)
                .build();
    }

    private static String generateContent(UserEntity sender, UserEntity receiver, NotificationType type) {
        return switch (type) {
            case FRIEND_REQUEST -> sender.getLastName() + " " + sender.getFirstName() + " sent you a friend request";
            case ACCEPT_FRIEND_REQUEST ->
                    sender.getLastName() + " " + sender.getFirstName() + " accepted your friend request";
            case DECLINE_FRIEND_REQUEST ->
                    sender.getLastName() + " " + sender.getFirstName() + " declined your friend request";
            case MESSAGE -> sender.getLastName() + " " + sender.getFirstName() + " sent you a message";
        };
    }

}
