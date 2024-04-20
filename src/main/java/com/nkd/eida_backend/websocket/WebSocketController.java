package com.nkd.eida_backend.websocket;

import com.nkd.eida_backend.Entity.NotificationEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.NotificationType;
import com.nkd.eida_backend.Enumeration.UserStatus;
import com.nkd.eida_backend.Service.ConversationService;
import com.nkd.eida_backend.Service.FriendService;
import com.nkd.eida_backend.Service.NotificationService;
import com.nkd.eida_backend.Service.UserService;
import com.nkd.eida_backend.dtoRequest.ConversationRequest;
import com.nkd.eida_backend.dtoRequest.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final FriendService friendService;
    private final UserService userService;
    private final ConversationService conversationService;

    @MessageMapping("/private")
    public void sendToSpecific(@Payload MessageRequest messageRequest) {
        var sender = userService.getUserByEmail(messageRequest.getFrom());
        var receiver = userService.getUserByEmail(messageRequest.getTo());
        NotificationEntity notificationRequest = notificationService.saveNotification(sender, receiver, messageRequest);
        switch (messageRequest.getType()) {
            case FRIEND_REQUEST -> friendService.createFriendship(sender, receiver);
            case ACCEPT_FRIEND_REQUEST, DECLINE_FRIEND_REQUEST ->
                    friendService.handleFriendRequest(sender, receiver, messageRequest.getType());
        }
        if (receiver.getUserStatus().equals(UserStatus.ACTIVE)) {
            if (messageRequest.getType().equals(NotificationType.ACCEPT_FRIEND_REQUEST)) {
                var conversationResponse = conversationService.createConversationResponse(sender, receiver);
                messagingTemplate.convertAndSendToUser(messageRequest.getFrom(), "/specific", conversationResponse.getSecond());
                messagingTemplate.convertAndSendToUser(messageRequest.getTo(), "/specific", conversationResponse.getFirst());
            }
            messagingTemplate.convertAndSendToUser(messageRequest.getTo(), "/specific", notificationRequest);
        }
    }

    @MessageMapping("/chat")
    public void sendToChat(@Payload ConversationRequest conversationRequest) {
        conversationService.updateConversation(conversationRequest);
        List<UserEntity> users = conversationService.getAllUsersByConversationID(conversationRequest.getConversationID());
        for(UserEntity user : users){
            if(user.getUserStatus().equals(UserStatus.ACTIVE) && !user.getEmail().equals(conversationRequest.getFrom())){
                messagingTemplate.convertAndSendToUser(conversationRequest.getTo(), "/specific", conversationRequest);
            }
        }
    }
}











